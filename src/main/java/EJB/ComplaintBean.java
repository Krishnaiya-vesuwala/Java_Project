/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Complaint;
import Entity.ComplaintCategory;
import Entity.ComplaintReply;
import Entity.ComplaintStatusHistory;
import Entity.Departments;
import Entity.Officers;
import Entity.SlaRules;
import Entity.Society;
import Entity.Users;
import Entity.Ward;
import Entity.Zone;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author riya vesuwala
 */
@Stateless
public class ComplaintBean implements ComplaintBeanLocal {

    @PersistenceContext(unitName = "jpu1")
    EntityManager em;

    @EJB
    NotificationBeanLocal notifyBean;

    // =========================
    // QR Code / Society Lookup
    // =========================

    @Override
    public List<Society> decodeQRCode(Integer wardID) {
        System.out.println("-----Called" + wardID);
        return em.createQuery(
                "SELECT s FROM Society s WHERE s.wardId.wardId=:wardID",
                Society.class)
                .setParameter("wardID", wardID)
                .getResultList();
    }

    // =========================
    // Complaint Creation
    // =========================

    @Override
    public void createComplaint(
            Integer userId,
            Integer categoryId,
            Integer societyId,
            Integer wardId,
            String title,
            String description,
            String priority) {

        try {
            Users user = em.find(Users.class, userId);
            ComplaintCategory category = em.find(ComplaintCategory.class, categoryId);
            Society society = em.find(Society.class, societyId);
            Ward ward = em.find(Ward.class, wardId);
            Zone zone = em.createQuery(
                    "SELECT w.zoneId FROM Ward w WHERE w.wardId = :wid",
                    Zone.class)
                    .setParameter("wid", wardId)
                    .getSingleResult();

            if (user == null || category == null || society == null
                    || ward == null || zone == null) {
                throw new Exception("Invalid foreign key while creating complaint");
            }

            SlaRules sla = em.createQuery(
                    "SELECT s FROM SlaRules s WHERE s.categoryId.categoryId=:catId",
                    SlaRules.class)
                    .setParameter("catId", categoryId)
                    .setMaxResults(1)
                    .getSingleResult();

            if (sla == null) {
                throw new RuntimeException("No SLA rule found for category");
            }

            // Time calculation
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime dueDate = now.plusMinutes(sla.getMaxResolutionDays());

            Complaint complaint = new Complaint();
            complaint.setCitizenId(user);
            complaint.setCategoryId(category);
            complaint.setSocietyId(society);
            complaint.setWardId(ward);
            complaint.setZoneId(zone);
            complaint.setDueDate(dueDate);
            complaint.setCreatedAt(now);
            complaint.setTitle(title);
            complaint.setDescription(description);
            complaint.setStatus("ACTIVE");
            complaint.setPriority(priority);

            user.getComplaintCollection().add(complaint);
            category.getComplaintCollection().add(complaint);
            society.getComplaintCollection().add(complaint);
            ward.getComplaintCollection().add(complaint);
            zone.getComplaintCollection().add(complaint);

            em.persist(complaint);
            em.flush();

            Integer generatedId = complaint.getComplaintId();
            System.out.println("Generated Complaint ID = " + generatedId);

            Officers officer = assignToWardOfficer(generatedId);

            // Notifications
            notifyBean.sendSMS(user.getMobile(),
                    "Complaint Registered Successfully. ID : " + generatedId);

            notifyBean.sendSMS(officer.getUserId().getMobile(),
                    "New Complaint Assigned. ID : " + generatedId);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Officers assignToWardOfficer(Integer complaintId) {

        Complaint complaint = em.find(Complaint.class, complaintId);

        if (complaint == null) {
            throw new RuntimeException("Complaint not found");
        }

        Ward ward = complaint.getWardId();
        Departments dept = complaint.getCategoryId().getDepartmentId();

        // First try: match by ward AND department
        List<Object[]> results = em.createQuery(
                "SELECT o, COUNT(c) FROM Officers o "
                + "LEFT JOIN Complaint c ON c.assignedOfficerId = o "
                + "AND c.status NOT IN ('RESOLVED','CLOSED') "
                + "WHERE o.designation = 'WARD_OFFICER' "
                + "AND o.wardId = :ward "
                + "AND o.departmentId = :dept "
                + "GROUP BY o "
                + "ORDER BY COUNT(c) ASC",
                Object[].class)
                .setParameter("ward", ward)
                .setParameter("dept", dept)
                .getResultList();

        // Fallback: match by ward only
        if (results.isEmpty()) {
            results = em.createQuery(
                    "SELECT o, COUNT(c) FROM Officers o "
                    + "LEFT JOIN Complaint c ON c.assignedOfficerId = o "
                    + "AND c.status NOT IN ('RESOLVED','CLOSED') "
                    + "WHERE o.designation = 'WARD_OFFICER' "
                    + "AND o.wardId = :ward "
                    + "GROUP BY o "
                    + "ORDER BY COUNT(c) ASC",
                    Object[].class)
                    .setParameter("ward", ward)
                    .getResultList();
        }

        if (results.isEmpty()) {
            throw new RuntimeException("No ward officer available for assignment");
        }

        long minLoad = (Long) results.get(0)[1];

        List<Officers> leastLoaded = new ArrayList<>();
        for (Object[] r : results) {
            if ((Long) r[1] == minLoad) {
                leastLoaded.add((Officers) r[0]);
            }
        }

        Officers selectedOfficer;
        if (leastLoaded.size() == 1) {
            selectedOfficer = leastLoaded.get(0);
        } else {
            selectedOfficer = roundRobinSelect(leastLoaded);
        }

        Officers oldOfficer = complaint.getAssignedOfficerId();
        if (oldOfficer != null) {
            oldOfficer.getComplaintCollection().remove(complaint);
        }

        complaint.setAssignedOfficerId(selectedOfficer);
        complaint.setStatus("ASSIGNED");
        selectedOfficer.getComplaintCollection().add(complaint);

        em.merge(selectedOfficer);
        em.merge(complaint);

        return selectedOfficer;
    }

    private static int lastIndex = -1;

    private Officers roundRobinSelect(List<Officers> officers) {
        if (officers.isEmpty()) {
            return null;
        }
        lastIndex = (lastIndex + 1) % officers.size();
        return officers.get(lastIndex);
    }

    // =========================
    // Complaint Retrieval
    // =========================

    @Override
    public List<Object[]> getComplaintByUserId(Integer userId) {
        return em.createQuery(
                "SELECT c.title, o.designation, c.status "
                + "FROM Complaint c LEFT JOIN c.assignedOfficerId o "
                + "WHERE c.citizenId.userId = :userId",
                Object[].class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Complaint> getAllComplaints() {
        return em.createQuery(
                "SELECT c FROM Complaint c ORDER BY c.complaintId DESC",
                Complaint.class)
                .getResultList();
    }

    @Override
    public List<Complaint> getPendingComplaints() {
        return em.createQuery(
                "SELECT c FROM Complaint c WHERE c.status='PENDING'",
                Complaint.class)
                .getResultList();
    }

    @Override
    public List<Complaint> getRecentComplaintsByUser(Integer userId) {
        return em.createQuery(
                "SELECT c FROM Complaint c "
                + "WHERE c.citizenId.userId = :uid "
                + "ORDER BY c.createdAt DESC",
                Complaint.class)
                .setParameter("uid", userId)
                .setMaxResults(5)
                .getResultList();
    }

    // =========================
    // Complaint Status History
    // =========================

    @Override
    public void createComplaintStatusHistory(
            int complaintId,
            String old_status,
            String new_status,
            Users changed_by) {

        Complaint complaint = em.find(Complaint.class, complaintId);
        ComplaintStatusHistory history = new ComplaintStatusHistory();

        complaint.getComplaintStatusHistoryCollection().add(history);
        history.setComplaintId(complaint);
        history.setOldStatus(old_status);
        history.setNewStatus(new_status);
        history.setChangedBy(changed_by);
        history.setChangedAt(LocalDateTime.now());

        em.persist(history);
    }

    @Override
    public List<ComplaintStatusHistory> getComplaintStatusHistory(int complaintId) {
        return em.createQuery(
                "SELECT c FROM ComplaintStatusHistory c "
                + "WHERE c.complaintId.complaintId = :cid "
                + "ORDER BY c.changedAt DESC",
                ComplaintStatusHistory.class)
                .setParameter("cid", complaintId)
                .getResultList();
    }

    // =========================
    // Complaint Replies
    // =========================

    @Override
    public void createComplaintReply(int complaint_id, int replied_by, String message) {
        Complaint complaint = em.find(Complaint.class, complaint_id);
        Users user = em.find(Users.class, replied_by);

        ComplaintReply reply = new ComplaintReply();
        reply.setComplaintId(complaint);
        reply.setRepliedBy(user);
        reply.setMessage(message);
        reply.setRepliedAt(LocalDateTime.now());
        complaint.getComplaintReplyCollection().add(reply);

        em.persist(reply);
        System.out.println(reply);
    }

    @Override
    public List<ComplaintReply> getComplaintReplies(int complaintId) {
        return em.createQuery(
                "SELECT r FROM ComplaintReply r "
                + "WHERE r.complaintId.complaintId = :cid "
                + "ORDER BY r.repliedAt DESC",
                ComplaintReply.class)
                .setParameter("cid", complaintId)
                .getResultList();
    }

    // =========================
    // Citizen Notifications
    // =========================

    @Override
    public List<Object[]> getCitizenNotifications(Integer userId) {
        return em.createQuery(
                "SELECT c.title, r.message, u.fullName, r.repliedAt "
                + "FROM ComplaintReply r "
                + "JOIN r.complaintId c "
                + "JOIN r.repliedBy u "
                + "WHERE c.citizenId.userId = :uid "
                + "ORDER BY r.repliedAt DESC",
                Object[].class)
                .setParameter("uid", userId)
                .getResultList();
    }

    // =========================
    // User Dashboard Statistics
    // =========================

    @Override
    public Long getTotalComplaintsByUser(Integer userId) {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.citizenId.userId = :uid",
                Long.class)
                .setParameter("uid", userId)
                .getSingleResult();
    }

    @Override
    public Long getAssignedComplaintsByUser(Integer userId) {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.citizenId.userId = :uid "
                + "AND c.status = 'ASSIGNED'",
                Long.class)
                .setParameter("uid", userId)
                .getSingleResult();
    }

    @Override
    public Long getResolvedComplaintsByUser(Integer userId) {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.citizenId.userId = :uid "
                + "AND c.status = 'RESOLVED'",
                Long.class)
                .setParameter("uid", userId)
                .getSingleResult();
    }

    @Override
    public Long getRejectedComplaintsByUser(Integer userId) {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.citizenId.userId = :uid "
                + "AND c.status = 'REJECTED'",
                Long.class)
                .setParameter("uid", userId)
                .getSingleResult();
    }

    // =========================
    // Ward Admin Dashboard
    // =========================

    @Override
    public Collection<Complaint> getComplaintsByWard(Integer wardId) {
        return em.createQuery(
                "SELECT c FROM Complaint c "
                + "WHERE c.wardId.wardId = :wid "
                + "ORDER BY c.createdAt DESC",
                Complaint.class)
                .setParameter("wid", wardId)
                .getResultList();
    }

    @Override
    public Long totalComplaints(Integer wardId) {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.wardId.wardId = :wid",
                Long.class)
                .setParameter("wid", wardId)
                .getSingleResult();
    }

    @Override
    public Long pendingComplaints(Integer wardId) {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.wardId.wardId = :wid "
                + "AND c.status = 'PENDING'",
                Long.class)
                .setParameter("wid", wardId)
                .getSingleResult();
    }

    @Override
    public Long resolvedComplaints(Integer wardId) {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.wardId.wardId = :wid "
                + "AND c.status = 'RESOLVED'",
                Long.class)
                .setParameter("wid", wardId)
                .getSingleResult();
    }

    @Override
    public Long rejectedComplaints(Integer wardId) {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.wardId.wardId = :wid "
                + "AND c.status = 'REJECTED'",
                Long.class)
                .setParameter("wid", wardId)
                .getSingleResult();
    }

    @Override
    public Collection<Society> getSocietiesByWard(Integer wardId) {
        return em.createQuery(
                "SELECT s FROM Society s "
                + "WHERE s.wardId.wardId = :wid",
                Society.class)
                .setParameter("wid", wardId)
                .getResultList();
    }

    @Override
    public Collection<Users> getCitizensByWard(Integer wardId) {
        return em.createQuery(
                "SELECT u FROM Users u "
                + "WHERE u.societyId.wardId.wardId = :wid",
                Users.class)
                .setParameter("wid", wardId)
                .getResultList();
    }

    @Override
    public Collection<Officers> getOfficersByWard(Integer wardId) {
        return em.createQuery(
                "SELECT o FROM Officers o "
                + "WHERE o.wardId.wardId = :wid",
                Officers.class)
                .setParameter("wid", wardId)
                .getResultList();
    }

    @Override
    public Collection<Officers> getWardOfficers(Integer wardId) {
        return em.createQuery(
                "SELECT o FROM Officers o "
                + "WHERE o.wardId.wardId = :wid "
                + "AND o.designation = 'WARD_OFFICER'",
                Officers.class)
                .setParameter("wid", wardId)
                .getResultList();
    }

    // =========================
    // Zone Admin Dashboard
    // =========================

    @Override
    public Long totalComplaintsByZone(Integer zoneId) {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.zoneId.zoneId = :z",
                Long.class)
                .setParameter("z", zoneId)
                .getSingleResult();
    }

    @Override
    public Long pendingComplaintsByZone(Integer zoneId) {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.status = 'PENDING' "
                + "AND c.zoneId.zoneId = :z",
                Long.class)
                .setParameter("z", zoneId)
                .getSingleResult();
    }

    @Override
    public Long resolvedComplaintsByZone(Integer zoneId) {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.status = 'RESOLVED' "
                + "AND c.zoneId.zoneId = :z",
                Long.class)
                .setParameter("z", zoneId)
                .getSingleResult();
    }

    @Override
    public Long rejectedComplaintsByZone(Integer zoneId) {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.status = 'REJECTED' "
                + "AND c.zoneId.zoneId = :z",
                Long.class)
                .setParameter("z", zoneId)
                .getSingleResult();
    }

    @Override
    public Collection<Complaint> getComplaintsByZone(Integer zoneId) {
        return em.createQuery(
                "SELECT c FROM Complaint c "
                + "WHERE c.zoneId.zoneId = :z "
                + "ORDER BY c.createdAt DESC",
                Complaint.class)
                .setParameter("z", zoneId)
                .getResultList();
    }

    @Override
    public Collection<Ward> getWardsByZone(Integer zoneId) {
        return em.createQuery(
                "SELECT w FROM Ward w "
                + "WHERE w.zoneId.zoneId = :z",
                Ward.class)
                .setParameter("z", zoneId)
                .getResultList();
    }

    @Override
    public Collection<Society> getSocietiesByZone(Integer zoneId) {
        return em.createQuery(
                "SELECT s FROM Society s "
                + "WHERE s.wardId.zoneId.zoneId = :z",
                Society.class)
                .setParameter("z", zoneId)
                .getResultList();
    }

    @Override
    public Collection<Users> getCitizensByZone(Integer zoneId) {
        return em.createQuery(
                "SELECT u FROM Users u "
                + "WHERE u.role = 'CITIZEN' "
                + "AND u.societyId.wardId.zoneId.zoneId = :z",
                Users.class)
                .setParameter("z", zoneId)
                .getResultList();
    }

    @Override
    public Collection<Officers> getOfficersByZone(Integer zoneId) {
        return em.createQuery(
                "SELECT o FROM Officers o "
                + "WHERE o.zoneId.zoneId = :z",
                Officers.class)
                .setParameter("z", zoneId)
                .getResultList();
    }
}
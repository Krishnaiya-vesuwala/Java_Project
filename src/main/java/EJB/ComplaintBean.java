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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    @Override
    public List<Society> decodeQRCode(Integer wardID) {
        System.out.println("-----Called" + wardID);
        return em.createQuery("SELECT s FROM Society s WHERE s.wardId.wardId=:wardID", Society.class)
                .setParameter("wardID", wardID).getResultList();
    }

    @Override
    public void createComplaint(Integer userId,
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
                    "SELECT w.zoneId FROM Ward w WHERE w.wardId = :wid", Zone.class)
                    .setParameter("wid", wardId).getSingleResult();
//            Zone zone = em.find(Zone.class, zoneId);

            if (user == null || category == null || society == null || ward == null || zone == null) {
                throw new Exception("Invalid foreign key while creating complaint");
            }

            SlaRules sla = em.createQuery(
                    "SELECT s FROM SlaRules s WHERE s.categoryId.categoryId=:catId",
                    SlaRules.class
            ).setParameter("catId", categoryId)
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

            Integer generatedId = complaint.getComplaintId();

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

        if (results.isEmpty()) {
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
        }

        long minLoad = (Long) results.get(0)[1];

        List<Officers> leastLoaded = new java.util.ArrayList<>();

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

    @Override
    public List<Object[]> getComplaintByUserId(Integer userId) {

        return em.createQuery("SELECT c.title,o.designation,c.status FROM Complaint c LEFT JOIN c.assignedOfficerId o WHERE c.citizenId.userId = :userId", Object[].class)
                .setParameter("userId", userId)
                .getResultList();
    }

    // Complaint_status_history Functionality
    @Override
    public void createComplaintStatusHistory(int complaintId, String old_status, String new_status, Users changed_by) {
        ComplaintStatusHistory history = new ComplaintStatusHistory();
        Complaint complaint = em.find(Complaint.class, complaintId);
        complaint.getComplaintStatusHistoryCollection().add(history);
        history.setComplaintId(complaint);
        history.setOldStatus(old_status);
        history.setNewStatus(new_status);
        history.setChangedBy(changed_by);
        history.setChangedAt(LocalDateTime.now());

        em.persist(history);
    }

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
    public List<ComplaintStatusHistory> getComplaintStatusHistory(int complaintId) {

        return em.createQuery(
                "SELECT c FROM ComplaintStatusHistory c WHERE c.complaintId.complaintId = :cid ORDER BY c.changedAt DESC",
                ComplaintStatusHistory.class)
                .setParameter("cid", complaintId)
                .getResultList();
    }
    
    @Override
    public Long getTotalComplaintsByUser(Integer userId) {

        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c " +
                "WHERE c.citizenId.userId = :uid",
                Long.class)
                .setParameter("uid", userId)
                .getSingleResult();
    }
    
    @Override
    public Long getAssignedComplaintsByUser(Integer userId) {

        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c " +
                "WHERE c.citizenId.userId = :uid " +
                "AND c.status = 'ASSIGNED'",
                Long.class)
                .setParameter("uid", userId)
                .getSingleResult();
    }

    @Override
    public Long getResolvedComplaintsByUser(Integer userId) {

        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c " +
                "WHERE c.citizenId.userId = :uid " +
                "AND c.status = 'RESOLVED'",
                Long.class)
                .setParameter("uid", userId)
                .getSingleResult();
    }

    @Override
    public Long getRejectedComplaintsByUser(Integer userId) {

        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c " +
                "WHERE c.citizenId.userId = :uid " +
                "AND c.status = 'REJECTED'",
                Long.class)
                .setParameter("uid", userId)
                .getSingleResult();
    }

    @Override
    public List<Complaint> getRecentComplaintsByUser(Integer userId) {

        return em.createQuery(
                "SELECT c FROM Complaint c " +
                "WHERE c.citizenId.userId = :uid " +
                "ORDER BY c.createdAt DESC",
                Complaint.class)
                .setParameter("uid", userId)
                .setMaxResults(5)
                .getResultList();
    }

}

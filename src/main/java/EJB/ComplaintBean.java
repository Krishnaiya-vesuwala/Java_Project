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
    public void createComplaint(Integer userId,
            Integer categoryId,
            Integer societyId,
            Integer wardId,
            Integer zoneId,
            String title,
            String description,
            String status,
            String priority) {

        try {
            Users user = em.find(Users.class, userId);
            ComplaintCategory category = em.find(ComplaintCategory.class, categoryId);
            Society society = em.find(Society.class, societyId);
            Ward ward = em.find(Ward.class, wardId);
            Zone zone = em.find(Zone.class, zoneId);

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

            // Count the due_date
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime dueDate = now.plusHours(sla.getMaxResolutionDays());

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
            complaint.setStatus(status);
            complaint.setPriority(priority);

            complaint.setCreatedAt(java.time.LocalDateTime.now());
            complaint.setDueDate(java.time.LocalDateTime.now().plusDays(3));
            
//        complaint.setc(new java.util.Date());
            em.persist(complaint);           
            em.flush();

            Integer generatedId = complaint.getComplaintId();
            Officers officer = assignToWardOfficer(generatedId);
            
            notifyBean.sendSMS(user.getMobile(),"Complaint Registered Successfully. ID : "+complaint.getComplaintId());
            notifyBean.sendSMS(officer.getUserId().getMobile(), "New Complaint Assigned. ID : "+generatedId);
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public Officers assignToWardOfficer(Integer complaintId) {
        Complaint complaint = em.find(Complaint.class, complaintId);

        if (complaint == null) {
            try {
                throw new Exception("Complaint not found");
            } catch (Exception ex) {
                Logger.getLogger(AdminBean.class.getName()).log(Level.SEVERE, null, ex);
            }
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
            throw new RuntimeException("No ward officers available");
        }

        long minLoad = (Long) results.get(0)[1];

        List<Officers> leastLoaded = new java.util.ArrayList<>();

        for (Object[] r : results) {

            if ((Long) r[1] == minLoad) {
                leastLoaded.add((Officers) r[0]);
            }
        }
       
//        List<Officers> officers = em.createQuery(
//            "SELECT o FROM Officers o WHERE "
//            + "o.wardId = :ward "
//            + "AND o.departmentId = :dept",
//            Officers.class)
//            .setParameter("ward", ward)
//            .setParameter("dept", dept)
//            .getResultList();
//    
//        if (officers.isEmpty()) {
//            try {
//                throw new Exception("No ward Officer available");
//            } catch (Exception ex) {
//                Logger.getLogger(AdminBean.class.getName()).log(Level.SEVERE, null, ex);
//            }

        

        Officers selectedOfficer;
        if (leastLoaded.size() == 1) {
            selectedOfficer = leastLoaded.get(0);
        } else {
            selectedOfficer = roundRobinSelect(leastLoaded);
        }

        complaint.setAssignedOfficerId(selectedOfficer);
        complaint.setStatus("ASSIGNED");

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
    public void createComplaintStatusHistory(Complaint complaint, String old_status, String new_status, Users changed_by) {
        ComplaintStatusHistory history = new ComplaintStatusHistory();
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

        em.persist(reply);
        System.out.println(reply);
    }

}

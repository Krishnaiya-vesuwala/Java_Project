/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Complaint;
import Entity.ComplaintCategory;
import Entity.Departments;
import Entity.Officers;
import Entity.Society;
import Entity.Users;
import Entity.Ward;
import Entity.Zone;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author riya vesuwala
 */
@Stateless
public class ComplaintBean implements ComplaintBeanLocal {
    @PersistenceContext(unitName="jpu1")
    EntityManager em;
    
   @Override
    public void createComplaint(Integer userId,
            Integer categoryId,
            Integer societyId,
            Integer wardId,
            Integer zoneId,
            String title,
            String description,
            String status) {

        try {
            Users user = em.find(Users.class, userId);
            ComplaintCategory category = em.find(ComplaintCategory.class, categoryId);
            Society society = em.find(Society.class, societyId);
            Ward ward = em.find(Ward.class, wardId);
            Zone zone = em.find(Zone.class, zoneId);

            if (user == null || category == null || society == null || ward == null || zone == null) {
                throw new Exception("Invalid foreign key while creating complaint");
            }
            Complaint complaint = new Complaint();
            complaint.setCitizenId(user);
            complaint.setCategoryId(category);
            complaint.setSocietyId(society);
            complaint.setWardId(ward);
            complaint.setZoneId(zone);

            complaint.setTitle(title);
            complaint.setDescription(description);
            complaint.setStatus(status);

//        complaint.setc(new java.util.Date());
            em.persist(complaint);
            em.flush();

            Integer generatedId = complaint.getComplaintId();
            assignToWardOfficer(generatedId);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void assignToWardOfficer(Integer complaintId) {
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

        List<Officers> officers = em.createQuery(
                "SELECT o FROM Officers o WHERE o.designation = :designation "
                + "AND o.wardId = :ward "
                + "AND o.departmentId = :dept ",
                Officers.class)
                .setParameter("designation", "WARD")
                .setParameter("ward", ward)
                .setParameter("dept", dept)
                .setMaxResults(1)
                .getResultList();

        if (officers.isEmpty()) {
            try {
                throw new Exception("No ward Officer available");
            } catch (Exception ex) {
                Logger.getLogger(AdminBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Officers selectedOfficer = officers.get(0);

        complaint.setAssignedOfficerId(selectedOfficer);
        complaint.setStatus("ASSIGNED");

        em.merge(complaint);
    }

    @Override
    public List<Object[]> getComplaintByUserId(Integer userId) {
        
        return em.createQuery("SELECT c.title,o.designation,c.status FROM Complaint c LEFT JOIN c.assignedOfficerId o WHERE c.citizenId.userId = :userId",Object[].class)
                .setParameter("userId", userId)
                .getResultList();
    }
 
}

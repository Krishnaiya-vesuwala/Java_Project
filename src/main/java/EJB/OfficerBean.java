/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Complaint;
import Entity.Officers;
import Entity.Users;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author krishnaiya
 */
@Stateless
public class OfficerBean implements OfficerBeanLocal {
 @PersistenceContext(unitName = "jpu1")
 EntityManager em;
 
 @EJB
 ComplaintBeanLocal complaintBean;
 
 @EJB
 NotificationBeanLocal notifyBean;

    @Override
    public List<Complaint> getAssignedComplaint(int officerId) {
        return em.createQuery("SELECT c FROM Complaint c WHERE c.assignedOfficerId.officerId = :oid",Complaint.class)
              .setParameter("oid",officerId)
              .getResultList();
    }

    @Override
    public void updateComplaintStatus(int complaintId, String status, int logenInUser) {

        Complaint c = em.find(Complaint.class, complaintId);

        if (c != null) {

            String oldStatus = c.getStatus();   // FIXED (typo)
            Users user = em.find(Users.class, logenInUser);

            //  Update status
            c.setStatus(status);

            //  Maintain history (FIX: old → new)
            complaintBean.createComplaintStatusHistory(c.getComplaintId(), oldStatus, status, user);

            // maintain bidirectional relation if exists
            if (c.getComplaintStatusHistoryCollection() != null) {
                // nothing mandatory, since history is new entity
            }

            em.merge(c);

            //  Notification (better: send to complaint owner, not officer)
            if (status.equalsIgnoreCase("RESOLVED") || status.equalsIgnoreCase("SOLVED")) {

                Users citizen = c.getCitizenId();   // correct user

                notifyBean.sendSMS(
                        citizen.getMobile(),
                        "Your Complaint ID: " + c.getComplaintId()
                        + " has been resolved. Thank you for using the system."
                );
            }
        }
    }
    
    @Override
    public Officers getOfficerProfile(int userId) {
        Users user = em.find(Users.class, userId);

    if (user == null) return null;

    return em.createQuery(
            "SELECT o FROM Officers o WHERE o.userId = :user",
            Officers.class)
            .setParameter("user", user)
            .getSingleResult();
    }

    @Override
    public List<Complaint> getComplaintByOfficer(int officerId) {
        Officers officer=em.find(Officers.class,officerId);
        
        if(officer == null){
            return null;
        }
        String designation = officer.getDesignation();
        
        //WARD OFFICER
        if(designation.equalsIgnoreCase("WARD")){
            return em.createQuery("SELECT c FROM Complaint c WHERE c.wardId = :ward",
                    Complaint.class).setParameter("ward",officer.getWardId())
                    .getResultList();
        }
        //ZONE OFFICER
        else if(designation.equalsIgnoreCase("ZONE")){
            return em.createQuery("SELECT c FROM Complaint c WHERE c.zoneId = :zone",
                    Complaint.class)
                    .setParameter("zone", officer.getZoneId())
                    .getResultList();                
        }
        else if(designation.equalsIgnoreCase("CORPORATE")){
            return em.createQuery("SELECT c FROM Complaint c",Complaint.class)
                    .getResultList();
        }
     return null;
    }
}

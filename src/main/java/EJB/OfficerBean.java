/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Complaint;
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

    @Override
    public List<Complaint> getAssignedComplaint(int officerId) {
        return em.createQuery("SELECT c FROM Complaint c WHERE c.assignedOfficerId.officerId = :oid",Complaint.class)
              .setParameter("oid",officerId)
              .getResultList();
    }

    @Override
    public void updateComplaintStatus(int complaintId, String status,int logenInUser) {
        Complaint c=em.find(Complaint.class, complaintId);
        
        
        if(c!=null){
            String odlStatus=c.getStatus();
            
            Users user=em.find(Users.class, logenInUser);

            c.setStatus(status);
            
            complaintBean.createComplaintStatusHistory(c, odlStatus, odlStatus, user);
            
            em.merge(c);
        }
    }
}

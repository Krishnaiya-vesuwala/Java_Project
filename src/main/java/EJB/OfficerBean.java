/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Complaint;
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

    @Override
    public List<Complaint> getAssignedComplaint(int officerId) {
        return em.createQuery("SELECT c FROM Complaint c WHERE c.assignedOfficerId.officerId = :oid",Complaint.class)
              .setParameter("oid",officerId)
              .getResultList();
    }

    @Override
    public void updateComplaintStatus(int complaintId, String status) {
        Complaint c=em.find(Complaint.class, complaintId);
        
        if(c!=null){
            if(status.equalsIgnoreCase("RESOLVED")){
                c.setStatus("CLOSED");
            }else{
                c.setStatus(status);
            }
            em.merge(c);
        }
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Complaint;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author riya vesuwala
 */
@Local
public interface ComplaintBeanLocal {
   
     public void createComplaint(Integer userId,
                            Integer categoryId,
                            Integer societyId,
                            Integer wardId,
                            Integer zoneId,
                            String title,
                            String description,
                            String status);
    
    public void assignToWardOfficer(Integer complaintId);
    public List<Object[]> getComplaintByUserId(Integer userId);
}

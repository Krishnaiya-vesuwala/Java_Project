/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Complaint;
import Entity.ComplaintCategory;
import Entity.ComplaintStatusHistory;
import Entity.Users;
import Entity.Officers;
import Entity.Society;
import jakarta.ejb.Local;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author riya vesuwala
 */
@Local
public interface ComplaintBeanLocal {
    
    public List<Society> decodeQRCode(Integer wardID);
   
     public void createComplaint(Integer userId,
                            Integer categoryId,
                            Integer societyId,
                            Integer wardId,
                            String title,
                            String description,
                            String priority);
    
    public Officers assignToWardOfficer(Integer complaintId);
    public List<Object[]> getComplaintByUserId(Integer userId);
    
    // Complaint_Status_History Functionality
    void createComplaintStatusHistory(int complaint,String old_status,String new_status,Users changed_by);
    
    //complaint reply
    void createComplaintReply(int complaint_id,int replied_by,String message);
    
    List<ComplaintStatusHistory> getComplaintStatusHistory(int complaintId);
    
    Long getTotalComplaintsByUser(Integer userId);

    Long getAssignedComplaintsByUser(Integer userId);

    Long getResolvedComplaintsByUser(Integer userId);

    Long getRejectedComplaintsByUser(Integer userId);

    List<Complaint> getRecentComplaintsByUser(Integer userId);
}

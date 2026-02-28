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
 * @author krishnaiya
 */
@Local
public interface OfficerBeanLocal {
    List<Complaint> getAssignedComplaint(int officerId);
    void updateComplaintStatus(int complaintId,String status);
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Complaint;
import Entity.Officers;
import Entity.Users;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author krishnaiya
 */
@Local
public interface OfficerBeanLocal {
    List<Complaint> getAssignedComplaint(int officerId);
    void updateComplaintStatus(int complaintId,String status,int logedInUser);
    Officers getOfficerProfile(int userId);
    List<Complaint> getComplaintByOfficer(int officerId);
    
    // Krishnaiya
    public void createOfficer(Integer userId,
                          String designation,
                          Integer departmentId,
                          Integer zoneId,
                          Integer wardId,
                          String status);
    public List<Users> getAvailableUsers();
    public List<Officers> getAllOfficers();
    public void removeOfficer(Integer officerId);
    public void transferOfficer(
        Integer officerId,
        Integer zoneId,
        Integer wardId);
    
    public List<Officers> getOfficersByCorporation(Integer corporationId);
    public void addOfficer(Users user, Officers officer);
    public void updateOfficer(Officers officer);
    public Officers findOfficer(Integer officerId);
    public void activateOfficer(Integer officerId);
    public Long getAssignedComplaintCount(
        Integer officerId);
    public void updateOfficerStatus(
        Integer officerId,
        String status);
    public void updateDepartment(
        Integer officerId,
        Integer departmentId);
    public void updateDesignation(
        Integer officerId,
        String designation);
    public void deactivateOfficer(Integer officerId);
    public void revokeOfficerRole(
        Integer officerId);
    public List<Officers> searchOfficers(
        Integer zoneId,
        Integer wardId,
        String designation,
        String status);
    
    public Officers getOfficerById(Integer officerId);
}

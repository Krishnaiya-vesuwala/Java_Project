/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.*;
import jakarta.ejb.Local;
import java.util.List;

/**
 *
 * @author krishnaiya
 */
@Local
public interface AdminBeanLocal {
    //Zone
    public void createZone(String zoneName, String status, Integer corporationId);
    public void updateZone(Integer zoneId,String zoneName,String status,Integer corporationId);
    public void deleteZone(Integer zoneId);
    
    //Department
    public void createDepartment(String departmentName,String description,String status);
    public void updateDepartment(Integer id, String name, String desc, String status);
    public void deleteDepartment(Integer id);
    
    //Society
    public void createSociety(Integer wardId,String societyName,String address,String status);
    public void updateSociety(Integer id, String name, String address, String status, Integer wardId);
    public void deleteSociety(Integer id);
    
    //Complaint Category
    public void createCategory(String categoryName,Integer departmentId);
    public void updateCategory(Integer id, String name, Integer departmentId);
    public void deleteCategory(Integer id);
    
    // Ward Functionalities
    public void createWard(Integer zoneId,String wardName,String status);
    public void updateWard(int wardId,int zoneId,String wardName,String status);
    public void deleteWard(int wardId);
    
    // Officer Functionalities
    public void createOfficer(Integer userId,Integer departmentId,Integer zoneId,Integer wardId,String designation);
    public void updateOfficer(int officerId,int userId,int departmentId,int zoneId,int wardId,String designation);
    public void deleteOfficer(int officerId);
    
    //SLA Functionality
    void addSlaRules(Integer categoryId,Integer maxDays); 
    void deleteSlaRule(Integer slaId);
    void updateSlaRule(Integer SlaId,Integer maxDays);
    List<SlaRules> getAllSlaRules();
}   


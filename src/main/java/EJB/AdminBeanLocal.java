/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.*;
import jakarta.ejb.Local;

/**
 *
 * @author krishnaiya
 */
@Local
public interface AdminBeanLocal {
    //Zone
    public void createZone(Zone zone);
    public void updateZone(Integer zoneId,String zoneName,String status,Integer corporationId);
    public void deleteZone(Integer zoneId);
    
    //Department
    public void createDepartment(Departments dept);
    public void updateDepartment(Integer id, String name, String desc, String status);
    public void deleteDepartment(Integer id);
     
    //Officer
    public void createOfficer(Officers officer);
    public void updateOfficer(Integer id,Integer userId,Integer departmentId,Integer wardId,Integer zoneId,String designation);
    public void deleteOfficer(Integer id);
    
    //Society
    public void createSociety(Society society);
    public void updateSociety(Integer id, String name, String address, String status, Integer wardId);
    public void deleteSociety(Integer id);
    
    //Complaint Category
    public void addCategory(ComplaintCategory category);
    public void updateCategory(Integer id, String name, Integer departmentId);
    public void deleteCategory(Integer id);
    // Ward Functionalities
    public void createWard(int zoneId,String wardName,String status);
    public void updateWard(int wardId,int zoneId,String wardName,String status);
    public void deleteWard(int wardId);
    
    //Officer Functionalities
    void createOfficer(int userId,int departmentId,int zoneId,int wardId,String designation);
    void updateOfficer(int officerId,int userId,int departmentId,int zoneId,int wardId,String designation);
    void deleteOfficer(int officerId);
}

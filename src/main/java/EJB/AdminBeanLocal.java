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
    // Ward Functionalities
    public void createWard(int zoneId,String wardName,String status);
    public void updateWard(int wardId,int zoneId,String wardName,String status);
    public void deleteWard(int wardId);
    
    //Officer Functionalities
    void createOfficer(int userId,int departmentId,int zoneId,int wardId,String designation);
    void updateOfficer(int officerId,int userId,int departmentId,int zoneId,int wardId,String designation);
    void deleteOfficer(int officerId);
}

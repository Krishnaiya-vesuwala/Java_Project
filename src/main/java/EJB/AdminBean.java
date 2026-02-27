/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author krishnaiya
 */
@Stateless
public class AdminBean implements AdminBeanLocal {

    @PersistenceContext(unitName = "jpu1")
    private EntityManager em;
    
    // Ward functionalities
    @Override
    public void createWard(int zoneId,String wardName,String status) {
        
        Zone zone=em.find(Zone.class,zoneId);
        
        if(zone==null){
            try {
                throw new Exception("Zone id not found" + zoneId);
            } catch (Exception ex) {
                Logger.getLogger(AdminBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        Ward ward=new Ward();
        ward.setZoneId(zone);
        ward.setStatus(status);
        ward.setWardName(wardName);
        
        em.persist(ward);
    }
    @Override
    public void updateWard(int wardId,int zoneId,String wardName,String status){
        Ward ward = em.find(Ward.class,wardId);
        if(ward==null){
            try {
                throw new Exception("Ward not Found with id"+ wardId);
            } catch (Exception ex) {
                Logger.getLogger(AdminBean.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        Zone zone = em.find(Zone.class,zoneId);    
        if(zone==null){
            try {
                throw new Exception("Zone not Found with id"+ zoneId);
            } catch (Exception ex) {
                Logger.getLogger(AdminBean.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }
        ward.setZoneId(zone);
        ward.setStatus(status);
        ward.setWardName(wardName);
        
    }

     
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public void deleteWard(int wardId) {
        Ward ward=em.find(Ward.class,wardId);
        
        if(ward!=null){
            em.remove(ward);
        }
    }
    
    // Officer Functionalities

    @Override
    public void createOfficer(int userId, int departmentId, int zoneId,int wardId, String designation) {
        Users user=em.find(Users.class, userId);
        Departments department=em.find(Departments.class, departmentId);
        Zone zone=em.find(Zone.class, zoneId);
        Ward ward=em.find(Ward.class, wardId);
        
        if(user == null || department == null || zone == null){
            try {
                throw new Exception("Invalid Foreign Key while creating officer");
            } catch (Exception ex) {
                Logger.getLogger(AdminBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Officers officer = new Officers();
        officer.setUserId(user);
        officer.setDepartmentId(department);
        officer.setZoneId(zone);
        officer.setWardId(ward);
        officer.setDesignation(designation);
        
        em.persist(officer);
    }

    @Override
    public void updateOfficer(int officerId, int userId, int departmentId, int zoneId, int wardId, String designation) {
        Officers officer = em.find(Officers.class, officerId);
        if (officer == null) {
            throw new RuntimeException("Officer not found with id " + officerId);
        }

        officer.setUserId(em.find(Users.class, userId));
        officer.setDepartmentId(em.find(Departments.class, departmentId));
        officer.setZoneId(em.find(Zone.class, zoneId));
        officer.setWardId(em.find(Ward.class, wardId));
        officer.setDesignation(designation);
    }

    @Override
    public void deleteOfficer(int officerId) {
        Officers officer = em.find(Officers.class, officerId);
        if (officer != null) {
            em.remove(officer);
        }
    }
}

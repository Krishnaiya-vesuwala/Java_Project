/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author krishnaiya
 */
@Stateless
public class AdminBean implements AdminBeanLocal {

    @PersistenceContext(unitName = "jpu1")
    EntityManager em;

    @Override
    public void createZone(Zone zone) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        try {
            em.persist(zone);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateZone(Integer zoneId, String zoneName, String status, Integer corporationId) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        try {
            Zone zone = em.find(Zone.class, zoneId);
            if (zone != null) {
                zone.setZoneName(zoneName);
                zone.setStatus(status);
                Corporation corporation = em.find(Corporation.class, corporationId);
                zone.setCorporationId(corporation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteZone(Integer zoneId) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        try {
            Zone zone = em.find(Zone.class, zoneId);

            if (zone != null) {
                em.remove(zone);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createDepartment(Departments dept) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        try {
            em.persist(dept);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDepartment(Integer id, String name, String desc, String status) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        try {
            Departments dept = em.find(Departments.class, id);
            if (dept != null) {
                dept.setDepartmentName(name);
                dept.setDescription(desc);
                dept.setStatus(status);
                em.merge(dept);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteDepartment(Integer id) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        try {
            Departments dept = em.find(Departments.class, id);
            if (dept != null) {
                em.remove(dept);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createSociety(Society society) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        try {
            em.persist(society);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateSociety(Integer id, String name, String address, String status, Integer wardId) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        try {
            Society soc = em.find(Society.class, id);
            if (soc != null) {

                Ward ward = em.find(Ward.class, wardId);

                soc.setSocietyName(name);
                soc.setAddress(address);
                soc.setStatus(status);
                soc.setWardId(ward);

                em.merge(soc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSociety(Integer id) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        try {
            Society soc = em.find(Society.class, id);
            if (soc != null) {
                em.remove(soc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addCategory(ComplaintCategory category) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        try {
            em.persist(category);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCategory(Integer id, String name, Integer departmentId) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        try {
            ComplaintCategory cat = em.find(ComplaintCategory.class, id);
            if (cat != null) {

                Departments dept = em.find(Departments.class, departmentId);

                cat.setCategoryName(name);
                cat.setDepartmentId(dept);
                em.merge(cat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCategory(Integer id) {
//        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        try {
            ComplaintCategory cat = em.find(ComplaintCategory.class, id);
            if (cat != null) {
                em.remove(cat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PersistenceContext(unitName = "jpu1")

    // Ward functionalities
    @Override
    public void createWard(int zoneId, String wardName, String status) {

        Zone zone = em.find(Zone.class, zoneId);

        if (zone == null) {
            try {
                throw new Exception("Zone id not found" + zoneId);
            } catch (Exception ex) {
                Logger.getLogger(AdminBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        Ward ward = new Ward();
        ward.setZoneId(zone);
        ward.setStatus(status);
        ward.setWardName(wardName);

        em.persist(ward);
    }

    @Override
    public void updateWard(int wardId, int zoneId, String wardName, String status) {
        Ward ward = em.find(Ward.class, wardId);
        if (ward == null) {
            try {
                throw new Exception("Ward not Found with id" + wardId);
            } catch (Exception ex) {
                Logger.getLogger(AdminBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        Zone zone = em.find(Zone.class, zoneId);
        if (zone == null) {
            try {
                throw new Exception("Zone not Found with id" + zoneId);
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
        Ward ward = em.find(Ward.class, wardId);

        if (ward != null) {
            em.remove(ward);
        }
    }

    // Officer Functionalities
    @Override
    public void createOfficer(int userId, int departmentId, int zoneId, int wardId, String designation) {
        Users user = em.find(Users.class, userId);
        Departments department = em.find(Departments.class, departmentId);
        Zone zone = em.find(Zone.class, zoneId);
        Ward ward = em.find(Ward.class, wardId);

        if (user == null || department == null || zone == null) {
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

    @Override
    public void addSlaRules(Integer categoryId, Integer maxDays) {
        ComplaintCategory category=em.find(ComplaintCategory.class,categoryId);
        
        if(category!=null){
            List<SlaRules> existing = em.createQuery(
            "SELECT s FROM SlaRules s WHERE s.categoryId.categoryId=:cid",SlaRules.class
            ).setParameter("cid", categoryId)
                    .getResultList();
            
            if(existing.isEmpty()){
                SlaRules sla=new SlaRules();
                sla.setCategoryId(category);
                sla.setMaxResolutionDays(maxDays);
                em.persist(sla);
            }
        }
    }

    @Override
    public void deleteSlaRule(Integer slaId) {
       SlaRules sla = em.find(SlaRules.class,slaId);
       
       if(sla!=null){
           em.remove(sla);
       }
    }

    @Override
    public void updateSlaRule(Integer slaId, Integer maxDays) {
        SlaRules sla=em.find(SlaRules.class,slaId);
        
        if(sla!=null){
            sla.setMaxResolutionDays(maxDays);
            em.merge(sla);
        }
    }
    @Override
    public List<SlaRules> getAllSlaRules(){
        return em.createQuery("SELECT s FROM SlaRules s",SlaRules.class)
                .getResultList();
    }
  
}

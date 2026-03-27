/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collection;
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
    public void createZone(String zoneName, String status, Integer corporationId) {
        try {
            Zone zone = new Zone();
            zone.setZoneName(zoneName);
            zone.setStatus(status);

            Corporation corp = em.find(Corporation.class, corporationId);

            if (corp != null) {
                zone.setCorporationId(corp);
                corp.getZoneCollection().add(zone); // keep your logic
            }

            em.persist(zone);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void updateZone(Integer zoneId, String zoneName, String status, Integer corporationId) {
        try {
            Corporation corp = em.find(Corporation.class, corporationId);
            Zone zone = em.find(Zone.class, zoneId);

            if (zone != null && corp != null) {

                Collection<Zone> zones = corp.getZoneCollection();

                zones.remove(zone); // remove old relation

                zone.setZoneName(zoneName);
                zone.setStatus(status);
                zone.setCorporationId(corp);

                zones.add(zone); // re-add

                corp.setZoneCollection(zones);

                em.merge(corp);
                em.merge(zone);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteZone(Integer zoneId) {
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
    public void createDepartment(String departmentName,String description,String status) {
        try {
            Departments dept = new Departments();

            dept.setDepartmentName(departmentName);
            dept.setDescription(description);
            dept.setStatus(status);

            em.persist(dept);

            System.out.println("Department Added Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateDepartment(Integer id, String name, String desc, String status) {
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
    public void createSociety(Integer wardId, String societyName, String address, String status) {
        try {
            Society s = new Society();

            s.setSocietyName(societyName);
            s.setAddress(address);
            s.setStatus(status);

            Ward ward = em.find(Ward.class, wardId);

            if (ward != null) {
                s.setWardId(ward);
                ward.getSocietyCollection().add(s); // add relation
            }

            em.persist(s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateSociety(Integer id, String name, String address, String status, Integer wardId) {
        try {
            Society soc = em.find(Society.class, id);
            Ward ward = em.find(Ward.class, wardId);

            if (soc != null && ward != null) {

                Collection<Society> societies = ward.getSocietyCollection();

                societies.remove(soc);

                soc.setSocietyName(name);
                soc.setAddress(address);
                soc.setStatus(status);
                soc.setWardId(ward);

                societies.add(soc);

                ward.setSocietyCollection(societies);

                em.merge(ward);
                em.merge(soc);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSociety(Integer id) {
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
    public void createCategory(String categoryName, Integer departmentId) {
        try {
            ComplaintCategory c = new ComplaintCategory();

            c.setCategoryName(categoryName);

            Departments dept = em.find(Departments.class, departmentId);

            if (dept != null) {
                c.setDepartmentId(dept);
                dept.getComplaintCategoryCollection().add(c);
            }

            em.persist(c);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCategory(Integer id, String name, Integer departmentId) {
    try {
        ComplaintCategory cat = em.find(ComplaintCategory.class, id);
        Departments dept = em.find(Departments.class, departmentId);

        if (cat != null && dept != null) {

            Collection<ComplaintCategory> categories = dept.getComplaintCategoryCollection();

            categories.remove(cat);

            cat.setCategoryName(name);
            cat.setDepartmentId(dept);

            categories.add(cat);

            dept.setComplaintCategoryCollection(categories);

            em.merge(dept);
            em.merge(cat);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    @Override
    public void deleteCategory(Integer id) {
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
    public void createWard(Integer zoneId, String wardName, String status) {
        try {
            Zone zone = em.find(Zone.class, zoneId);

            if (zone != null) {
                Ward ward = new Ward();

                ward.setZoneId(zone);
                ward.setStatus(status);
                ward.setWardName(wardName);

                zone.getWardCollection().add(ward);

                em.persist(ward);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateWard(int wardId, int zoneId, String wardName, String status) {
        try {
            Ward ward = em.find(Ward.class, wardId);
            Zone zone = em.find(Zone.class, zoneId);

            if (ward != null && zone != null) {

                Collection<Ward> wards = zone.getWardCollection();

                wards.remove(ward);

                ward.setZoneId(zone);
                ward.setStatus(status);
                ward.setWardName(wardName);

                wards.add(ward);

                zone.setWardCollection(wards);

                em.merge(zone);
                em.merge(ward);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void createOfficer(Integer userId, Integer departmentId, Integer zoneId, Integer wardId, String designation) {
        try {
            Users user = em.find(Users.class, userId);
            Departments department = em.find(Departments.class, departmentId);
            Zone zone = em.find(Zone.class, zoneId);
            Ward ward = em.find(Ward.class, wardId);

            if (user != null && department != null && zone != null && ward != null) {

                Officers officer = new Officers();

                officer.setUserId(user);
                officer.setDepartmentId(department);
                officer.setZoneId(zone);
                officer.setWardId(ward);
                officer.setDesignation(designation);

                // Maintain bidirectional relationship (like ward)
                zone.getOfficersCollection().add(officer);
                ward.getOfficersCollection().add(officer);

                em.persist(officer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void updateOfficer(int officerId, int userId, int departmentId, int zoneId, int wardId, String designation) {
        try {
            Officers officer = em.find(Officers.class, officerId);

            Users user = em.find(Users.class, userId);
            Departments department = em.find(Departments.class, departmentId);
            Zone zone = em.find(Zone.class, zoneId);
            Ward ward = em.find(Ward.class, wardId);

            if (officer != null && user != null && department != null && zone != null && ward != null) {

                // Remove from old collections
                Zone oldZone = officer.getZoneId();
                Ward oldWard = officer.getWardId();

                if (oldZone != null) {
                    oldZone.getOfficersCollection().remove(officer);
                }

                if (oldWard != null) {
                    oldWard.getOfficersCollection().remove(officer);
                }

                // Set new values
                officer.setUserId(user);
                officer.setDepartmentId(department);
                officer.setZoneId(zone);
                officer.setWardId(ward);
                officer.setDesignation(designation);

                // Add to new collections
                zone.getOfficersCollection().add(officer);
                ward.getOfficersCollection().add(officer);

                // Update parent + child
                em.merge(zone);
                em.merge(ward);
                em.merge(officer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

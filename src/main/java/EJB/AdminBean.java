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

/**
 *
 * @author krishnaiya
 */
// Admin will create zone, department, ward, society, category, officer, sla_rules
@Stateless
public class AdminBean implements AdminBeanLocal {

    @PersistenceContext(unitName = "jpu1")
    EntityManager em;

    // =========================
    // Zone
    // =========================

    @Override
    public void createZone(String zoneName, String status, Integer corporationId) {
        try {
            if (corporationId == null) {
                throw new RuntimeException("Corporation not selected");
            }

            Zone zone = new Zone();
            zone.setZoneName(zoneName);
            zone.setStatus(status);

            Corporation corp = em.find(Corporation.class, corporationId);

            if (corp != null) {
                zone.setCorporationId(corp);
                corp.getZoneCollection().add(zone);
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

                zones.remove(zone);         // remove old relation

                zone.setZoneName(zoneName);
                zone.setStatus(status);
                zone.setCorporationId(corp);

                zones.add(zone);            // re-add updated

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
                // Soft delete — mark as INACTIVE instead of removing
                zone.setStatus("INACTIVE");
                em.merge(zone);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Zone> getAllZones() {
        return em.createQuery(
                "SELECT z FROM Zone z",
                Zone.class)
                .getResultList();
    }

    @Override
    public Zone getZoneById(Integer zoneId) {
        return em.find(Zone.class, zoneId);
    }

    @Override
    public List<Zone> getZonesByCorporation(Integer corporationId) {
        return em.createQuery(
                "SELECT z FROM Zone z "
                + "WHERE z.corporationId.corporationId = :cid",
                Zone.class)
                .setParameter("cid", corporationId)
                .getResultList();
    }

    @Override
    public void activateZone(Integer zoneId) {
        Zone zone = em.find(Zone.class, zoneId);

        if (zone != null) {
            zone.setStatus("ACTIVE");
            em.merge(zone);
        }
    }

    @Override
    public void deactivateZone(Integer zoneId) {
        Zone zone = em.find(Zone.class, zoneId);

        if (zone != null) {
            zone.setStatus("INACTIVE");
            em.merge(zone);
        }
    }

    @Override
    public Collection<Ward> getWardsByZone(Integer zoneId) {
        return em.createQuery(
                "SELECT w FROM Ward w "
                + "WHERE w.zoneId.zoneId = :zoneId "
                + "ORDER BY w.wardName",
                Ward.class)
                .setParameter("zoneId", zoneId)
                .getResultList();
    }

    // =========================
    // Corporation
    // =========================

    @Override
    public List<Corporation> getAllCorporation() {
        return em.createQuery(
                "SELECT c FROM Corporation c",
                Corporation.class)
                .getResultList();
    }

    // =========================
    // Department
    // =========================

    @Override
    public void createDepartment(String departmentName, String description, String status) {
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
    public List<Departments> getAllDepartments() {
        return em.createQuery(
                "SELECT d FROM Departments d",
                Departments.class)
                .getResultList();
    }

    // =========================
    // Society
    // =========================

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
                ward.getSocietyCollection().add(s);
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
    public List<Society> getAllSocities(int wardid) {
        return em.createQuery(
                "SELECT s FROM Society s WHERE s.wardId.wardId = :wid",
                Society.class)
                .setParameter("wid", wardid)
                .getResultList();
    }

    @Override
    public List<Society> getAllSocieties() {
        return em.createQuery(
                "SELECT s FROM Society s",
                Society.class)
                .getResultList();
    }

    // =========================
    // Complaint Category
    // =========================

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

    @Override
    public List<ComplaintCategory> getAllCategory() {
        return em.createNamedQuery(
                "ComplaintCategory.findAll",
                ComplaintCategory.class)
                .getResultList();
    }

    // =========================
    // Ward
    // =========================

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

    @Override
    public void deleteWard(int wardId) {
        Ward ward = em.find(Ward.class, wardId);

        if (ward != null) {
            em.remove(ward);
        }
    }

    @Override
    public List<Ward> getAllWards() {
        return em.createQuery(
                "SELECT w FROM Ward w",
                Ward.class)
                .getResultList();
    }

    // =========================
    // Officer
    // =========================

    @Override
    public void createOfficer(Integer userId, Integer departmentId,
            Integer zoneId, Integer wardId, String designation) {
        try {
            Users user = em.find(Users.class, userId);
            Departments department = em.find(Departments.class, departmentId);
            Zone zone = em.find(Zone.class, zoneId);
            Ward ward = em.find(Ward.class, wardId);

            if (user != null && department != null
                    && zone != null && ward != null) {

                Officers officer = new Officers();

                officer.setUserId(user);
                officer.setDepartmentId(department);
                officer.setZoneId(zone);
                officer.setWardId(ward);
                officer.setDesignation(designation);

                zone.getOfficersCollection().add(officer);
                ward.getOfficersCollection().add(officer);

                em.persist(officer);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateOfficer(int officerId, int userId, int departmentId,
            int zoneId, int wardId, String designation) {
        try {
            Officers officer = em.find(Officers.class, officerId);
            Users user = em.find(Users.class, userId);
            Departments department = em.find(Departments.class, departmentId);
            Zone zone = em.find(Zone.class, zoneId);
            Ward ward = em.find(Ward.class, wardId);

            if (officer != null && user != null && department != null
                    && zone != null && ward != null) {

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

                zone.getOfficersCollection().add(officer);
                ward.getOfficersCollection().add(officer);

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
    public List<Officers> getAllOfficers() {
        return em.createQuery(
                "SELECT o FROM Officers o",
                Officers.class)
                .getResultList();
    }

    @Override
    public void changeOfficerWard(Integer officerId, Integer wardId) {
        try {
            Officers officer = em.find(Officers.class, officerId);
            Ward ward = em.find(Ward.class, wardId);

            if (officer != null && ward != null) {
                // Remove from old ward collection
                Ward oldWard = officer.getWardId();
                if (oldWard != null) {
                    oldWard.getOfficersCollection().remove(officer);
                    em.merge(oldWard);
                }

                officer.setWardId(ward);
                ward.getOfficersCollection().add(officer);

                em.merge(ward);
                em.merge(officer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeOfficerZone(Integer officerId, Integer zoneId) {
        try {
            Officers officer = em.find(Officers.class, officerId);
            Zone zone = em.find(Zone.class, zoneId);

            if (officer != null && zone != null
                    && !zone.getWardCollection().isEmpty()) {

                // Remove from old zone and ward collections
                Zone oldZone = officer.getZoneId();
                Ward oldWard = officer.getWardId();

                if (oldZone != null) {
                    oldZone.getOfficersCollection().remove(officer);
                    em.merge(oldZone);
                }

                if (oldWard != null) {
                    oldWard.getOfficersCollection().remove(officer);
                    em.merge(oldWard);
                }

                // Assign first ward in the new zone as default
                Ward newWard = zone.getWardCollection()
                        .iterator()
                        .next();

                officer.setZoneId(zone);
                officer.setWardId(newWard);

                zone.getOfficersCollection().add(officer);
                newWard.getOfficersCollection().add(officer);

                em.merge(zone);
                em.merge(newWard);
                em.merge(officer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================
    // SLA Rules
    // =========================

    @Override
    public void addSlaRules(Integer categoryId, Integer maxDays, String level) {
        try {
            ComplaintCategory category = em.find(ComplaintCategory.class, categoryId);

            if (category != null) {
                // Check for existing SLA rule for this category
                List<SlaRules> existing = em.createQuery(
                        "SELECT s FROM SlaRules s "
                        + "WHERE s.categoryId.categoryId = :cid",
                        SlaRules.class)
                        .setParameter("cid", categoryId)
                        .getResultList();

                if (existing.isEmpty()) {
                    SlaRules sla = new SlaRules();
                    sla.setCategoryId(category);
                    sla.setMaxResolutionDays(maxDays);
                    sla.setLevel(level);
                    em.persist(sla);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSlaRule(Integer slaId) {
        try {
            SlaRules sla = em.find(SlaRules.class, slaId);

            if (sla != null) {
                em.remove(sla);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateSlaRule(Integer slaId, Integer maxDays, String level) {
        try {
            SlaRules sla = em.find(SlaRules.class, slaId);

            if (sla != null) {
                sla.setMaxResolutionDays(maxDays);
                sla.setLevel(level);
                em.merge(sla);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<SlaRules> getAllSlaRules() {
        return em.createQuery(
                "SELECT s FROM SlaRules s",
                SlaRules.class)
                .getResultList();
    }

    // =========================
    // Complaint Counts
    // =========================

    @Override
    public Long getTotalComplaints() {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c",
                Long.class)
                .getSingleResult();
    }

    @Override
    public Long getOpenComplaints() {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.status = 'OPEN'",
                Long.class)
                .getSingleResult();
    }

    @Override
    public Long getAssignedComplaints() {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.status = 'ASSIGNED'",
                Long.class)
                .getSingleResult();
    }

    @Override
    public Long getInProgressComplaints() {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.status = 'IN_PROGRESS'",
                Long.class)
                .getSingleResult();
    }

    @Override
    public Long getResolvedComplaints() {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.status = 'RESOLVED'",
                Long.class)
                .getSingleResult();
    }

    @Override
    public Long getClosedComplaints() {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.status = 'CLOSED'",
                Long.class)
                .getSingleResult();
    }

    @Override
    public Long getEscalatedComplaints() {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c "
                + "WHERE c.status LIKE 'ESCALATED%'",
                Long.class)
                .getSingleResult();
    }

    // =========================
    // Dashboard Charts
    // =========================

    @Override
    public List<Object[]> getZoneWiseComplaintCount() {
        return em.createQuery(
                "SELECT c.zoneId.zoneName, COUNT(c) "
                + "FROM Complaint c "
                + "GROUP BY c.zoneId.zoneName",
                Object[].class)
                .getResultList();
    }

    @Override
    public List<Object[]> getWardWiseComplaintCount() {
        return em.createQuery(
                "SELECT c.wardId.wardName, COUNT(c) "
                + "FROM Complaint c "
                + "GROUP BY c.wardId.wardName",
                Object[].class)
                .getResultList();
    }

    @Override
    public List<Object[]> getDepartmentWiseComplaintCount() {
        return em.createQuery(
                "SELECT c.categoryId.departmentId.departmentName, COUNT(c) "
                + "FROM Complaint c "
                + "GROUP BY c.categoryId.departmentId.departmentName",
                Object[].class)
                .getResultList();
    }

    @Override
    public List<Object[]> getCategoryWiseComplaintCount() {
        return em.createQuery(
                "SELECT c.categoryId.categoryName, COUNT(c) "
                + "FROM Complaint c "
                + "GROUP BY c.categoryId.categoryName",
                Object[].class)
                .getResultList();
    }
}
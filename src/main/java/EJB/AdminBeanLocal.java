/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.*;
import jakarta.ejb.Local;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author krishnaiya
 */
@Local
public interface AdminBeanLocal {

    // =========================
    // Zone
    // =========================

    public void createZone(String zoneName, String status, Integer corporationId);

    public void updateZone(Integer zoneId, String zoneName, String status, Integer corporationId);

    public void deleteZone(Integer zoneId);

    public List<Zone> getAllZones();

    public Zone getZoneById(Integer zoneId);

    public List<Zone> getZonesByCorporation(Integer corporationId);

    public void activateZone(Integer zoneId);

    public void deactivateZone(Integer zoneId);

    public Collection<Ward> getWardsByZone(Integer zoneId);

    // =========================
    // Corporation
    // =========================

    public List<Corporation> getAllCorporation();

    // =========================
    // Department
    // =========================

    public void createDepartment(String departmentName, String description, String status);

    public void updateDepartment(Integer id, String name, String desc, String status);

    public void deleteDepartment(Integer id);

    public List<Departments> getAllDepartments();

    // =========================
    // Society
    // =========================

    public void createSociety(Integer wardId, String societyName, String address, String status);

    public void updateSociety(Integer id, String name, String address, String status, Integer wardId);

    public void deleteSociety(Integer id);

    public List<Society> getAllSocities(int wardid);

    public List<Society> getAllSocieties();

    // =========================
    // Complaint Category
    // =========================

    public void createCategory(String categoryName, Integer departmentId);

    public void updateCategory(Integer id, String name, Integer departmentId);

    public void deleteCategory(Integer id);

    public List<ComplaintCategory> getAllCategory();

    // =========================
    // Ward
    // =========================

    public void createWard(Integer zoneId, String wardName, String status);

    public void updateWard(int wardId, int zoneId, String wardName, String status);

    public void deleteWard(int wardId);

    public List<Ward> getAllWards();

    // =========================
    // Officer
    // =========================

    public void createOfficer(Integer userId, Integer departmentId, Integer zoneId,
            Integer wardId, String designation);

    public void updateOfficer(int officerId, int userId, int departmentId,
            int zoneId, int wardId, String designation);

    public void deleteOfficer(int officerId);

    public List<Officers> getAllOfficers();

    public void changeOfficerWard(Integer officerId, Integer wardId);

    public void changeOfficerZone(Integer officerId, Integer zoneId);

    // =========================
    // SLA Rules
    // =========================

    void addSlaRules(Integer categoryId, Integer maxDays, String level);

    void deleteSlaRule(Integer slaId);

    void updateSlaRule(Integer slaId, Integer maxDays, String level);

    List<SlaRules> getAllSlaRules();

    // =========================
    // Complaint Counts
    // =========================

    Long getTotalComplaints();

    Long getOpenComplaints();

    Long getAssignedComplaints();

    Long getInProgressComplaints();

    Long getResolvedComplaints();

    Long getClosedComplaints();

    Long getEscalatedComplaints();

    // =========================
    // Dashboard Charts
    // =========================

    List<Object[]> getZoneWiseComplaintCount();

    List<Object[]> getWardWiseComplaintCount();

    List<Object[]> getDepartmentWiseComplaintCount();

    List<Object[]> getCategoryWiseComplaintCount();
}
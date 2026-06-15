package com.mycompany.grievancesystem.resources;

import EJB.AdminBeanLocal;
import EJB.ComplaintBeanLocal;
import EJB.CorporateDashboardService;
import EJB.MasterDataService;
import EJB.OfficerBeanLocal;
import EJB.UserBeanLocal;
import Entity.Complaint;
import Entity.ComplaintCategory;
import Entity.ComplaintEscalation;
import Entity.ComplaintReply;
import Entity.ComplaintStatusHistory;
import Entity.Corporation;
import Entity.Departments;
import Entity.Officers;
import Entity.SlaRules;
import Entity.Society;
import Entity.Users;
import Entity.Ward;
import Entity.Zone;
import JWT.JwtUtil;
import JWT.Secured;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author
 */
@Path("jakartaee10")
public class JakartaEE10Resource {

    @EJB
    AdminBeanLocal adminBean;

    @EJB
    ComplaintBeanLocal complaintBean;

    @EJB
    OfficerBeanLocal officerBean;

    @EJB
    UserBeanLocal userBean;

    @EJB
    MasterDataService masterDataService;

    @EJB
    CorporateDashboardService corporateDashboardService;

    // =========================================================
    // ZONE
    // =========================================================

    @POST
    @Path("createZone/{zoneName}/{status}/{corporationId}")
    @Secured(roles = {"Admin"})
    public void createZone(
            @PathParam("zoneName") String zoneName,
            @PathParam("status") String status,
            @PathParam("corporationId") Integer corporationId) {
        adminBean.createZone(zoneName, status, corporationId);
    }

    @PUT
    @Path("updateZone/{zoneId}/{zoneName}/{status}/{corporationId}")
    @Secured(roles = {"Admin"})
    public void updateZone(
            @PathParam("zoneId") Integer zoneId,
            @PathParam("zoneName") String zoneName,
            @PathParam("status") String status,
            @PathParam("corporationId") Integer corporationId) {
        adminBean.updateZone(zoneId, zoneName, status, corporationId);
    }

    @DELETE
    @Path("deleteZone/{zoneId}")
    @Secured(roles = {"Admin"})
    public void deleteZone(@PathParam("zoneId") Integer zoneId) {
        adminBean.deleteZone(zoneId);
    }

    @GET
    @Path("getAllZones")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Zone> getAllZones() {
        return adminBean.getAllZones();
    }

    @GET
    @Path("getZoneById/{zoneId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Zone getZoneById(@PathParam("zoneId") Integer zoneId) {
        return adminBean.getZoneById(zoneId);
    }

    @GET
    @Path("getZonesByCorporation/{corporationId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Zone> getZonesByCorporation(
            @PathParam("corporationId") Integer corporationId) {
        return adminBean.getZonesByCorporation(corporationId);
    }

    @PUT
    @Path("activateZone/{zoneId}")
    @Secured(roles = {"Admin"})
    public void activateZone(@PathParam("zoneId") Integer zoneId) {
        adminBean.activateZone(zoneId);
    }

    @PUT
    @Path("deactivateZone/{zoneId}")
    @Secured(roles = {"Admin"})
    public void deactivateZone(@PathParam("zoneId") Integer zoneId) {
        adminBean.deactivateZone(zoneId);
    }

    @GET
    @Path("getWardsByZone/{zoneId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Collection<Ward> getWardsByZone(
            @PathParam("zoneId") Integer zoneId) {
        return adminBean.getWardsByZone(zoneId);
    }

    // =========================================================
    // CORPORATION
    // =========================================================

    @GET
    @Path("getAllCorporations")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Corporation> getAllCorporations() {
        return adminBean.getAllCorporation();
    }

    // =========================================================
    // DEPARTMENT
    // =========================================================

    @POST
    @Path("createDepartment/{departmentName}/{description}/{status}")
    @Secured(roles = {"Admin"})
    public void createDepartment(
            @PathParam("departmentName") String departmentName,
            @PathParam("description") String description,
            @PathParam("status") String status) {
        adminBean.createDepartment(departmentName, description, status);
    }

    @PUT
    @Path("updateDepartment/{id}/{name}/{desc}/{status}")
    @Secured(roles = {"Admin"})
    public void updateDepartment(
            @PathParam("id") Integer id,
            @PathParam("name") String name,
            @PathParam("desc") String desc,
            @PathParam("status") String status) {
        adminBean.updateDepartment(id, name, desc, status);
    }

    @DELETE
    @Path("deleteDepartment/{id}")
    @Secured(roles = {"Admin"})
    public void deleteDepartment(@PathParam("id") Integer id) {
        adminBean.deleteDepartment(id);
    }

    @GET
    @Path("getAllDepartments")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Departments> getAllDepartments() {
        return adminBean.getAllDepartments();
    }

    // =========================================================
    // SOCIETY
    // =========================================================

    @POST
    @Path("createSociety/{wardId}/{societyName}/{address}/{status}")
    @Secured(roles = {"Admin"})
    public void createSociety(
            @PathParam("wardId") Integer wardId,
            @PathParam("societyName") String societyName,
            @PathParam("address") String address,
            @PathParam("status") String status) {
        adminBean.createSociety(wardId, societyName, address, status);
    }

    @PUT
    @Path("updateSociety/{id}/{name}/{address}/{status}/{wardId}")
    @Secured(roles = {"Admin"})
    public void updateSociety(
            @PathParam("id") Integer id,
            @PathParam("name") String name,
            @PathParam("address") String address,
            @PathParam("status") String status,
            @PathParam("wardId") Integer wardId) {
        adminBean.updateSociety(id, name, address, status, wardId);
    }

    @DELETE
    @Path("deleteSociety/{id}")
    @Secured(roles = {"Admin"})
    public void deleteSociety(@PathParam("id") Integer id) {
        adminBean.deleteSociety(id);
    }

    @GET
    @Path("getAllSocieties")
    @Produces("application/json")
    public List<Society> getAllSocieties() {
        return adminBean.getAllSocieties();
    }

    @GET
    @Path("getAllSocietiesByWard/{wardId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Society> getAllSocietiesByWard(
            @PathParam("wardId") int wardId) {
        return adminBean.getAllSocities(wardId);
    }

    // =========================================================
    // COMPLAINT CATEGORY
    // =========================================================

    @POST
    @Path("createCategory/{categoryName}/{departmentId}")
    @Secured(roles = {"Admin"})
    public void createCategory(
            @PathParam("categoryName") String categoryName,
            @PathParam("departmentId") Integer departmentId) {
        adminBean.createCategory(categoryName, departmentId);
    }

    @PUT
    @Path("updateCategory/{id}/{name}/{departmentId}")
    @Secured(roles = {"Admin"})
    public void updateCategory(
            @PathParam("id") Integer id,
            @PathParam("name") String name,
            @PathParam("departmentId") Integer departmentId) {
        adminBean.updateCategory(id, name, departmentId);
    }

    @DELETE
    @Path("deleteCategory/{id}")
    @Secured(roles = {"Admin"})
    public void deleteCategory(@PathParam("id") Integer id) {
        adminBean.deleteCategory(id);
    }

    @GET
    @Path("getAllCategories")
    @Produces("application/json")
    @Secured(roles = {"Admin", "Officer", "Citizen"})
    public List<ComplaintCategory> getAllCategories() {
        return adminBean.getAllCategory();
    }

    // =========================================================
    // WARD
    // =========================================================

    @POST
    @Path("createWard/{zoneId}/{wardName}/{status}")
    @Secured(roles = {"Admin"})
    public void createWard(
            @PathParam("zoneId") Integer zoneId,
            @PathParam("wardName") String wardName,
            @PathParam("status") String status) {
        adminBean.createWard(zoneId, wardName, status);
    }

    @PUT
    @Path("updateWard/{wardId}/{zoneId}/{wardName}/{status}")
    @Secured(roles = {"Admin"})
    public void updateWard(
            @PathParam("wardId") int wardId,
            @PathParam("zoneId") int zoneId,
            @PathParam("wardName") String wardName,
            @PathParam("status") String status) {
        adminBean.updateWard(wardId, zoneId, wardName, status);
    }

    @DELETE
    @Path("deleteWard/{wardId}")
    @Secured(roles = {"Admin"})
    public void deleteWard(@PathParam("wardId") int wardId) {
        adminBean.deleteWard(wardId);
    }

    @GET
    @Path("getAllWards")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Ward> getAllWards() {
        return adminBean.getAllWards();
    }

    // =========================================================
    // OFFICER (Admin)
    // =========================================================

    @POST
    @Path("createOfficer/{userId}/{departmentId}/{zoneId}/{wardId}/{designation}")
    @Secured(roles = {"Admin"})
    public void createOfficer(
            @PathParam("userId") Integer userId,
            @PathParam("departmentId") Integer departmentId,
            @PathParam("zoneId") Integer zoneId,
            @PathParam("wardId") Integer wardId,
            @PathParam("designation") String designation) {
        adminBean.createOfficer(userId, departmentId, zoneId, wardId, designation);
    }

    @PUT
    @Path("updateOfficer/{officerId}/{userId}/{departmentId}/{zoneId}/{wardId}/{designation}")
    @Secured(roles = {"Admin"})
    public void updateOfficer(
            @PathParam("officerId") int officerId,
            @PathParam("userId") int userId,
            @PathParam("departmentId") int departmentId,
            @PathParam("zoneId") int zoneId,
            @PathParam("wardId") int wardId,
            @PathParam("designation") String designation) {
        adminBean.updateOfficer(officerId, userId, departmentId, zoneId, wardId, designation);
    }

    @DELETE
    @Path("deleteOfficer/{officerId}")
    @Secured(roles = {"Admin"})
    public void deleteOfficer(@PathParam("officerId") int officerId) {
        adminBean.deleteOfficer(officerId);
    }

    @GET
    @Path("getAllOfficers")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Officers> getAllOfficers() {
        return adminBean.getAllOfficers();
    }

    @PUT
    @Path("changeOfficerWard/{officerId}/{wardId}")
    @Secured(roles = {"Admin"})
    public void changeOfficerWard(
            @PathParam("officerId") Integer officerId,
            @PathParam("wardId") Integer wardId) {
        adminBean.changeOfficerWard(officerId, wardId);
    }

    @PUT
    @Path("changeOfficerZone/{officerId}/{zoneId}")
    @Secured(roles = {"Admin"})
    public void changeOfficerZone(
            @PathParam("officerId") Integer officerId,
            @PathParam("zoneId") Integer zoneId) {
        adminBean.changeOfficerZone(officerId, zoneId);
    }

    // =========================================================
    // SLA RULES
    // =========================================================

    @POST
    @Path("addSlaRule/{categoryId}/{maxDays}/{status}")
    @Secured(roles = {"Admin"})
    public void addSlaRule(
            @PathParam("categoryId") Integer categoryId,
            @PathParam("maxDays") Integer maxDays,
            @PathParam("status") String status) {
        adminBean.addSlaRules(categoryId, maxDays, status);
    }

    @PUT
    @Path("updateSlaRule/{slaId}/{maxDays}/{status}")
    @Secured(roles = {"Admin"})
    public void updateSlaRule(
            @PathParam("slaId") Integer slaId,
            @PathParam("maxDays") Integer maxDays,
            @PathParam("status") String status) {
        adminBean.updateSlaRule(slaId, maxDays, status);
    }

    @DELETE
    @Path("deleteSlaRule/{slaId}")
    @Secured(roles = {"Admin"})
    public void deleteSlaRule(@PathParam("slaId") Integer slaId) {
        adminBean.deleteSlaRule(slaId);
    }

    @GET
    @Path("getAllSlaRules")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<SlaRules> getAllSlaRules() {
        return adminBean.getAllSlaRules();
    }

    // =========================================================
    // ADMIN COMPLAINT COUNTS
    // =========================================================

    @GET
    @Path("admin/totalComplaints")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long adminTotalComplaints() {
        return adminBean.getTotalComplaints();
    }

    @GET
    @Path("admin/openComplaints")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long adminOpenComplaints() {
        return adminBean.getOpenComplaints();
    }

    @GET
    @Path("admin/assignedComplaints")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long adminAssignedComplaints() {
        return adminBean.getAssignedComplaints();
    }

    @GET
    @Path("admin/inProgressComplaints")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long adminInProgressComplaints() {
        return adminBean.getInProgressComplaints();
    }

    @GET
    @Path("admin/resolvedComplaints")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long adminResolvedComplaints() {
        return adminBean.getResolvedComplaints();
    }

    @GET
    @Path("admin/closedComplaints")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long adminClosedComplaints() {
        return adminBean.getClosedComplaints();
    }

    @GET
    @Path("admin/escalatedComplaints")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long adminEscalatedComplaints() {
        return adminBean.getEscalatedComplaints();
    }

    // =========================================================
    // ADMIN DASHBOARD CHARTS
    // =========================================================

    @GET
    @Path("admin/zoneWiseComplaintCount")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Object[]> zoneWiseComplaintCount() {
        return adminBean.getZoneWiseComplaintCount();
    }

    @GET
    @Path("admin/wardWiseComplaintCount")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Object[]> wardWiseComplaintCount() {
        return adminBean.getWardWiseComplaintCount();
    }

    @GET
    @Path("admin/departmentWiseComplaintCount")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Object[]> departmentWiseComplaintCount() {
        return adminBean.getDepartmentWiseComplaintCount();
    }

    @GET
    @Path("admin/categoryWiseComplaintCount")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Object[]> categoryWiseComplaintCount() {
        return adminBean.getCategoryWiseComplaintCount();
    }

    // =========================================================
    // COMPLAINT (Citizen)
    // =========================================================

    @GET
    @Path("decodeQRCode/{wardID}")
    @Produces("application/json")
    @Secured(roles = {"Citizen"})
    public List<Society> decodeQRCode(
            @PathParam("wardID") Integer wardID) {
        return complaintBean.decodeQRCode(wardID);
    }

    @POST
    @Path("createComplaint/{userId}/{categoryId}/{societyId}/{wardId}/{title}/{description}/{status}/{priority}")
    @Secured(roles = {"Citizen"})
    public Response createComplaint(
            @PathParam("userId") Integer userId,
            @PathParam("categoryId") Integer categoryId,
            @PathParam("societyId") Integer societyId,
            @PathParam("wardId") Integer wardId,
            @PathParam("title") String title,
            @PathParam("description") String description,
            @PathParam("status") String status,
            @PathParam("priority") String priority) {

        try {
            complaintBean.createComplaint(
                    userId, categoryId, societyId,
                    wardId, title, description, priority);
            return Response.ok("Complaint Created").build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("getComplaintByUser/{userId}")
    @Produces("application/json")
    @Secured(roles = {"Citizen"})
    public List<Object[]> getComplaintByUser(
            @PathParam("userId") Integer userId) {
        System.out.println("GET COMPLAINT BY USER CALLED");
        return complaintBean.getComplaintByUserId(userId);
    }

    @GET
    @Path("getAllComplaints")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Complaint> getAllComplaints() {
        return complaintBean.getAllComplaints();
    }

    @GET
    @Path("getPendingComplaints")
    @Produces("application/json")
    @Secured(roles = {"Admin", "Officer"})
    public List<Complaint> getPendingComplaints() {
        return complaintBean.getPendingComplaints();
    }

    @POST
    @Path("createComplaintReply/{complaintId}/{repliedBy}/{message}")
    @Secured(roles = {"Citizen"})
    public void createComplaintReply(
            @PathParam("complaintId") int complaintId,
            @PathParam("repliedBy") int repliedBy,
            @PathParam("message") String message) {
        complaintBean.createComplaintReply(complaintId, repliedBy, message);
    }

    @GET
    @Path("complaintReplies/{complaintId}")
    @Produces("application/json")
    @Secured(roles = {"Citizen", "Officer", "Admin"})
    public List<ComplaintReply> getComplaintReplies(
            @PathParam("complaintId") int complaintId) {
        return complaintBean.getComplaintReplies(complaintId);
    }

    @GET
    @Path("complaintHistory/{complaintId}")
    @Produces("application/json")
    @Secured(roles = {"Citizen"})
    public List<ComplaintStatusHistory> getHistory(
            @PathParam("complaintId") int complaintId) {
        return complaintBean.getComplaintStatusHistory(complaintId);
    }

    @GET
    @Path("citizenNotifications/{userId}")
    @Secured(roles = {"Citizen"})
    public Response citizenNotifications(
            @PathParam("userId") Integer userId) {
        List<Object[]> notifications = complaintBean.getCitizenNotifications(userId);
        return Response.ok(notifications).build();
    }

    @GET
    @Path("totalComplaints/{userId}")
    @Secured(roles = {"Citizen"})
    @Produces("application/json")
    public Long totalComplaints(@PathParam("userId") Integer userId) {
        return complaintBean.getTotalComplaintsByUser(userId);
    }

    @GET
    @Path("assignedComplaints/{userId}")
    @Secured(roles = {"Citizen"})
    @Produces("application/json")
    public Long assignedComplaints(@PathParam("userId") Integer userId) {
        return complaintBean.getAssignedComplaintsByUser(userId);
    }

    @GET
    @Path("resolvedComplaints/{userId}")
    @Secured(roles = {"Citizen"})
    @Produces("application/json")
    public Long resolvedComplaints(@PathParam("userId") Integer userId) {
        return complaintBean.getResolvedComplaintsByUser(userId);
    }

    @GET
    @Path("rejectedComplaints/{userId}")
    @Secured(roles = {"Citizen"})
    @Produces("application/json")
    public Long rejectedComplaints(@PathParam("userId") Integer userId) {
        return complaintBean.getRejectedComplaintsByUser(userId);
    }

    @GET
    @Path("recentComplaints/{userId}")
    @Secured(roles = {"Citizen"})
    @Produces("application/json")
    public List<Complaint> recentComplaints(@PathParam("userId") Integer userId) {
        return complaintBean.getRecentComplaintsByUser(userId);
    }

    // =========================================================
    // WARD ADMIN DASHBOARD (ComplaintBean)
    // =========================================================

    @GET
    @Path("ward/complaints/{wardId}")
    @Produces("application/json")
    @Secured(roles = {"Admin", "Officer"})
    public Collection<Complaint> getComplaintsByWard(
            @PathParam("wardId") Integer wardId) {
        return complaintBean.getComplaintsByWard(wardId);
    }

    @GET
    @Path("ward/totalComplaints/{wardId}")
    @Produces("application/json")
    @Secured(roles = {"Admin", "Officer"})
    public Long wardTotalComplaints(@PathParam("wardId") Integer wardId) {
        return complaintBean.totalComplaints(wardId);
    }

    @GET
    @Path("ward/pendingComplaints/{wardId}")
    @Produces("application/json")
    @Secured(roles = {"Admin", "Officer"})
    public Long wardPendingComplaints(@PathParam("wardId") Integer wardId) {
        return complaintBean.pendingComplaints(wardId);
    }

    @GET
    @Path("ward/resolvedComplaints/{wardId}")
    @Produces("application/json")
    @Secured(roles = {"Admin", "Officer"})
    public Long wardResolvedComplaints(@PathParam("wardId") Integer wardId) {
        return complaintBean.resolvedComplaints(wardId);
    }

    @GET
    @Path("ward/rejectedComplaints/{wardId}")
    @Produces("application/json")
    @Secured(roles = {"Admin", "Officer"})
    public Long wardRejectedComplaints(@PathParam("wardId") Integer wardId) {
        return complaintBean.rejectedComplaints(wardId);
    }

    @GET
    @Path("ward/societies/{wardId}")
    @Produces("application/json")
    @Secured(roles = {"Admin", "Officer"})
    public Collection<Society> getSocietiesByWard(
            @PathParam("wardId") Integer wardId) {
        return complaintBean.getSocietiesByWard(wardId);
    }

    @GET
    @Path("ward/citizens/{wardId}")
    @Produces("application/json")
    @Secured(roles = {"Admin", "Officer"})
    public Collection<Users> getCitizensByWard(
            @PathParam("wardId") Integer wardId) {
        return complaintBean.getCitizensByWard(wardId);
    }

    @GET
    @Path("ward/officers/{wardId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Collection<Officers> getOfficersByWard(
            @PathParam("wardId") Integer wardId) {
        return complaintBean.getOfficersByWard(wardId);
    }

    @GET
    @Path("ward/wardOfficers/{wardId}")
    @Produces("application/json")
    @Secured(roles = {"Admin", "Officer"})
    public Collection<Officers> getWardOfficers(
            @PathParam("wardId") Integer wardId) {
        return complaintBean.getWardOfficers(wardId);
    }

    // =========================================================
    // ZONE ADMIN DASHBOARD (ComplaintBean)
    // =========================================================

    @GET
    @Path("zone/totalComplaints/{zoneId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long zoneTotalComplaints(@PathParam("zoneId") Integer zoneId) {
        return complaintBean.totalComplaintsByZone(zoneId);
    }

    @GET
    @Path("zone/pendingComplaints/{zoneId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long zonePendingComplaints(@PathParam("zoneId") Integer zoneId) {
        return complaintBean.pendingComplaintsByZone(zoneId);
    }

    @GET
    @Path("zone/resolvedComplaints/{zoneId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long zoneResolvedComplaints(@PathParam("zoneId") Integer zoneId) {
        return complaintBean.resolvedComplaintsByZone(zoneId);
    }

    @GET
    @Path("zone/rejectedComplaints/{zoneId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long zoneRejectedComplaints(@PathParam("zoneId") Integer zoneId) {
        return complaintBean.rejectedComplaintsByZone(zoneId);
    }

    @GET
    @Path("zone/complaints/{zoneId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Collection<Complaint> getComplaintsByZone(
            @PathParam("zoneId") Integer zoneId) {
        return complaintBean.getComplaintsByZone(zoneId);
    }

    @GET
    @Path("zone/wards/{zoneId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Collection<Ward> getWardsByZoneComplaint(
            @PathParam("zoneId") Integer zoneId) {
        return complaintBean.getWardsByZone(zoneId);
    }

    @GET
    @Path("zone/societies/{zoneId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Collection<Society> getSocietiesByZone(
            @PathParam("zoneId") Integer zoneId) {
        return complaintBean.getSocietiesByZone(zoneId);
    }

    @GET
    @Path("zone/citizens/{zoneId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Collection<Users> getCitizensByZone(
            @PathParam("zoneId") Integer zoneId) {
        return complaintBean.getCitizensByZone(zoneId);
    }

    @GET
    @Path("zone/officers/{zoneId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Collection<Officers> getOfficersByZone(
            @PathParam("zoneId") Integer zoneId) {
        return complaintBean.getOfficersByZone(zoneId);
    }

    // =========================================================
    // OFFICER BEAN
    // =========================================================

    @GET
    @Path("getAssignedComplaint/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public List<Complaint> getAssignedComplaint(
            @PathParam("officerId") int officerId) {
        return officerBean.getAssignedComplaint(officerId);
    }

    @PUT
    @Path("updateComplaintStatus/{complaintId}/{status}/{loggedInUser}")
    @Secured(roles = {"Officer"})
    public void updateComplaintStatus(
            @PathParam("complaintId") int complaintId,
            @PathParam("status") String status,
            @PathParam("loggedInUser") int loggedInUser) {
        officerBean.updateComplaintStatus(complaintId, status, loggedInUser);
    }

    @GET
    @Path("getOfficerProfile/{userId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public Officers getOfficerProfile(@PathParam("userId") int userId) {
        return officerBean.getOfficerProfile(userId);
    }

    @GET
    @Path("getComplaintByOfficer/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public List<Complaint> getComplaintByOfficer(
            @PathParam("officerId") int officerId) {
        return officerBean.getComplaintByOfficer(officerId);
    }

    @POST
    @Path("officer/create/{userId}/{designation}/{departmentId}/{zoneId}/{wardId}/{status}")
    @Secured(roles = {"Admin"})
    public void officerCreate(
            @PathParam("userId") Integer userId,
            @PathParam("designation") String designation,
            @PathParam("departmentId") Integer departmentId,
            @PathParam("zoneId") Integer zoneId,
            @PathParam("wardId") Integer wardId,
            @PathParam("status") String status) {
        officerBean.createOfficer(userId, designation, departmentId, zoneId, wardId, status);
    }

    @GET
    @Path("officer/availableUsers")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Users> getAvailableUsers() {
        return officerBean.getAvailableUsers();
    }

    @GET
    @Path("officer/all")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Officers> officerGetAll() {
        return officerBean.getAllOfficers();
    }

    @DELETE
    @Path("officer/remove/{officerId}")
    @Secured(roles = {"Admin"})
    public void removeOfficer(@PathParam("officerId") Integer officerId) {
        officerBean.removeOfficer(officerId);
    }

    @PUT
    @Path("officer/transfer/{officerId}/{zoneId}/{wardId}")
    @Secured(roles = {"Admin"})
    public void transferOfficer(
            @PathParam("officerId") Integer officerId,
            @PathParam("zoneId") Integer zoneId,
            @PathParam("wardId") Integer wardId) {
        officerBean.transferOfficer(officerId, zoneId, wardId);
    }

    @GET
    @Path("officer/byCorporation/{corporationId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Officers> getOfficersByCorporation(
            @PathParam("corporationId") Integer corporationId) {
        return officerBean.getOfficersByCorporation(corporationId);
    }

    @PUT
    @Path("officer/activate/{officerId}")
    @Secured(roles = {"Admin"})
    public void activateOfficer(@PathParam("officerId") Integer officerId) {
        officerBean.activateOfficer(officerId);
    }

    @PUT
    @Path("officer/deactivate/{officerId}")
    @Secured(roles = {"Admin"})
    public void deactivateOfficer(@PathParam("officerId") Integer officerId) {
        officerBean.deactivateOfficer(officerId);
    }

    @PUT
    @Path("officer/revokeRole/{officerId}")
    @Secured(roles = {"Admin"})
    public void revokeOfficerRole(@PathParam("officerId") Integer officerId) {
        officerBean.revokeOfficerRole(officerId);
    }

    @PUT
    @Path("officer/updateStatus/{officerId}/{status}")
    @Secured(roles = {"Admin"})
    public void updateOfficerStatus(
            @PathParam("officerId") Integer officerId,
            @PathParam("status") String status) {
        officerBean.updateOfficerStatus(officerId, status);
    }

    @PUT
    @Path("officer/updateDepartment/{officerId}/{departmentId}")
    @Secured(roles = {"Admin"})
    public void updateOfficerDepartment(
            @PathParam("officerId") Integer officerId,
            @PathParam("departmentId") Integer departmentId) {
        officerBean.updateDepartment(officerId, departmentId);
    }

    @PUT
    @Path("officer/updateDesignation/{officerId}/{designation}")
    @Secured(roles = {"Admin"})
    public void updateOfficerDesignation(
            @PathParam("officerId") Integer officerId,
            @PathParam("designation") String designation) {
        officerBean.updateDesignation(officerId, designation);
    }

    @GET
    @Path("officer/assignedCount/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Admin", "Officer"})
    public Long getAssignedComplaintCount(
            @PathParam("officerId") Integer officerId) {
        return officerBean.getAssignedComplaintCount(officerId);
    }

    @GET
    @Path("officer/find/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Officers findOfficer(@PathParam("officerId") Integer officerId) {
        return officerBean.findOfficer(officerId);
    }

    @GET
    @Path("officer/getById/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Admin", "Officer"})
    public Officers getOfficerById(@PathParam("officerId") Integer officerId) {
        return officerBean.getOfficerById(officerId);
    }

    @GET
    @Path("officer/search")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Officers> searchOfficers(
            @QueryParam("zoneId") Integer zoneId,
            @QueryParam("wardId") Integer wardId,
            @QueryParam("designation") String designation,
            @QueryParam("status") String status) {
        return officerBean.searchOfficers(zoneId, wardId, designation, status);
    }

    // =========================================================
    // USER
    // =========================================================

    @POST
    @Path("login")
    @Consumes("application/json")
    @Produces("application/json")
    public Response login(Users requestUser) {
        Users user = userBean.login(
                requestUser.getUsername(),
                requestUser.getPassword());

        if (user != null) {
            String token = JwtUtil.generateToken(
                    user.getUsername(),
                    user.getRole());

            return Response.ok(user)
                    .header("Authorization", token)
                    .build();
        }

        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("Invalid username or password")
                .build();
    }

    @POST
    @Path("registerUser/{fullname}/{email}/{mobile}/{username}/{password}/{societyId}")
    public Response registerUser(
            @PathParam("fullname") String fullname,
            @PathParam("email") String email,
            @PathParam("mobile") String mobile,
            @PathParam("username") String username,
            @PathParam("password") String password,
            @PathParam("societyId") Integer societyId) {

        try {
            userBean.registerUser(fullname, email, mobile,
                    username, password, societyId);
            return Response.ok("Registration Successful").build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(e.getMessage())
                    .build();
        }
    }

    @GET
    @Path("getUserById/{userId}")
    @Produces("application/json")
    @Secured(roles = {"Admin", "Officer", "Citizen"})
    public Users getUserById(@PathParam("userId") int userId) {
        return userBean.getUserById(userId);
    }

    @PUT
    @Path("updateUser/{userId}/{fullName}/{email}/{mobile}/{username}")
    @Secured(roles = {"Admin", "Officer", "Citizen"})
    public Response updateUser(
            @PathParam("userId") Integer userId,
            @PathParam("fullName") String fullName,
            @PathParam("email") String email,
            @PathParam("mobile") String mobile,
            @PathParam("username") String username) {

        userBean.updateUser(userId, fullName, email, mobile, username);
        return Response.ok().build();
    }

    @PUT
    @Path("updateUserFull/{userId}/{fullName}/{email}/{mobile}/{username}/{role}/{status}/{societyId}")
    @Secured(roles = {"Admin"})
    public Response updateUserFull(
            @PathParam("userId") Integer userId,
            @PathParam("fullName") String fullName,
            @PathParam("email") String email,
            @PathParam("mobile") String mobile,
            @PathParam("username") String username,
            @PathParam("role") String role,
            @PathParam("status") String status,
            @PathParam("societyId") Integer societyId) {

        userBean.updateUser(userId, fullName, email, mobile,
                username, role, status, societyId);
        return Response.ok().build();
    }

    @POST
    @Path("forgotPassword/{username}")
    @Produces("application/json")
    public Users forgotPassword(@PathParam("username") String username) {
        return userBean.forgotPassword(username);
    }

    @PUT
    @Path("resetPassword/{userId}/{newPassword}")
    @Secured(roles = {"Admin", "Officer", "Citizen"})
    public void resetPassword(
            @PathParam("userId") int userId,
            @PathParam("newPassword") String newPassword) {
        userBean.resetPassword(userId, newPassword);
    }

    @POST
    @Path("submitFeedback/{complaintId}/{rating}/{comments}")
    @Secured(roles = {"Citizen"})
    public void submitFeedback(
            @PathParam("complaintId") int complaintId,
            @PathParam("rating") String rating,
            @PathParam("comments") String comments) {
        userBean.submitFeedback(complaintId, rating, comments);
    }

    @GET
    @Path("getAllUsers")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Users> getAllUsers() {
        return userBean.getAllUsers();
    }

    @POST
    @Path("createUser")
    @Consumes("application/json")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Users createUser(Users user) {
        return userBean.createUser(user);
    }

    @GET
    @Path("getAdminProfile/{userId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Users getAdminProfile(@PathParam("userId") Integer userId) {
        return userBean.getAdminProfile(userId);
    }

    // =========================================================
    // MASTER DATA SERVICE
    // =========================================================

    @GET
    @Path("masterdata/zones/{corporationId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Zone> getMasterZones(
            @PathParam("corporationId") Integer corporationId) {
        return masterDataService.getZones(corporationId);
    }

    @GET
    @Path("masterdata/wards/{corporationId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Ward> getMasterWards(
            @PathParam("corporationId") Integer corporationId) {
        return masterDataService.getWards(corporationId);
    }

    @GET
    @Path("masterdata/categories")
    @Produces("application/json")
    @Secured(roles = {"Admin", "Officer", "Citizen"})
    public List<ComplaintCategory> getMasterCategories() {
        return masterDataService.getCategories();
    }

    @GET
    @Path("masterdata/departments")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Departments> getMasterDepartments() {
        return masterDataService.getAllDepartments();
    }

    // =========================================================
    // CORPORATE DASHBOARD SERVICE
    // =========================================================

    @GET
    @Path("corporate/totalComplaints/{corporationId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long corporateTotalComplaints(
            @PathParam("corporationId") Integer corporationId) {
        return corporateDashboardService.getTotalComplaints(corporationId);
    }

    @GET
    @Path("corporate/openComplaints/{corporationId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long corporateOpenComplaints(
            @PathParam("corporationId") Integer corporationId) {
        return corporateDashboardService.getOpenComplaints(corporationId);
    }

    @GET
    @Path("corporate/assignedComplaints/{corporationId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long corporateAssignedComplaints(
            @PathParam("corporationId") Integer corporationId) {
        return corporateDashboardService.getAssignedComplaints(corporationId);
    }

    @GET
    @Path("corporate/resolvedComplaints/{corporationId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long corporateResolvedComplaints(
            @PathParam("corporationId") Integer corporationId) {
        return corporateDashboardService.getResolvedComplaints(corporationId);
    }

    @GET
    @Path("corporate/escalatedComplaints/{corporationId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Long corporateEscalatedComplaints(
            @PathParam("corporationId") Integer corporationId) {
        return corporateDashboardService.getEscalatedComplaints(corporationId);
    }

    @GET
    @Path("corporate/filterComplaints/{corporationId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<Complaint> corporateFilterComplaints(
            @PathParam("corporationId") Integer corporationId,
            @QueryParam("zoneId") Integer zoneId,
            @QueryParam("wardId") Integer wardId,
            @QueryParam("categoryId") Integer categoryId,
            @QueryParam("status") String status) {
        return corporateDashboardService.filterComplaints(
                corporationId, zoneId, wardId, categoryId, status);
    }

    @GET
    @Path("corporate/complaintDetails/{complaintId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Complaint corporateComplaintDetails(
            @PathParam("complaintId") Integer complaintId) {
        return corporateDashboardService.getComplaintDetails(complaintId);
    }

    @GET
    @Path("corporate/citizenDetails/{complaintId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public Users corporateCitizenDetails(
            @PathParam("complaintId") Integer complaintId) {
        return corporateDashboardService.getCitizenDetails(complaintId);
    }

    @GET
    @Path("corporate/complaintReplies/{complaintId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<ComplaintReply> corporateComplaintReplies(
            @PathParam("complaintId") Integer complaintId) {
        return corporateDashboardService.getComplaintReplies(complaintId);
    }

    @GET
    @Path("corporate/statusHistory/{complaintId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<ComplaintStatusHistory> corporateStatusHistory(
            @PathParam("complaintId") Integer complaintId) {
        return corporateDashboardService.getStatusHistory(complaintId);
    }

    @GET
    @Path("corporate/escalationHistory/{complaintId}")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<ComplaintEscalation> corporateEscalationHistory(
            @PathParam("complaintId") Integer complaintId) {
        return corporateDashboardService.getEscalationHistory(complaintId);
    }

    // =========================================================
    // OFFICER DASHBOARD (CorporateDashboardService)
    // =========================================================

    @GET
    @Path("officer/dashboard/filterComplaints/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public List<Complaint> filterAssignedComplaintsAdvanced(
            @PathParam("officerId") Integer officerId,
            @QueryParam("complaintId") Integer complaintId,
            @QueryParam("categoryId") Integer categoryId,
            @QueryParam("status") String status,
            @QueryParam("priority") String priority,
            @QueryParam("citizenName") String citizenName,
            @QueryParam("overdueOnly") Boolean overdueOnly,
            @QueryParam("slaBreached") Boolean slaBreached,
            @QueryParam("complaintType") String complaintType) {

        return corporateDashboardService.filterAssignedComplaintsAdvanced(
                officerId, complaintId, categoryId, status,
                priority, citizenName, overdueOnly,
                slaBreached, complaintType);
    }

    @GET
    @Path("officer/dashboard/totalAssigned/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public Long officerTotalAssigned(
            @PathParam("officerId") Integer officerId) {
        return corporateDashboardService.getTotalAssigned(officerId);
    }

    @GET
    @Path("officer/dashboard/pendingCount/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public Long officerPendingCount(
            @PathParam("officerId") Integer officerId) {
        return corporateDashboardService.getPendingCount(officerId);
    }

    @GET
    @Path("officer/dashboard/inProgressCount/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public Long officerInProgressCount(
            @PathParam("officerId") Integer officerId) {
        return corporateDashboardService.getInProgressCount(officerId);
    }

    @GET
    @Path("officer/dashboard/resolvedCount/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public Long officerResolvedCount(
            @PathParam("officerId") Integer officerId) {
        return corporateDashboardService.getResolvedCount(officerId);
    }

    @GET
    @Path("officer/dashboard/overdueCount/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public Long officerOverdueCount(
            @PathParam("officerId") Integer officerId) {
        return corporateDashboardService.getOverdueCount(officerId);
    }

    @GET
    @Path("officer/dashboard/slaWarningCount/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public Long officerSlaWarningCount(
            @PathParam("officerId") Integer officerId) {
        return corporateDashboardService.getSlaWarningCount(officerId);
    }

    @GET
    @Path("officer/dashboard/todayComplaints/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public List<Complaint> officerTodayComplaints(
            @PathParam("officerId") Integer officerId) {
        return corporateDashboardService.getTodayComplaints(officerId);
    }

    @GET
    @Path("officer/dashboard/overdueComplaints/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public List<Complaint> officerOverdueComplaints(
            @PathParam("officerId") Integer officerId) {
        return corporateDashboardService.getOverdueComplaints(officerId);
    }

    @GET
    @Path("officer/dashboard/highPriorityComplaints/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public List<Complaint> officerHighPriorityComplaints(
            @PathParam("officerId") Integer officerId) {
        return corporateDashboardService.getHighPriorityComplaints(officerId);
    }
}
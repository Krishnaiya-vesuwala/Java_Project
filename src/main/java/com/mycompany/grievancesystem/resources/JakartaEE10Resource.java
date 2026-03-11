package com.mycompany.grievancesystem.resources;

import EJB.AdminBeanLocal;
import EJB.ComplaintBeanLocal;
import EJB.OfficerBeanLocal;
import EJB.UserBeanLocal;
import Entity.Complaint;
import Entity.Officers;
import Entity.SlaRules;
import Entity.Users;
import jakarta.ejb.EJB;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
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
    
    //Zone
    @POST
    @Path("createZone/{zoneName}/{status}/{corporationId}")
    public void createZone(@PathParam ("zoneName") String zoneName, @PathParam ("status") String status,@PathParam ("corporationId") Integer corporationId){
        adminBean.createZone(zoneName, status, corporationId);
    }
    
    @PUT
    @Path("updateZone/{zoneId}/{zoneName}/{status}/{corporationId}")
    public void updateZone(@PathParam ("zoneId") Integer zoneId, @PathParam ("zoneName") String zoneName, @PathParam ("status") String status,@PathParam ("corporationId") Integer corporationId)
    {
        adminBean.updateZone(zoneId, zoneName, status, corporationId);
    }
    
    @DELETE
    @Path("deleteZone/{zoneId}/{zoneName}/{status}/{corporationId}")
    public void deleteZone(@PathParam ("zoneId") Integer zoneId)
    {
        adminBean.deleteZone(zoneId);
    }
    
    //Department

    @POST
    @Path("createDepartment/{departmentName}/{description}/{status}")
    public void createDepartment(@PathParam("departmentName") String departmentName,
                                 @PathParam("description") String description,
                                 @PathParam("status") String status)
    {
        adminBean.createDepartment(departmentName, description, status);
    }

    @PUT
    @Path("updateDepartment/{id}/{name}/{desc}/{status}")
    public void updateDepartment(@PathParam("id") Integer id,
                                 @PathParam("name") String name,
                                 @PathParam("desc") String desc,
                                 @PathParam("status") String status)
    {
        adminBean.updateDepartment(id, name, desc, status);
    }

    @DELETE
    @Path("deleteDepartment/{id}")
    public void deleteDepartment(@PathParam("id") Integer id)
    {
        adminBean.deleteDepartment(id);
    }

    //Society

    @POST
    @Path("createSociety/{wardId}/{societyName}/{address}/{status}")
    public void createSociety(@PathParam("wardId") Integer wardId,
                              @PathParam("societyName") String societyName,
                              @PathParam("address") String address,
                              @PathParam("status") String status)
    {
        adminBean.createSociety(wardId, societyName, address, status);
    }

    @PUT
    @Path("updateSociety/{id}/{name}/{address}/{status}/{wardId}")
    public void updateSociety(@PathParam("id") Integer id,
                              @PathParam("name") String name,
                              @PathParam("address") String address,
                              @PathParam("status") String status,
                              @PathParam("wardId") Integer wardId)
    {
        adminBean.updateSociety(id, name, address, status, wardId);
    }

    @DELETE
    @Path("deleteSociety/{id}")
    public void deleteSociety(@PathParam("id") Integer id)
    {
        adminBean.deleteSociety(id);
    }

    //Category

    @POST
    @Path("createCategory/{categoryName}/{departmentId}")
    public void createCategory(@PathParam("categoryName") String categoryName,
                               @PathParam("departmentId") Integer departmentId)
    {
        adminBean.createCategory(categoryName, departmentId);
    }

    @PUT
    @Path("updateCategory/{id}/{name}/{departmentId}")
    public void updateCategory(@PathParam("id") Integer id,
                               @PathParam("name") String name,
                               @PathParam("departmentId") Integer departmentId)
    {
        adminBean.updateCategory(id, name, departmentId);
    }

    @DELETE
    @Path("deleteCategory/{id}")
    public void deleteCategory(@PathParam("id") Integer id)
    {
        adminBean.deleteCategory(id);
    }

    //Ward

    @POST
    @Path("createWard/{zoneId}/{wardName}/{status}")
    public void createWard(@PathParam("zoneId") Integer zoneId,
                           @PathParam("wardName") String wardName,
                           @PathParam("status") String status)
    {
        adminBean.createWard(zoneId, wardName, status);
    }

    @PUT
    @Path("updateWard/{wardId}/{zoneId}/{wardName}/{status}")
    public void updateWard(@PathParam("wardId") int wardId,
                           @PathParam("zoneId") int zoneId,
                           @PathParam("wardName") String wardName,
                           @PathParam("status") String status)
    {
        adminBean.updateWard(wardId, zoneId, wardName, status);
    }

    @DELETE
    @Path("deleteWard/{wardId}")
    public void deleteWard(@PathParam("wardId") int wardId)
    {
        adminBean.deleteWard(wardId);
    }

    //Officer

    @POST
    @Path("createOfficer/{userId}/{departmentId}/{zoneId}/{wardId}/{designation}")
    public void createOfficer(@PathParam("userId") Integer userId,
                              @PathParam("departmentId") Integer departmentId,
                              @PathParam("zoneId") Integer zoneId,
                              @PathParam("wardId") Integer wardId,
                              @PathParam("designation") String designation)
    {
        adminBean.createOfficer(userId, departmentId, zoneId, wardId, designation);
    }

    @PUT
    @Path("updateOfficer/{officerId}/{userId}/{departmentId}/{zoneId}/{wardId}/{designation}")
    public void updateOfficer(@PathParam("officerId") int officerId,
                              @PathParam("userId") int userId,
                              @PathParam("departmentId") int departmentId,
                              @PathParam("zoneId") int zoneId,
                              @PathParam("wardId") int wardId,
                              @PathParam("designation") String designation)
    {
        adminBean.updateOfficer(officerId, userId, departmentId, zoneId, wardId, designation);
    }

    @DELETE
    @Path("deleteOfficer/{officerId}")
    public void deleteOfficer(@PathParam("officerId") int officerId)
    {
        adminBean.deleteOfficer(officerId);
    }

    //SLA Rules

    @POST
    @Path("addSlaRule/{categoryId}/{maxDays}")
    public void addSlaRule(@PathParam("categoryId") Integer categoryId,
                           @PathParam("maxDays") Integer maxDays)
    {
        adminBean.addSlaRules(categoryId, maxDays);
    }

    @PUT
    @Path("updateSlaRule/{slaId}/{maxDays}")
    public void updateSlaRule(@PathParam("slaId") Integer slaId,
                              @PathParam("maxDays") Integer maxDays)
    {
        adminBean.updateSlaRule(slaId, maxDays);
    }

    @DELETE
    @Path("deleteSlaRule/{slaId}")
    public void deleteSlaRule(@PathParam("slaId") Integer slaId)
    {
        adminBean.deleteSlaRule(slaId);
    }
    
    @GET
    @Path("getAllSlaRules")
    @Produces("application/json")
    public List<SlaRules> getAllSlaRules()
    {
        return adminBean.getAllSlaRules();
    }
    
    //Complaint
    @POST
    @Path("createComplaint/{userId}/{categoryId}/{societyId}/{wardId}/{zoneId}/{title}/{description}/{status}/{priority}")
    public void createComplaint(@PathParam("userId") Integer userId,
                                @PathParam("categoryId") Integer categoryId,
                                @PathParam("societyId") Integer societyId,
                                @PathParam("wardId") Integer wardId,
                                @PathParam("zoneId") Integer zoneId,
                                @PathParam("title") String title,
                                @PathParam("description") String description,
                                @PathParam("status") String status,
                                @PathParam("priority") String priority)
    {
        complaintBean.createComplaint(userId, categoryId, societyId, wardId, zoneId, title, description, status, priority);
    }
    
    @GET
    @Path("getComplaintByUser/{userId}")
    @Produces("application/json")
    public List<Object[]> getComplaintByUser(@PathParam("userId") Integer userId)
    {
        return complaintBean.getComplaintByUserId(userId);
    }
    
    @POST
    @Path("createComplaintReply/{complaintId}/{repliedBy}/{message}")
    public void createComplaintReply(@PathParam("complaintId") int complaintId,
                                     @PathParam("repliedBy") int repliedBy,
                                     @PathParam("message") String message)
    {
        complaintBean.createComplaintReply(complaintId, repliedBy, message);
    }
    
    //OfficerBean
    
    @GET
    @Path("getAssignedComplaint/{officerId}")
    @Produces("application/json")
    public List<Complaint> getAssignedComplaint(@PathParam("officerId") int officerId)
    {
        return officerBean.getAssignedComplaint(officerId);
    }
    
    @PUT
    @Path("updateComplaintStatus/{complaintId}/{status}/{loggedInUser}")
    public void updateComplaintStatus(@PathParam("complaintId") int complaintId,
                                      @PathParam("status") String status,
                                      @PathParam("loggedInUser") int loggedInUser)
    {
        officerBean.updateComplaintStatus(complaintId, status, loggedInUser);
    }
    
    @GET
    @Path("getOfficerProfile/{userId}")
    @Produces("application/json")
    public Officers getOfficerProfile(@PathParam("userId") int userId)
    {
        return officerBean.getOfficerProfile(userId);
    }
    
    @GET
    @Path("getComplaintByOfficer/{officerId}")
    @Produces("application/json")
    public List<Complaint> getComplaintByOfficer(@PathParam("officerId") int officerId)
    {
        return officerBean.getComplaintByOfficer(officerId);
    }
    
    //User
    @GET
    @Path("login/{username}/{password}")
    @Produces("application/json")
    public Users login(@PathParam("username") String username,
                       @PathParam("password") String password)
    {
        return userBean.login(username, password);
    }
    
    @POST
    @Path("registerUser/{fullname}/{email}/{mobile}/{username}/{password}/{role}/{societyId}")
    public void registerUser(@PathParam("fullname") String fullname,
                             @PathParam("email") String email,
                             @PathParam("mobile") String mobile,
                             @PathParam("username") String username,
                             @PathParam("password") String password,
                             @PathParam("role") String role,
                             @PathParam("societyId") Integer societyId){

        userBean.registerUser(fullname,email,mobile,username,password,role,societyId);
    }
    
    @GET
    @Path("getUserById/{userId}")
    @Produces("application/json")
    public Users getUserById(@PathParam("userId") int userId)
    {
        return userBean.getUserById(userId);
    }
    
    @POST
    @Path("forgotPassword/{username}")
    @Produces("application/json")
    public Users forgotPassword(@PathParam("username") String username){
        return userBean.forgotPassword(username);
    }
    
    @PUT
    @Path("resetPassword/{userId}/{newPassword}")
    public void resetPassword(@PathParam("userId") int userId,
                              @PathParam("newPassword") String newPassword){

        userBean.resetPassword(userId, newPassword);
    }
}

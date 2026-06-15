package com.mycompany.grievancesystem.resources;

import EJB.AdminBeanLocal;
import EJB.ComplaintBeanLocal;
import EJB.OfficerBeanLocal;
import EJB.UserBeanLocal;
import Entity.Complaint;
import Entity.ComplaintCategory;
import Entity.ComplaintStatusHistory;
import Entity.Officers;
import Entity.SlaRules;
import Entity.Society;
import Entity.Users;
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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
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
    @Secured(roles = {"Admin"})
    public void createZone(@PathParam ("zoneName") String zoneName, @PathParam ("status") String status,@PathParam ("corporationId") Integer corporationId){
        adminBean.createZone(zoneName, status, corporationId);
    }
    
    @PUT
    @Path("updateZone/{zoneId}/{zoneName}/{status}/{corporationId}")
    @Secured(roles = {"Admin"})
    public void updateZone(@PathParam ("zoneId") Integer zoneId, @PathParam ("zoneName") String zoneName, @PathParam ("status") String status,@PathParam ("corporationId") Integer corporationId)
    {
        adminBean.updateZone(zoneId, zoneName, status, corporationId);
    }
    
    @DELETE
    @Path("deleteZone/{zoneId}/{zoneName}/{status}/{corporationId}")
    @Secured(roles = {"Admin"})
    public void deleteZone(@PathParam ("zoneId") Integer zoneId)
    {
        adminBean.deleteZone(zoneId);
    }
    
    //Department

    @POST
    @Path("createDepartment/{departmentName}/{description}/{status}")
    @Secured(roles = {"Admin"})
    public void createDepartment(@PathParam("departmentName") String departmentName,
                                 @PathParam("description") String description,
                                 @PathParam("status") String status)
    {
        adminBean.createDepartment(departmentName, description, status);
    }

    @PUT
    @Path("updateDepartment/{id}/{name}/{desc}/{status}")
    @Secured(roles = {"Admin"})
    public void updateDepartment(@PathParam("id") Integer id,
                                 @PathParam("name") String name,
                                 @PathParam("desc") String desc,
                                 @PathParam("status") String status)
    {
        adminBean.updateDepartment(id, name, desc, status);
    }

    @DELETE
    @Path("deleteDepartment/{id}")
    @Secured(roles = {"Admin"})
    public void deleteDepartment(@PathParam("id") Integer id)
    {
        adminBean.deleteDepartment(id);
    }

    //Society

    @POST
    @Path("createSociety/{wardId}/{societyName}/{address}/{status}")
    @Secured(roles = {"Admin"})
    public void createSociety(@PathParam("wardId") Integer wardId,
                              @PathParam("societyName") String societyName,
                              @PathParam("address") String address,
                              @PathParam("status") String status)
    {
        adminBean.createSociety(wardId, societyName, address, status);
    }

    @PUT
    @Path("updateSociety/{id}/{name}/{address}/{status}/{wardId}")
    @Secured(roles = {"Admin"})
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
    @Secured(roles = {"Admin"})
    public void deleteSociety(@PathParam("id") Integer id)
    {
        adminBean.deleteSociety(id);
    }
    
    @GET
    @Path("getSocietiesByWard/{wardId}")
    @Produces("application/json")
    public List<Society> getAllSocities(@PathParam("wardId") int wardId) {
        return adminBean.getAllSocities(wardId);
    }
    
    @GET
    @Path("getAllSocieties")
    @Produces("application/json")
    public List<Society> getAllSocities() {

        return adminBean.getAllSocities();
    }

    //Category

    @POST
    @Path("createCategory/{categoryName}/{departmentId}")
    @Secured(roles = {"Admin"})
    public void createCategory(@PathParam("categoryName") String categoryName,
                               @PathParam("departmentId") Integer departmentId)
    {
        adminBean.createCategory(categoryName, departmentId);
    }

    @PUT
    @Path("updateCategory/{id}/{name}/{departmentId}")
    @Secured(roles = {"Admin"})
    public void updateCategory(@PathParam("id") Integer id,
                               @PathParam("name") String name,
                               @PathParam("departmentId") Integer departmentId)
    {
        adminBean.updateCategory(id, name, departmentId);
    }

    @DELETE
    @Path("deleteCategory/{id}")
    @Secured(roles = {"Admin"})
    public void deleteCategory(@PathParam("id") Integer id)
    {
        adminBean.deleteCategory(id);
    }
    
    @GET
    @Path("getAllCategories")
    @Produces("application/json")
    @Secured(roles = {"Admin", "Officer", "Citizen"})
    public List<ComplaintCategory> getAllCategories() {
        return adminBean.getAllCategory();
    }

    //Ward

    @POST
    @Path("createWard/{zoneId}/{wardName}/{status}")
    @Secured(roles = {"Admin"})
    public void createWard(@PathParam("zoneId") Integer zoneId,
                           @PathParam("wardName") String wardName,
                           @PathParam("status") String status)
    {
        adminBean.createWard(zoneId, wardName, status);
    }

    @PUT
    @Path("updateWard/{wardId}/{zoneId}/{wardName}/{status}")
    @Secured(roles = {"Admin"})
    public void updateWard(@PathParam("wardId") int wardId,
                           @PathParam("zoneId") int zoneId,
                           @PathParam("wardName") String wardName,
                           @PathParam("status") String status)
    {
        adminBean.updateWard(wardId, zoneId, wardName, status);
    }

    @DELETE
    @Path("deleteWard/{wardId}")
    @Secured(roles = {"Admin"})
    public void deleteWard(@PathParam("wardId") int wardId)
    {
        adminBean.deleteWard(wardId);
    }

    //Officer

    @POST
    @Path("createOfficer/{userId}/{departmentId}/{zoneId}/{wardId}/{designation}")
    @Secured(roles = {"Admin"})
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
    @Secured(roles = {"Admin"})
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
    @Secured(roles = {"Admin"})
    public void deleteOfficer(@PathParam("officerId") int officerId)
    {
        adminBean.deleteOfficer(officerId);
    }

    //SLA Rules

    @POST
    @Path("addSlaRule/{categoryId}/{maxDays}")
    @Secured(roles = {"Admin"})
    public void addSlaRule(@PathParam("categoryId") Integer categoryId,
                           @PathParam("maxDays") Integer maxDays)
    {
        adminBean.addSlaRules(categoryId, maxDays);
    }

    @PUT
    @Path("updateSlaRule/{slaId}/{maxDays}")
    @Secured(roles = {"Admin"})
    public void updateSlaRule(@PathParam("slaId") Integer slaId,
                              @PathParam("maxDays") Integer maxDays)
    {
        adminBean.updateSlaRule(slaId, maxDays);
    }

    @DELETE
    @Path("deleteSlaRule/{slaId}")
    @Secured(roles = {"Admin"})
    public void deleteSlaRule(@PathParam("slaId") Integer slaId)
    {
        adminBean.deleteSlaRule(slaId);
    }
    
    @GET
    @Path("getAllSlaRules")
    @Produces("application/json")
    @Secured(roles = {"Admin"})
    public List<SlaRules> getAllSlaRules()
    {
        return adminBean.getAllSlaRules();
    }
    
    //Complaint
    @GET
    @Path("decodeQRCode/{wardID}")
    @Produces("application/json")
    @Secured(roles = {"Citizen"})
    public List<Society> decodeQRCode(@PathParam("wardID") Integer wardID){
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
                    userId,
                    categoryId,
                    societyId,
                    wardId,
                    title,
                    description,
                    priority);

            return Response.ok("Complaint Created").build();

        } catch (Exception e) {

            e.printStackTrace();

            return Response.serverError()
                    .entity(e.getMessage())
                    .build();
        }
    }
    
    @GET
    @Path("getComplaintByUser/{userId}")
    @Produces("application/json")
    @Secured(roles = {"Citizen"})
    public List<Object[]> getComplaintByUser(@PathParam("userId") Integer userId)
    {
         System.out.println("GET COMPLAINT BY USER CALLED");
        return complaintBean.getComplaintByUserId(userId);
    }
    
    //Role pending
    @POST
    @Path("createComplaintReply/{complaintId}/{repliedBy}/{message}")
    @Secured(roles = {"Citizen"})
    public void createComplaintReply(@PathParam("complaintId") int complaintId,
                                     @PathParam("repliedBy") int repliedBy,
                                     @PathParam("message") String message)
    {
        complaintBean.createComplaintReply(complaintId, repliedBy, message);
    }
    
    @GET
    @Path("complaintHistory/{complaintId}")
    @Produces("application/json")
    @Secured(roles = {"Citizen"})
    public List<ComplaintStatusHistory> getHistory(@PathParam("complaintId") int complaintId){
        return complaintBean.getComplaintStatusHistory(complaintId);
    }
    
    @GET
    @Path("citizenNotifications/{userId}")
    @Secured(roles = {"Citizen"})
    public Response citizenNotifications(
            @PathParam("userId") Integer userId) {

        List<Object[]> notifications =
                complaintBean.getCitizenNotifications(userId);

        return Response.ok(notifications).build();
    }
    
    @GET
    @Path("totalComplaints/{userId}")
    @Secured(roles = {"Citizen"})
    @Produces("application/json")
    public Long totalComplaints(
            @PathParam("userId") Integer userId) {

        return complaintBean.getTotalComplaintsByUser(userId);
    }

    @GET
    @Path("assignedComplaints/{userId}")
    @Secured(roles = {"Citizen"})
    @Produces("application/json")
    public Long assignedComplaints(
            @PathParam("userId") Integer userId) {

        return complaintBean.getAssignedComplaintsByUser(userId);
    }

    @GET
    @Path("resolvedComplaints/{userId}")
    @Secured(roles = {"Citizen"})
    @Produces("application/json")
    public Long resolvedComplaints(
            @PathParam("userId") Integer userId) {

        return complaintBean.getResolvedComplaintsByUser(userId);
    }

    @GET
    @Path("rejectedComplaints/{userId}")
    @Secured(roles = {"Citizen"})
    @Produces("application/json")
    public Long rejectedComplaints(
            @PathParam("userId") Integer userId) {

        return complaintBean.getRejectedComplaintsByUser(userId);
    }

    @GET
    @Path("recentComplaints/{userId}")
    @Secured(roles = {"Citizen"})
    @Produces("application/json")
    public List<Complaint> recentComplaints(
            @PathParam("userId") Integer userId) {

        return complaintBean.getRecentComplaintsByUser(userId);
    }
    
    //OfficerBean
    
    @GET
    @Path("getAssignedComplaint/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public List<Complaint> getAssignedComplaint(@PathParam("officerId") int officerId)
    {
        return officerBean.getAssignedComplaint(officerId);
    }
    
    @PUT
    @Path("updateComplaintStatus/{complaintId}/{status}/{loggedInUser}")
    @Secured(roles = {"Officer"})
    public void updateComplaintStatus(@PathParam("complaintId") int complaintId,
                                      @PathParam("status") String status,
                                      @PathParam("loggedInUser") int loggedInUser)
    {
        officerBean.updateComplaintStatus(complaintId, status, loggedInUser);
    }
    
    @GET
    @Path("getOfficerProfile/{userId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public Officers getOfficerProfile(@PathParam("userId") int userId)
    {
        return officerBean.getOfficerProfile(userId);
    }
    
    @GET
    @Path("getComplaintByOfficer/{officerId}")
    @Produces("application/json")
    @Secured(roles = {"Officer"})
    public List<Complaint> getComplaintByOfficer(@PathParam("officerId") int officerId)
    {
        return officerBean.getComplaintByOfficer(officerId);
    }
    
    //User
//    @GET
//    @Path("login/{username}/{password}")
//    @Produces("application/json")
//    public Users login(@PathParam("username") String username,
//                       @PathParam("password") String password)
//    {
//        return userBean.login(username, password);
//    }
    @POST
    @Path("login")
    @Consumes("application/json")
    @Produces("application/json")
    public Response login(Users requestUser) {

        Users user = userBean.login(
                requestUser.getUsername(),
                requestUser.getPassword()
        );

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

            userBean.registerUser(
                    fullname,
                    email,
                    mobile,
                    username,
                    password,
                    societyId);

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
    public Users getUserById(@PathParam("userId") int userId)
    {
        return userBean.getUserById(userId);
    }
    
    @PUT
    @Path("updateUser")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateUser(Users user) {
         System.out.println("REST method called");
    System.out.println(user.getFullName());

        userBean.updateUser(
                user.getUserId(),
                user.getFullName(),
                user.getEmail(),
                user.getMobile(),
                user.getUsername());

        return Response.ok().build();
    }
    
    @POST
    @Path("forgotPassword/{username}")
    @Produces("application/json")
    public Users forgotPassword(@PathParam("username") String username){
        return userBean.forgotPassword(username);
    }
    
    @PUT
    @Path("resetPassword/{userId}/{newPassword}")
    @Secured(roles = {"Admin", "Officer", "Citizen"})
    public void resetPassword(@PathParam("userId") int userId,
                              @PathParam("newPassword") String newPassword){

        userBean.resetPassword(userId, newPassword);
    }
    
    @POST
    @Path("submitFeedback/{complaintId}/{rating}/{comments}")
    @Secured(roles = {"Citizen"})
    public void submitFeedback(@PathParam("complaintId") int complaintId,
                               @PathParam("rating") String rating,
                               @PathParam("comments") String comments){

        userBean.submitFeedback(complaintId, rating, comments);
    }
}

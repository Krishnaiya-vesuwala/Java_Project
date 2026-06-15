/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/JerseyClient.java to edit this template
 */
package Client;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.text.MessageFormat;

public class RestClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/Java_Project/resources";

    public RestClient() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("jakartaee10");
    }

    public void createWard(String zoneId, String wardName, String status) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("createWard/{0}/{1}/{2}", new Object[]{zoneId, wardName, status})).request().post(null);
    }

    // UPDATED: Added token parameter and Authorization header
    public Response createComplaint(String userId, String categoryId, String societyId, String wardId, String title, String description, String status, String priority, String token) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("createComplaint/{0}/{1}/{2}/{3}/{4}/{5}/{6}/{7}", new Object[]{userId, categoryId, societyId, wardId, title, description, status, priority}))
                .request()
                .header("Authorization", "Bearer " + token)
                .post(null, Response.class);
    }

    public void updateCategory(String id, String name, String departmentId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateCategory/{0}/{1}/{2}", new Object[]{id, name, departmentId})).request().put(null);
    }

    public <T> T wardResolvedComplaints(Class<T> responseType, String wardId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("ward/resolvedComplaints/{0}", new Object[]{wardId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T zoneWiseComplaintCount(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("admin/zoneWiseComplaintCount");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T corporateEscalatedComplaints(Class<T> responseType, String corporationId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("corporate/escalatedComplaints/{0}", new Object[]{corporationId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T filterAssignedComplaintsAdvanced(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("officer/dashboard/filterComplaints/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getPendingComplaints(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getPendingComplaints");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void createOfficer(String userId, String departmentId, String zoneId, String wardId, String designation) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("createOfficer/{0}/{1}/{2}/{3}/{4}", new Object[]{userId, departmentId, zoneId, wardId, designation})).request().post(null);
    }

    public <T> T officerHighPriorityComplaints(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("officer/dashboard/highPriorityComplaints/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    // UPDATED: Added token parameter and Authorization header
    public Response updateUserFull(String userId, String fullName, String email, String mobile, String username, String role, String status, String societyId, String token) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("updateUserFull/{0}/{1}/{2}/{3}/{4}/{5}/{6}/{7}", new Object[]{userId, fullName, email, mobile, username, role, status, societyId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(null, Response.class);
    }

    public void deleteSlaRule(String slaId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("deleteSlaRule/{0}", new Object[]{slaId})).request().delete();
    }

    // UPDATED: Added token parameter and Authorization header
    public <T> T resolvedComplaints(Class<T> responseType, String userId, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("resolvedComplaints/{0}", new Object[]{userId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T zonePendingComplaints(Class<T> responseType, String zoneId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("zone/pendingComplaints/{0}", new Object[]{zoneId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T officerSlaWarningCount(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("officer/dashboard/slaWarningCount/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void createCategory(String categoryName, String departmentId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("createCategory/{0}/{1}", new Object[]{categoryName, departmentId})).request().post(null);
    }

    public <T> T getComplaintsByWard(Class<T> responseType, String wardId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("ward/complaints/{0}", new Object[]{wardId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getAllOfficers(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllOfficers");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getAllUsers(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllUsers");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T officerPendingCount(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("officer/dashboard/pendingCount/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T officerResolvedCount(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("officer/dashboard/resolvedCount/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T adminInProgressComplaints(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("admin/inProgressComplaints");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T officerTotalAssigned(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("officer/dashboard/totalAssigned/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T wardWiseComplaintCount(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("admin/wardWiseComplaintCount");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void deactivateOfficer(String officerId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("officer/deactivate/{0}", new Object[]{officerId})).request().put(null);
    }

    public <T> T getAvailableUsers(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("officer/availableUsers");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T adminOpenComplaints(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("admin/openComplaints");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T adminClosedComplaints(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("admin/closedComplaints");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T searchOfficers(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("officer/search");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void updateSlaRule(String slaId, String maxDays, String status) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateSlaRule/{0}/{1}/{2}", new Object[]{slaId, maxDays, status})).request().put(null);
    }

    public void createDepartment(String departmentName, String description, String status) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("createDepartment/{0}/{1}/{2}", new Object[]{departmentName, description, status})).request().post(null);
    }

    public <T> T adminResolvedComplaints(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("admin/resolvedComplaints");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getAllComplaints(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllComplaints");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public Response registerUser(String fullname, String email, String mobile, String username, String password, String societyId) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("registerUser/{0}/{1}/{2}/{3}/{4}/{5}", new Object[]{fullname, email, mobile, username, password, societyId})).request().post(null, Response.class);
    }

    public void deleteSociety(String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("deleteSociety/{0}", new Object[]{id})).request().delete();
    }

    public <T> T adminEscalatedComplaints(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("admin/escalatedComplaints");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T wardPendingComplaints(Class<T> responseType, String wardId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("ward/pendingComplaints/{0}", new Object[]{wardId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    // UPDATED: Added token parameter and Authorization header
    public <T> T getUserById(Class<T> responseType, String userId, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getUserById/{0}", new Object[]{userId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T decodeQRCode(Class<T> responseType, String wardID) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("decodeQRCode/{0}", new Object[]{wardID}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void changeOfficerZone(String officerId, String zoneId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("changeOfficerZone/{0}/{1}", new Object[]{officerId, zoneId})).request().put(null);
    }

    public <T> T getComplaintByOfficer(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getComplaintByOfficer/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void updateZone(String zoneId, String zoneName, String status, String corporationId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateZone/{0}/{1}/{2}/{3}", new Object[]{zoneId, zoneName, status, corporationId})).request().put(null);
    }

    public Response login(Object requestEntity) throws ClientErrorException {
        return webTarget.path("login").request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).post(jakarta.ws.rs.client.Entity.entity(requestEntity, jakarta.ws.rs.core.MediaType.APPLICATION_JSON), Response.class);
    }

    public <T> T getComplaintsByZone(Class<T> responseType, String zoneId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("zone/complaints/{0}", new Object[]{zoneId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T corporateStatusHistory(Class<T> responseType, String complaintId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("corporate/statusHistory/{0}", new Object[]{complaintId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getCitizensByWard(Class<T> responseType, String wardId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("ward/citizens/{0}", new Object[]{wardId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T forgotPassword(Class<T> responseType, String username) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("forgotPassword/{0}", new Object[]{username})).request().post(null, responseType);
    }

    public <T> T getHistory(Class<T> responseType, String complaintId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("complaintHistory/{0}", new Object[]{complaintId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T corporateFilterComplaints(Class<T> responseType, String corporationId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("corporate/filterComplaints/{0}", new Object[]{corporationId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void createZone(String zoneName, String status, String corporationId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("createZone/{0}/{1}/{2}", new Object[]{zoneName, status, corporationId})).request().post(null);
    }

    public <T> T getOfficerById(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("officer/getById/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getAllWards(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllWards");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getAllZones(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllZones");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getOfficersByCorporation(Class<T> responseType, String corporationId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("officer/byCorporation/{0}", new Object[]{corporationId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void deactivateZone(String zoneId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("deactivateZone/{0}", new Object[]{zoneId})).request().put(null);
    }

    // UPDATED: Added token parameter and Authorization header
    public Response updateUser(String userId, String fullName, String email, String mobile, String username, String token) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("updateUser/{0}/{1}/{2}/{3}/{4}", new Object[]{userId, fullName, email, mobile, username}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(null, Response.class);
    }

    // UPDATED: Added token parameter and Authorization header
    public <T> T getAllCategories(Class<T> responseType, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllCategories");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public void deleteCategory(String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("deleteCategory/{0}", new Object[]{id})).request().delete();
    }

    public void createSociety(String wardId, String societyName, String address, String status) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("createSociety/{0}/{1}/{2}/{3}", new Object[]{wardId, societyName, address, status})).request().post(null);
    }

    public <T> T officerOverdueComplaints(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("officer/dashboard/overdueComplaints/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void createComplaintReply(String complaintId, String repliedBy, String message) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("createComplaintReply/{0}/{1}/{2}", new Object[]{complaintId, repliedBy, message})).request().post(null);
    }

    public <T> T getZoneById(Class<T> responseType, String zoneId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getZoneById/{0}", new Object[]{zoneId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    // UPDATED: Added token parameter and Authorization header
    public <T> T citizenNotifications(Class<T> responseType, String userId, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("citizenNotifications/{0}", userId));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getOfficerProfile(Class<T> responseType, String userId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getOfficerProfile/{0}", new Object[]{userId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T corporateTotalComplaints(Class<T> responseType, String corporationId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("corporate/totalComplaints/{0}", new Object[]{corporationId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void deleteOfficer(String officerId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("deleteOfficer/{0}", new Object[]{officerId})).request().delete();
    }

    public void changeOfficerWard(String officerId, String wardId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("changeOfficerWard/{0}/{1}", new Object[]{officerId, wardId})).request().put(null);
    }

    public <T> T zoneRejectedComplaints(Class<T> responseType, String zoneId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("zone/rejectedComplaints/{0}", new Object[]{zoneId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T createUser(Object requestEntity, Class<T> responseType) throws ClientErrorException {
        return webTarget.path("createUser").request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).post(jakarta.ws.rs.client.Entity.entity(requestEntity, jakarta.ws.rs.core.MediaType.APPLICATION_JSON), responseType);
    }

    public <T> T getAllSlaRules(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllSlaRules");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void resetPassword(String userId, String password, String token) {
        webTarget.path("resetPassword")
                 .path(userId)
                 .path(password)
                 .request()
                 .header("Authorization", "Bearer " + token)
                 .put(Entity.text(""));
    }

    public <T> T getCitizensByZone(Class<T> responseType, String zoneId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("zone/citizens/{0}", new Object[]{zoneId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T officerInProgressCount(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("officer/dashboard/inProgressCount/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getMasterCategories(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("masterdata/categories");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getAllDepartments(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllDepartments");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getWardsByZone(Class<T> responseType, String zoneId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getWardsByZone/{0}", new Object[]{zoneId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void addSlaRule(String categoryId, String maxDays, String status) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("addSlaRule/{0}/{1}/{2}", new Object[]{categoryId, maxDays, status})).request().post(null);
    }

    // UPDATED: Added token parameter and Authorization header
    public <T> T rejectedComplaints(Class<T> responseType, String userId, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("rejectedComplaints/{0}", new Object[]{userId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public void transferOfficer(String officerId, String zoneId, String wardId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("officer/transfer/{0}/{1}/{2}", new Object[]{officerId, zoneId, wardId})).request().put(null);
    }

    public <T> T getComplaintReplies(Class<T> responseType, String complaintId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("complaintReplies/{0}", new Object[]{complaintId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getMasterZones(Class<T> responseType, String corporationId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("masterdata/zones/{0}", new Object[]{corporationId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getAllCorporations(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllCorporations");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getAllSocietiesByWard(Class<T> responseType, String wardId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getAllSocietiesByWard/{0}", new Object[]{wardId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getAllSocieties(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllSocieties");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    // UPDATED: Added token parameter and Authorization header
    public <T> T totalComplaints(Class<T> responseType, String userId, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("totalComplaints/{0}", new Object[]{userId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T adminAssignedComplaints(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("admin/assignedComplaints");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T corporateCitizenDetails(Class<T> responseType, String complaintId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("corporate/citizenDetails/{0}", new Object[]{complaintId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T corporateEscalationHistory(Class<T> responseType, String complaintId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("corporate/escalationHistory/{0}", new Object[]{complaintId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void updateComplaintStatus(String complaintId, String status, String loggedInUser) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateComplaintStatus/{0}/{1}/{2}", new Object[]{complaintId, status, loggedInUser})).request().put(null);
    }

    public void officerCreate(String userId, String designation, String departmentId, String zoneId, String wardId, String status) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("officer/create/{0}/{1}/{2}/{3}/{4}/{5}", new Object[]{userId, designation, departmentId, zoneId, wardId, status})).request().post(null);
    }

    // UPDATED: Added token parameter and Authorization header
    public <T> T recentComplaints(Class<T> responseType, String userId, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("recentComplaints/{0}", new Object[]{userId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getAdminProfile(Class<T> responseType, String userId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getAdminProfile/{0}", new Object[]{userId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getMasterDepartments(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("masterdata/departments");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T corporateAssignedComplaints(Class<T> responseType, String corporationId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("corporate/assignedComplaints/{0}", new Object[]{corporationId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void updateOfficer(String officerId, String userId, String departmentId, String zoneId, String wardId, String designation) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateOfficer/{0}/{1}/{2}/{3}/{4}/{5}", new Object[]{officerId, userId, departmentId, zoneId, wardId, designation})).request().put(null);
    }

    public <T> T getAssignedComplaint(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getAssignedComplaint/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T corporateOpenComplaints(Class<T> responseType, String corporationId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("corporate/openComplaints/{0}", new Object[]{corporationId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T wardRejectedComplaints(Class<T> responseType, String wardId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("ward/rejectedComplaints/{0}", new Object[]{wardId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void updateOfficerDepartment(String officerId, String departmentId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("officer/updateDepartment/{0}/{1}", new Object[]{officerId, departmentId})).request().put(null);
    }

    public void updateWard(String wardId, String zoneId, String wardName, String status) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateWard/{0}/{1}/{2}/{3}", new Object[]{wardId, zoneId, wardName, status})).request().put(null);
    }

    public <T> T findOfficer(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("officer/find/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void deleteDepartment(String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("deleteDepartment/{0}", new Object[]{id})).request().delete();
    }

    public <T> T getSocietiesByZone(Class<T> responseType, String zoneId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("zone/societies/{0}", new Object[]{zoneId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getWardsByZoneComplaint(Class<T> responseType, String zoneId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("zone/wards/{0}", new Object[]{zoneId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T categoryWiseComplaintCount(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("admin/categoryWiseComplaintCount");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T adminTotalComplaints(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("admin/totalComplaints");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void updateOfficerStatus(String officerId, String status) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("officer/updateStatus/{0}/{1}", new Object[]{officerId, status})).request().put(null);
    }

    public <T> T getAssignedComplaintCount(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("officer/assignedCount/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void deleteZone(String zoneId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("deleteZone/{0}", new Object[]{zoneId})).request().delete();
    }

    public void updateOfficerDesignation(String officerId, String designation) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("officer/updateDesignation/{0}/{1}", new Object[]{officerId, designation})).request().put(null);
    }

    public void submitFeedback(String complaintId, String rating, String comments) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("submitFeedback/{0}/{1}/{2}", new Object[]{complaintId, rating, comments})).request().post(null);
    }

    public <T> T corporateComplaintDetails(Class<T> responseType, String complaintId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("corporate/complaintDetails/{0}", new Object[]{complaintId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getSocietiesByWard(Class<T> responseType, String wardId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("ward/societies/{0}", new Object[]{wardId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void updateSociety(String id, String name, String address, String status, String wardId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateSociety/{0}/{1}/{2}/{3}/{4}", new Object[]{id, name, address, status, wardId})).request().put(null);
    }

    public <T> T wardTotalComplaints(Class<T> responseType, String wardId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("ward/totalComplaints/{0}", new Object[]{wardId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getOfficersByWard(Class<T> responseType, String wardId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("ward/officers/{0}", new Object[]{wardId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T getWardOfficers(Class<T> responseType, String wardId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("ward/wardOfficers/{0}", new Object[]{wardId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T officerGetAll(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("officer/all");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T corporateComplaintReplies(Class<T> responseType, String complaintId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("corporate/complaintReplies/{0}", new Object[]{complaintId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    // UPDATED: Added token parameter and Authorization header
    public <T> T getComplaintByUser(Class<T> responseType, String userId, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getComplaintByUser/{0}", new Object[]{userId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getMasterWards(Class<T> responseType, String corporationId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("masterdata/wards/{0}", new Object[]{corporationId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void removeOfficer(String officerId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("officer/remove/{0}", new Object[]{officerId})).request().delete();
    }

    public <T> T getZonesByCorporation(Class<T> responseType, String corporationId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getZonesByCorporation/{0}", new Object[]{corporationId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T zoneResolvedComplaints(Class<T> responseType, String zoneId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("zone/resolvedComplaints/{0}", new Object[]{zoneId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T zoneTotalComplaints(Class<T> responseType, String zoneId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("zone/totalComplaints/{0}", new Object[]{zoneId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void activateOfficer(String officerId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("officer/activate/{0}", new Object[]{officerId})).request().put(null);
    }

    public void deleteWard(String wardId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("deleteWard/{0}", new Object[]{wardId})).request().delete();
    }

    public <T> T getOfficersByZone(Class<T> responseType, String zoneId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("zone/officers/{0}", new Object[]{zoneId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void updateDepartment(String id, String name, String desc, String status) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateDepartment/{0}/{1}/{2}/{3}", new Object[]{id, name, desc, status})).request().put(null);
    }

    public <T> T officerTodayComplaints(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("officer/dashboard/todayComplaints/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void activateZone(String zoneId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("activateZone/{0}", new Object[]{zoneId})).request().put(null);
    }

    // UPDATED: Added token parameter and Authorization header
    public <T> T assignedComplaints(Class<T> responseType, String userId, String token) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("assignedComplaints/{0}", new Object[]{userId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T corporateResolvedComplaints(Class<T> responseType, String corporationId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("corporate/resolvedComplaints/{0}", new Object[]{corporationId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public <T> T officerOverdueCount(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("officer/dashboard/overdueCount/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void revokeOfficerRole(String officerId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("officer/revokeRole/{0}", new Object[]{officerId})).request().put(null);
    }

    public <T> T departmentWiseComplaintCount(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("admin/departmentWiseComplaintCount");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }
       public <T> T getAllSocities(Class<T> responseType, String wardId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getSocietiesByWard/{0}", new Object[]{wardId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

     public <T> T getCitizenNotifications(
        Class<T> responseType,
        String userId,
        String token) {

        WebTarget resource = webTarget;

        resource = resource.path(
                MessageFormat.format(
                        "citizenNotifications/{0}",
                        userId));

        return resource.request(MediaType.APPLICATION_JSON)
                .header("Authorization",
                        "Bearer " + token)
                .get(responseType);
    }
    
    public void close() {
        client.close();
    }
}
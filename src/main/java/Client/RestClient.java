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

    // =========================================================
    // PUBLIC ENDPOINTS (No Token Required)
    // =========================================================

    public Response login(Object requestEntity) throws ClientErrorException {
        return webTarget.path("login")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(requestEntity, MediaType.APPLICATION_JSON), Response.class);
    }

    public Response registerUser(String fullname, String email, String mobile,
            String username, String password, String societyId) throws ClientErrorException {
        return webTarget.path(MessageFormat.format(
                "registerUser/{0}/{1}/{2}/{3}/{4}/{5}",
                new Object[]{fullname, email, mobile, username, password, societyId}))
                .request()
                .post(null, Response.class);
    }

   public <T> T getAllSocities(Class<T> responseType, String wardId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getSocietiesByWard/{0}", new Object[]{wardId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }
    
    public <T> T getAllSocities(Class<T> responseType)
            throws ClientErrorException {

        WebTarget resource = webTarget;

        resource = resource.path("getAllSocieties");

        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }


    public <T> T forgotPassword(Class<T> responseType, String username) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("forgotPassword/{0}", new Object[]{username}))
                .request()
                .post(null, responseType);
    }

    // =========================================================
    // ZONE (Token Required)
    // =========================================================

    public void createZone(String zoneName, String status, String corporationId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("createZone/{0}/{1}/{2}", new Object[]{zoneName, status, corporationId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .post(null);
    }

    public void updateZone(String zoneId, String zoneName, String status, String corporationId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("updateZone/{0}/{1}/{2}/{3}", new Object[]{zoneId, zoneName, status, corporationId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(jakarta.ws.rs.client.Entity.text(""), Response.class);
    }

    public void deleteZone(String zoneId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("deleteZone/{0}", new Object[]{zoneId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .delete();
    }

    public <T> T getAllZones(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("getAllZones")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getZoneById(Class<T> responseType, String zoneId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("getZoneById/{0}", new Object[]{zoneId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getZonesByCorporation(Class<T> responseType, String corporationId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("getZonesByCorporation/{0}", new Object[]{corporationId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public void activateZone(String zoneId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("activateZone/{0}", new Object[]{zoneId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(null);
    }

    public void deactivateZone(String zoneId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("deactivateZone/{0}", new Object[]{zoneId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(null);
    }

    public <T> T getWardsByZone(Class<T> responseType, String zoneId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("getWardsByZone/{0}", new Object[]{zoneId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // CORPORATION (Token Required)
    // =========================================================

    public <T> T getAllCorporations(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("getAllCorporations")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // DEPARTMENT (Token Required)
    // =========================================================

    public void createDepartment(String departmentName, String description, String status, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("createDepartment/{0}/{1}/{2}", new Object[]{departmentName, description, status}))
                .request()
                .header("Authorization", "Bearer " + token)
                .post(null);
    }

    public void updateDepartment(String id, String name, String desc, String status, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("updateDepartment/{0}/{1}/{2}/{3}", new Object[]{id, name, desc, status}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(jakarta.ws.rs.client.Entity.text(""), Response.class);
    }

    public void deleteDepartment(String id, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("deleteDepartment/{0}", new Object[]{id}))
                .request()
                .header("Authorization", "Bearer " + token)
                .delete();
    }

    public <T> T getAllDepartments(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("getAllDepartments")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // SOCIETY (Token Required)
    // =========================================================

    public void createSociety(String wardId, String societyName, String address, String status, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("createSociety/{0}/{1}/{2}/{3}", new Object[]{wardId, societyName, address, status}))
                .request()
                .header("Authorization", "Bearer " + token)
                .post(null);
    }

    public void updateSociety(String id, String name, String address, String status, String wardId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("updateSociety/{0}/{1}/{2}/{3}/{4}", new Object[]{id, name, address, status, wardId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(jakarta.ws.rs.client.Entity.text(""), Response.class);
    }

    public void deleteSociety(String id, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("deleteSociety/{0}", new Object[]{id}))
                .request()
                .header("Authorization", "Bearer " + token)
                .delete();
    }

    public <T> T getAllSocietiesByWard(Class<T> responseType, String wardId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("getAllSocietiesByWard/{0}", new Object[]{wardId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getAllSocities(Class<T> responseType, String wardId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("getAllSocietiesByWard/{0}", new Object[]{wardId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // CATEGORY (Token Required)
    // =========================================================

    public void createCategory(String categoryName, String departmentId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("createCategory/{0}/{1}", new Object[]{categoryName, departmentId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .post(null);
    }

    public void updateCategory(String id, String name, String departmentId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("updateCategory/{0}/{1}/{2}", new Object[]{id, name, departmentId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(jakarta.ws.rs.client.Entity.text(""), Response.class);
    }

    public void deleteCategory(String id, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("deleteCategory/{0}", new Object[]{id}))
                .request()
                .header("Authorization", "Bearer " + token)
                .delete();
    }

    public <T> T getAllCategories(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("getAllCategories")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // WARD (Token Required)
    // =========================================================

    public void createWard(String zoneId, String wardName, String status, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("createWard/{0}/{1}/{2}", new Object[]{zoneId, wardName, status}))
                .request()
                .header("Authorization", "Bearer " + token)
                .post(null);
    }

    public void updateWard(String wardId, String zoneId, String wardName, String status, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("updateWard/{0}/{1}/{2}/{3}", new Object[]{wardId, zoneId, wardName, status}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(jakarta.ws.rs.client.Entity.text(""), Response.class);
    }

    public void deleteWard(String wardId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("deleteWard/{0}", new Object[]{wardId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .delete();
    }

    public <T> T getAllWards(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("getAllWards")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // OFFICER - Admin Management (Token Required)
    // =========================================================

    public void createOfficer(String userId, String departmentId, String zoneId, String wardId, String designation, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("createOfficer/{0}/{1}/{2}/{3}/{4}", new Object[]{userId, departmentId, zoneId, wardId, designation}))
                .request()
                .header("Authorization", "Bearer " + token)
                .post(null);
    }

    public void updateOfficer(String officerId, String userId, String departmentId, String zoneId, String wardId, String designation, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("updateOfficer/{0}/{1}/{2}/{3}/{4}/{5}", new Object[]{officerId, userId, departmentId, zoneId, wardId, designation}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(jakarta.ws.rs.client.Entity.text(""), Response.class);
    }

    public void deleteOfficer(String officerId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("deleteOfficer/{0}", new Object[]{officerId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .delete();
    }

    public <T> T getAllOfficers(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("getAllOfficers")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public void changeOfficerWard(String officerId, String wardId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("changeOfficerWard/{0}/{1}", new Object[]{officerId, wardId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(null);
    }

    public void changeOfficerZone(String officerId, String zoneId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("changeOfficerZone/{0}/{1}", new Object[]{officerId, zoneId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(null);
    }

    public void officerCreate(String userId, String designation, String departmentId, String zoneId, String wardId, String status, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("officer/create/{0}/{1}/{2}/{3}/{4}/{5}", new Object[]{userId, designation, departmentId, zoneId, wardId, status}))
                .request()
                .header("Authorization", "Bearer " + token)
                .post(null);
    }

    public <T> T getAvailableUsers(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("officer/availableUsers")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T officerGetAll(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("officer/all")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public void removeOfficer(String officerId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("officer/remove/{0}", new Object[]{officerId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .delete();
    }

    public void transferOfficer(String officerId, String zoneId, String wardId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("officer/transfer/{0}/{1}/{2}", new Object[]{officerId, zoneId, wardId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(null);
    }

    public <T> T getOfficersByCorporation(Class<T> responseType, String corporationId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("officer/byCorporation/{0}", new Object[]{corporationId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public void activateOfficer(String officerId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("officer/activate/{0}", new Object[]{officerId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(null);
    }

    public void deactivateOfficer(String officerId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("officer/deactivate/{0}", new Object[]{officerId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(null);
    }

    public void revokeOfficerRole(String officerId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("officer/revokeRole/{0}", new Object[]{officerId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(null);
    }

    public void updateOfficerStatus(String officerId, String status, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("officer/updateStatus/{0}/{1}", new Object[]{officerId, status}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(jakarta.ws.rs.client.Entity.text(""), Response.class);
    }

    public void updateOfficerDepartment(String officerId, String departmentId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("officer/updateDepartment/{0}/{1}", new Object[]{officerId, departmentId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(jakarta.ws.rs.client.Entity.text(""), Response.class);
    }

    public void updateOfficerDesignation(String officerId, String designation, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("officer/updateDesignation/{0}/{1}", new Object[]{officerId, designation}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(jakarta.ws.rs.client.Entity.text(""), Response.class);
    }

    public <T> T getAssignedComplaintCount(Class<T> responseType, String officerId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("officer/assignedCount/{0}", new Object[]{officerId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T findOfficer(Class<T> responseType, String officerId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("officer/find/{0}", new Object[]{officerId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getOfficerById(Class<T> responseType, String officerId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("officer/getById/{0}", new Object[]{officerId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T searchOfficers(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("officer/search")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // SLA RULES (Token Required)
    // =========================================================

    public void addSlaRule(String categoryId, String maxDays, String status, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("addSlaRule/{0}/{1}/{2}", new Object[]{categoryId, maxDays, status}))
                .request()
                .header("Authorization", "Bearer " + token)
                .post(null);
    }

    public void updateSlaRule(String slaId, String maxDays, String status, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("updateSlaRule/{0}/{1}/{2}", new Object[]{slaId, maxDays, status}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(jakarta.ws.rs.client.Entity.text(""), Response.class);
    }

    public void deleteSlaRule(String slaId, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("deleteSlaRule/{0}", new Object[]{slaId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .delete();
    }

    public <T> T getAllSlaRules(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("getAllSlaRules")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // USER (Token Required except login/register)
    // =========================================================

    public <T> T getUserById(Class<T> responseType, String userId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("getUserById/{0}", new Object[]{userId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public Response updateUser(String userId, String fullName, String email, String mobile, String username, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("updateUser/{0}/{1}/{2}/{3}/{4}", new Object[]{userId, fullName, email, mobile, username}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(jakarta.ws.rs.client.Entity.text(""), Response.class);
    }

    public Response updateUserFull(String userId, String fullName, String email, String mobile, String username, String role, String status, String societyId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("updateUserFull/{0}/{1}/{2}/{3}/{4}/{5}/{6}/{7}", new Object[]{userId, fullName, email, mobile, username, role, status, societyId}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(jakarta.ws.rs.client.Entity.text(""), Response.class);
    }

    public void resetPassword(String userId, String password, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("resetPassword/{0}/{1}", new Object[]{userId, password}))
                .request()
                .header("Authorization", "Bearer " + token)
                .put(Entity.text(""));
    }

    public void submitFeedback(String complaintId, String rating, String comments, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("submitFeedback/{0}/{1}/{2}", new Object[]{complaintId, rating, comments}))
                .request()
                .header("Authorization", "Bearer " + token)
                .post(null);
    }

    public <T> T getAllUsers(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("getAllUsers")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T createUser(Object requestEntity, Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("createUser")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .post(Entity.entity(requestEntity, MediaType.APPLICATION_JSON), responseType);
    }

    public <T> T getAdminProfile(Class<T> responseType, String userId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("getAdminProfile/{0}", new Object[]{userId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getOfficerProfile(Class<T> responseType, String userId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("getOfficerProfile/{0}", new Object[]{userId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // COMPLAINT - CITIZEN (Token Required)
    // =========================================================

    public <T> T decodeQRCode(Class<T> responseType, String wardID, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("decodeQRCode/{0}", new Object[]{wardID}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public Response createComplaint(String userId, String categoryId, String societyId, String wardId,
            String title, String description, String status, String priority, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format(
                "createComplaint/{0}/{1}/{2}/{3}/{4}/{5}/{6}/{7}",
                new Object[]{userId, categoryId, societyId, wardId, title, description, status, priority}))
                .request()
                .header("Authorization", "Bearer " + token)
                .post(null, Response.class);
    }

    public <T> T getComplaintByUser(Class<T> responseType, String userId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("getComplaintByUser/{0}", new Object[]{userId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getAllComplaints(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("getAllComplaints")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getPendingComplaints(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("getPendingComplaints")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public void createComplaintReply(String complaintId, String repliedBy, String message, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("createComplaintReply/{0}/{1}/{2}", new Object[]{complaintId, repliedBy, message}))
                .request()
                .header("Authorization", "Bearer " + token)
                .post(null);
    }

    public <T> T getComplaintReplies(Class<T> responseType, String complaintId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("complaintReplies/{0}", new Object[]{complaintId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getHistory(Class<T> responseType, String complaintId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("complaintHistory/{0}", new Object[]{complaintId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T citizenNotifications(Class<T> responseType, String userId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("citizenNotifications/{0}", new Object[]{userId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getCitizenNotifications(Class<T> responseType, String userId, String token) throws ClientErrorException {
        return citizenNotifications(responseType, userId, token);
    }

    public <T> T totalComplaints(Class<T> responseType, String userId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("totalComplaints/{0}", new Object[]{userId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T assignedComplaints(Class<T> responseType, String userId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("assignedComplaints/{0}", new Object[]{userId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T resolvedComplaints(Class<T> responseType, String userId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("resolvedComplaints/{0}", new Object[]{userId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T rejectedComplaints(Class<T> responseType, String userId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("rejectedComplaints/{0}", new Object[]{userId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T recentComplaints(Class<T> responseType, String userId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("recentComplaints/{0}", new Object[]{userId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // WARD DASHBOARD (Token Required)
    // =========================================================

    public <T> T getComplaintsByWard(Class<T> responseType, String wardId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("ward/complaints/{0}", new Object[]{wardId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T wardTotalComplaints(Class<T> responseType, String wardId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("ward/totalComplaints/{0}", new Object[]{wardId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T wardPendingComplaints(Class<T> responseType, String wardId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("ward/pendingComplaints/{0}", new Object[]{wardId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T wardResolvedComplaints(Class<T> responseType, String wardId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("ward/resolvedComplaints/{0}", new Object[]{wardId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T wardRejectedComplaints(Class<T> responseType, String wardId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("ward/rejectedComplaints/{0}", new Object[]{wardId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getSocietiesByWard(Class<T> responseType, String wardId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("ward/societies/{0}", new Object[]{wardId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getCitizensByWard(Class<T> responseType, String wardId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("ward/citizens/{0}", new Object[]{wardId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getOfficersByWard(Class<T> responseType, String wardId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("ward/officers/{0}", new Object[]{wardId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getWardOfficers(Class<T> responseType, String wardId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("ward/wardOfficers/{0}", new Object[]{wardId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // ZONE DASHBOARD (Token Required)
    // =========================================================

    public <T> T zoneTotalComplaints(Class<T> responseType, String zoneId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("zone/totalComplaints/{0}", new Object[]{zoneId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T zonePendingComplaints(Class<T> responseType, String zoneId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("zone/pendingComplaints/{0}", new Object[]{zoneId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T zoneResolvedComplaints(Class<T> responseType, String zoneId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("zone/resolvedComplaints/{0}", new Object[]{zoneId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T zoneRejectedComplaints(Class<T> responseType, String zoneId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("zone/rejectedComplaints/{0}", new Object[]{zoneId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getComplaintsByZone(Class<T> responseType, String zoneId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("zone/complaints/{0}", new Object[]{zoneId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getWardsByZoneComplaint(Class<T> responseType, String zoneId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("zone/wards/{0}", new Object[]{zoneId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getSocietiesByZone(Class<T> responseType, String zoneId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("zone/societies/{0}", new Object[]{zoneId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getCitizensByZone(Class<T> responseType, String zoneId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("zone/citizens/{0}", new Object[]{zoneId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getOfficersByZone(Class<T> responseType, String zoneId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("zone/officers/{0}", new Object[]{zoneId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // OFFICER ACTIONS (Token Required)
    // =========================================================

    public <T> T getAssignedComplaint(Class<T> responseType, String officerId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("getAssignedComplaint/{0}", new Object[]{officerId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public void updateComplaintStatus(String complaintId, String status, String loggedInUser, String token) throws ClientErrorException {
        webTarget.path(MessageFormat.format("updateComplaintStatus/{0}/{1}/{2}", new Object[]{complaintId, status, loggedInUser}))
                .request()
                .header("Authorization", "Bearer " + token)
               .put(jakarta.ws.rs.client.Entity.text(""), Response.class);
    }

    public <T> T getComplaintByOfficer(Class<T> responseType, String officerId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("getComplaintByOfficer/{0}", new Object[]{officerId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // ADMIN COUNTS (Token Required)
    // =========================================================

    public <T> T adminTotalComplaints(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("admin/totalComplaints")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T adminOpenComplaints(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("admin/openComplaints")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T adminAssignedComplaints(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("admin/assignedComplaints")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T adminInProgressComplaints(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("admin/inProgressComplaints")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T adminResolvedComplaints(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("admin/resolvedComplaints")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T adminClosedComplaints(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("admin/closedComplaints")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T adminEscalatedComplaints(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("admin/escalatedComplaints")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // ADMIN CHARTS (Token Required)
    // =========================================================

    public <T> T zoneWiseComplaintCount(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("admin/zoneWiseComplaintCount")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T wardWiseComplaintCount(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("admin/wardWiseComplaintCount")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T departmentWiseComplaintCount(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("admin/departmentWiseComplaintCount")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T categoryWiseComplaintCount(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("admin/categoryWiseComplaintCount")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // MASTER DATA (Token Required)
    // =========================================================

    public <T> T getMasterZones(Class<T> responseType, String corporationId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("masterdata/zones/{0}", new Object[]{corporationId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getMasterWards(Class<T> responseType, String corporationId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("masterdata/wards/{0}", new Object[]{corporationId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getMasterCategories(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("masterdata/categories")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T getMasterDepartments(Class<T> responseType, String token) throws ClientErrorException {
        return webTarget.path("masterdata/departments")
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // CORPORATE DASHBOARD (Token Required)
    // =========================================================

    public <T> T corporateTotalComplaints(Class<T> responseType, String corporationId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("corporate/totalComplaints/{0}", new Object[]{corporationId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T corporateOpenComplaints(Class<T> responseType, String corporationId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("corporate/openComplaints/{0}", new Object[]{corporationId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T corporateAssignedComplaints(Class<T> responseType, String corporationId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("corporate/assignedComplaints/{0}", new Object[]{corporationId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T corporateResolvedComplaints(Class<T> responseType, String corporationId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("corporate/resolvedComplaints/{0}", new Object[]{corporationId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T corporateEscalatedComplaints(Class<T> responseType, String corporationId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("corporate/escalatedComplaints/{0}", new Object[]{corporationId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

   public WebTarget corporateFilterComplaintsTarget(
        String corporationId,
        Integer zoneId,
        Integer wardId,
        Integer categoryId,
        String status,
        String token) {

    WebTarget target = webTarget
            .path(java.text.MessageFormat.format(
                    "corporate/filterComplaints/{0}",
                    new Object[]{corporationId}));

    if (zoneId != null) {
        target = target.queryParam("zoneId", zoneId);
    }
    if (wardId != null) {
        target = target.queryParam("wardId", wardId);
    }
    if (categoryId != null) {
        target = target.queryParam("categoryId", categoryId);
    }
    if (status != null && !status.trim().isEmpty()) {
        target = target.queryParam("status", status);
    }

    return target;
}

    public <T> T corporateComplaintDetails(Class<T> responseType, String complaintId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("corporate/complaintDetails/{0}", new Object[]{complaintId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T corporateCitizenDetails(Class<T> responseType, String complaintId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("corporate/citizenDetails/{0}", new Object[]{complaintId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T corporateComplaintReplies(Class<T> responseType, String complaintId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("corporate/complaintReplies/{0}", new Object[]{complaintId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T corporateStatusHistory(Class<T> responseType, String complaintId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("corporate/statusHistory/{0}", new Object[]{complaintId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T corporateEscalationHistory(Class<T> responseType, String complaintId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("corporate/escalationHistory/{0}", new Object[]{complaintId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // OFFICER DASHBOARD (Token Required)
    // =========================================================

    public <T> T filterAssignedComplaintsAdvanced(Class<T> responseType, String officerId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("officer/dashboard/filterComplaints/{0}", new Object[]{officerId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }
    public <T> T filterAssignedComplaintsAdvanced(
        Class<T> responseType,
        String officerId,
        String complaintId,
        String categoryId,
        String status,
        String priority,
        String citizenName,
        Boolean overdueOnly,
        Boolean slaBreached,
        String complaintType,
        String token) {

    WebTarget resource = webTarget
            .path("officer/dashboard/filterComplaints")
            .path(officerId);

    if (complaintId != null && !complaintId.isEmpty()) {
        resource = resource.queryParam("complaintId", complaintId);
    }

    if (categoryId != null && !categoryId.isEmpty()) {
        resource = resource.queryParam("categoryId", categoryId);
    }

    if (status != null && !status.isEmpty()) {
        resource = resource.queryParam("status", status);
    }

    if (priority != null && !priority.isEmpty()) {
        resource = resource.queryParam("priority", priority);
    }

    if (citizenName != null && !citizenName.isEmpty()) {
        resource = resource.queryParam("citizenName", citizenName);
    }

    if (overdueOnly != null) {
        resource = resource.queryParam("overdueOnly", overdueOnly);
    }

    if (slaBreached != null) {
        resource = resource.queryParam("slaBreached", slaBreached);
    }

    if (complaintType != null && !complaintType.isEmpty()) {
        resource = resource.queryParam("complaintType", complaintType);
    }

    return resource
            .request(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer " + token)
            .get(responseType);
}

    public <T> T officerTotalAssigned(Class<T> responseType, String officerId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("officer/dashboard/totalAssigned/{0}", new Object[]{officerId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T officerPendingCount(Class<T> responseType, String officerId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("officer/dashboard/pendingCount/{0}", new Object[]{officerId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T officerInProgressCount(Class<T> responseType, String officerId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("officer/dashboard/inProgressCount/{0}", new Object[]{officerId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T officerResolvedCount(Class<T> responseType, String officerId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("officer/dashboard/resolvedCount/{0}", new Object[]{officerId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T officerOverdueCount(Class<T> responseType, String officerId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("officer/dashboard/overdueCount/{0}", new Object[]{officerId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T officerSlaWarningCount(Class<T> responseType, String officerId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("officer/dashboard/slaWarningCount/{0}", new Object[]{officerId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T officerTodayComplaints(Class<T> responseType, String officerId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("officer/dashboard/todayComplaints/{0}", new Object[]{officerId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T officerOverdueComplaints(Class<T> responseType, String officerId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("officer/dashboard/overdueComplaints/{0}", new Object[]{officerId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    public <T> T officerHighPriorityComplaints(Class<T> responseType, String officerId, String token) throws ClientErrorException {
        return webTarget.path(MessageFormat.format("officer/dashboard/highPriorityComplaints/{0}", new Object[]{officerId}))
                .request(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .get(responseType);
    }

    // =========================================================
    // CLOSE
    // =========================================================

    public void close() {
        client.close();
    }
}
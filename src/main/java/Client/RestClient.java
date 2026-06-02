/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/WebServices/JerseyClient.java to edit this template
 */
package Client;

import jakarta.ws.rs.ClientErrorException;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:JakartaEE10Resource
 * [jakartaee10]<br>
 * USAGE:
 * <pre>
 *        RestClient client = new RestClient();
 *        Object response = client.XXX(...);
 *        // do whatever with response
 *        client.close();
 * </pre>
 *
 * @author riya vesuwala
 */
public class RestClient {

    private WebTarget webTarget;
    private Client client;
    private static final String BASE_URI = "http://localhost:8080/GrievanceSystem/resources";

    public RestClient() {
        client = jakarta.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("jakartaee10");
    }

    public <T> T getAllSlaRules(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllSlaRules");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void resetPassword(String userId, String newPassword) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("resetPassword/{0}/{1}", new Object[]{userId, newPassword})).request().put(null);
    }

    public <T> T getUserById(Class<T> responseType, String userId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getUserById/{0}", new Object[]{userId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void createWard(String zoneId, String wardName, String status) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("createWard/{0}/{1}/{2}", new Object[]{zoneId, wardName, status})).request().post(null);
    }

    public <T> T decodeQRCode(Class<T> responseType, String wardID) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("decodeQRCode/{0}", new Object[]{wardID}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void createComplaint(String userId, String categoryId, String societyId, String wardId, String title, String description, String status, String priority) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("createComplaint/{0}/{1}/{2}/{3}/{4}/{5}/{6}/{7}", new Object[]{userId, categoryId, societyId, wardId, title, description, status, priority})).request().post(null);
    }

    public void deleteZone(String zoneId, String zoneName, String status, String corporationId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("deleteZone/{0}/{1}/{2}/{3}", new Object[]{zoneId, zoneName, status, corporationId})).request().delete();
    }

    public void updateCategory(String id, String name, String departmentId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateCategory/{0}/{1}/{2}", new Object[]{id, name, departmentId})).request().put(null);
    }

    public <T> T getComplaintByOfficer(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getComplaintByOfficer/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void updateZone(String zoneId, String zoneName, String status, String corporationId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateZone/{0}/{1}/{2}/{3}", new Object[]{zoneId, zoneName, status, corporationId})).request().put(null);
    }

    public void submitFeedback(String complaintId, String rating, String comments) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("submitFeedback/{0}/{1}/{2}", new Object[]{complaintId, rating, comments})).request().post(null);
    }

    public Response login(Object requestEntity) throws ClientErrorException {
        return webTarget.path("login").request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).post(jakarta.ws.rs.client.Entity.entity(requestEntity, jakarta.ws.rs.core.MediaType.APPLICATION_JSON), Response.class);
    }

    public void createOfficer(String userId, String departmentId, String zoneId, String wardId, String designation) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("createOfficer/{0}/{1}/{2}/{3}/{4}", new Object[]{userId, departmentId, zoneId, wardId, designation})).request().post(null);
    }

    public void updateSociety(String id, String name, String address, String status, String wardId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateSociety/{0}/{1}/{2}/{3}/{4}", new Object[]{id, name, address, status, wardId})).request().put(null);
    }

    public void addSlaRule(String categoryId, String maxDays) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("addSlaRule/{0}/{1}", new Object[]{categoryId, maxDays})).request().post(null);
    }

    public <T> T forgotPassword(Class<T> responseType, String username) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("forgotPassword/{0}", new Object[]{username})).request().post(null, responseType);
    }

    public <T> T getHistory(Class<T> responseType, String complaintId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("complaintHistory/{0}", new Object[]{complaintId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void createZone(String zoneName, String status, String corporationId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("createZone/{0}/{1}/{2}", new Object[]{zoneName, status, corporationId})).request().post(null);
    }

    public void deleteSlaRule(String slaId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("deleteSlaRule/{0}", new Object[]{slaId})).request().delete();
    }

    public <T> T getComplaintByUser(Class<T> responseType, String userId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getComplaintByUser/{0}", new Object[]{userId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }
    
    public <T> T getTotalComplaints(
        Class<T> responseType,
        String userId)
        throws ClientErrorException {

        WebTarget resource = webTarget;

        resource = resource.path(
                java.text.MessageFormat.format(
                        "totalComplaints/{0}",
                        userId));

        return resource.request(
                jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                .get(responseType);
    }
    
    public <T> T getAssignedComplaints(
        Class<T> responseType,
        String userId)
        throws ClientErrorException {

        WebTarget resource = webTarget;

        resource = resource.path(
                java.text.MessageFormat.format(
                        "assignedComplaints/{0}",
                        userId));

        return resource.request(
                jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                .get(responseType);
    }
    
    public <T> T getResolvedComplaints(
        Class<T> responseType,
        String userId)
        throws ClientErrorException {

        WebTarget resource = webTarget;

        resource = resource.path(
                java.text.MessageFormat.format(
                        "resolvedComplaints/{0}",
                        userId));

        return resource.request(
                jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                .get(responseType);
    }
    
    public <T> T getRejectedComplaints(
        Class<T> responseType,
        String userId)
        throws ClientErrorException {

        WebTarget resource = webTarget;

        resource = resource.path(
                java.text.MessageFormat.format(
                        "rejectedComplaints/{0}",
                        userId));

        return resource.request(
                jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                .get(responseType);
    }
    
    public <T> T getRecentComplaints(
        Class<T> responseType,
        String userId)
        throws ClientErrorException {

        WebTarget resource = webTarget;

        resource = resource.path(
                java.text.MessageFormat.format(
                        "recentComplaints/{0}",
                        userId));

        return resource.request(
                jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                .get(responseType);
    }

    public void createCategory(String categoryName, String departmentId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("createCategory/{0}/{1}", new Object[]{categoryName, departmentId})).request().post(null);
    }

    public void updateComplaintStatus(String complaintId, String status, String loggedInUser) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateComplaintStatus/{0}/{1}/{2}", new Object[]{complaintId, status, loggedInUser})).request().put(null);
    }

    public void deleteWard(String wardId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("deleteWard/{0}", new Object[]{wardId})).request().delete();
    }

    public <T> T getAllSocities(Class<T> responseType, String wardId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getSocietiesByWard/{0}", new Object[]{wardId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void updateOfficer(String officerId, String userId, String departmentId, String zoneId, String wardId, String designation) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateOfficer/{0}/{1}/{2}/{3}/{4}/{5}", new Object[]{officerId, userId, departmentId, zoneId, wardId, designation})).request().put(null);
    }

    public <T> T getAllCategories(Class<T> responseType) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path("getAllCategories");
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void deleteCategory(String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("deleteCategory/{0}", new Object[]{id})).request().delete();
    }

    public void createSociety(String wardId, String societyName, String address, String status) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("createSociety/{0}/{1}/{2}/{3}", new Object[]{wardId, societyName, address, status})).request().post(null);
    }

    public <T> T getAssignedComplaint(Class<T> responseType, String officerId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getAssignedComplaint/{0}", new Object[]{officerId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void updateSlaRule(String slaId, String maxDays) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateSlaRule/{0}/{1}", new Object[]{slaId, maxDays})).request().put(null);
    }

    public void updateDepartment(String id, String name, String desc, String status) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateDepartment/{0}/{1}/{2}/{3}", new Object[]{id, name, desc, status})).request().put(null);
    }

    public void createDepartment(String departmentName, String description, String status) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("createDepartment/{0}/{1}/{2}", new Object[]{departmentName, description, status})).request().post(null);
    }

    public void createComplaintReply(String complaintId, String repliedBy, String message) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("createComplaintReply/{0}/{1}/{2}", new Object[]{complaintId, repliedBy, message})).request().post(null);
    }

    public <T> T getOfficerProfile(Class<T> responseType, String userId) throws ClientErrorException {
        WebTarget resource = webTarget;
        resource = resource.path(java.text.MessageFormat.format("getOfficerProfile/{0}", new Object[]{userId}));
        return resource.request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON).get(responseType);
    }

    public void deleteOfficer(String officerId) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("deleteOfficer/{0}", new Object[]{officerId})).request().delete();
    }

    public void updateWard(String wardId, String zoneId, String wardName, String status) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("updateWard/{0}/{1}/{2}/{3}", new Object[]{wardId, zoneId, wardName, status})).request().put(null);
    }

    public Response registerUser(
            String fullname,
            String email,
            String mobile,
            String username,
            String password,
            String societyId)
            throws ClientErrorException {

        return webTarget.path(
                java.text.MessageFormat.format(
                        "registerUser/{0}/{1}/{2}/{3}/{4}/{5}",
                        new Object[]{
                            fullname,
                            email,
                            mobile,
                            username,
                            password,
                            societyId
                        }))
                .request()
                .post(null);
    }

    public void deleteDepartment(String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("deleteDepartment/{0}", new Object[]{id})).request().delete();
    }

    public void deleteSociety(String id) throws ClientErrorException {
        webTarget.path(java.text.MessageFormat.format("deleteSociety/{0}", new Object[]{id})).request().delete();
    }

    public void close() {
        client.close();
    }
    
}

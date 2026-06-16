package CDIBean;

import Client.RestClient;
import Entity.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named
@ViewScoped
public class CorporateDashboardCDI implements Serializable {

    private RestClient restClient = new RestClient();

    private Integer selectedZoneId;
    private Integer selectedWardId;
    private Integer selectedCategoryId;
    private String selectedStatus;

    private List<Complaint> complaints;
    private final Integer corporationId = 1;

    private Complaint selectedComplaint;
    private Users selectedCitizen;
    private List<ComplaintReply> complaintReplies;
    private List<ComplaintStatusHistory> statusHistory;
    private List<ComplaintEscalation> escalationHistory;

    // ── Stat counts ──────────────────────────────────────────
    private long totalComplaints;
    private long openComplaints;
    private long assignedComplaints;
    private long resolvedComplaints;
    private long escalatedComplaints;

    // ── Master data ──────────────────────────────────────────
    private List<Zone> zones;
    private List<Ward> wards;
    private List<ComplaintCategory> categories;

    // ─────────────────────────────────────────────────────────
    //  Helpers
    // ─────────────────────────────────────────────────────────

    private String getToken() {
        Map<String, Object> session = FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap();
        return (String) session.get("token");
    }

    // ─────────────────────────────────────────────────────────
    //  Init
    // ─────────────────────────────────────────────────────────

    @PostConstruct
    public void init() {
        loadStats();
        loadMasterData();
        loadComplaints(null, null, null, null);
    }

    // ─────────────────────────────────────────────────────────
    //  Load Statistics
    // ─────────────────────────────────────────────────────────

    private void loadStats() {

        String token = getToken();
        String corpId = String.valueOf(corporationId);

        // ── Total Complaints ─────────────────────────────────
        try {
            Response rs = restClient.corporateTotalComplaints(
                    Response.class, corpId, token);
            if (rs.getStatus() == 200) {
                totalComplaints = rs.readEntity(Long.class);
            } else {
                totalComplaints = 0L;
            }
        } catch (Exception e) {
            System.err.println("[CorporateDashboardCDI] totalComplaints error: "
                    + e.getMessage());
            totalComplaints = 0L;
        }

        // ── Open Complaints ──────────────────────────────────
        try {
            Response rs = restClient.corporateOpenComplaints(
                    Response.class, corpId, token);
            if (rs.getStatus() == 200) {
                openComplaints = rs.readEntity(Long.class);
            } else {
                openComplaints = 0L;
            }
        } catch (Exception e) {
            System.err.println("[CorporateDashboardCDI] openComplaints error: "
                    + e.getMessage());
            openComplaints = 0L;
        }

        // ── Assigned Complaints ──────────────────────────────
        try {
            Response rs = restClient.corporateAssignedComplaints(
                    Response.class, corpId, token);
            if (rs.getStatus() == 200) {
                assignedComplaints = rs.readEntity(Long.class);
            } else {
                assignedComplaints = 0L;
            }
        } catch (Exception e) {
            System.err.println("[CorporateDashboardCDI] assignedComplaints error: "
                    + e.getMessage());
            assignedComplaints = 0L;
        }

        // ── Resolved Complaints ──────────────────────────────
        try {
            Response rs = restClient.corporateResolvedComplaints(
                    Response.class, corpId, token);
            if (rs.getStatus() == 200) {
                resolvedComplaints = rs.readEntity(Long.class);
            } else {
                resolvedComplaints = 0L;
            }
        } catch (Exception e) {
            System.err.println("[CorporateDashboardCDI] resolvedComplaints error: "
                    + e.getMessage());
            resolvedComplaints = 0L;
        }

        // ── Escalated Complaints ─────────────────────────────
        try {
            Response rs = restClient.corporateEscalatedComplaints(
                    Response.class, corpId, token);
            if (rs.getStatus() == 200) {
                escalatedComplaints = rs.readEntity(Long.class);
            } else {
                escalatedComplaints = 0L;
            }
        } catch (Exception e) {
            System.err.println("[CorporateDashboardCDI] escalatedComplaints error: "
                    + e.getMessage());
            escalatedComplaints = 0L;
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Load Master Data (Zones / Wards / Categories)
    // ─────────────────────────────────────────────────────────

    private void loadMasterData() {

        String token  = getToken();
        String corpId = String.valueOf(corporationId);

        // ── Zones ────────────────────────────────────────────
        try {
            Response rs = restClient.getMasterZones(
                    Response.class, corpId, token);
            if (rs.getStatus() == 200) {
                zones = rs.readEntity(new GenericType<List<Zone>>() {});
            } else {
                zones = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("[CorporateDashboardCDI] zones error: "
                    + e.getMessage());
            zones = new ArrayList<>();
        }

        // ── Wards ────────────────────────────────────────────
        try {
            Response rs = restClient.getMasterWards(
                    Response.class, corpId, token);
            if (rs.getStatus() == 200) {
                wards = rs.readEntity(new GenericType<List<Ward>>() {});
            } else {
                wards = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("[CorporateDashboardCDI] wards error: "
                    + e.getMessage());
            wards = new ArrayList<>();
        }

        // ── Categories ───────────────────────────────────────
        try {
            Response rs = restClient.getMasterCategories(
                    Response.class, token);
            if (rs.getStatus() == 200) {
                categories = rs.readEntity(
                        new GenericType<List<ComplaintCategory>>() {});
            } else {
                categories = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("[CorporateDashboardCDI] categories error: "
                    + e.getMessage());
            categories = new ArrayList<>();
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Load / Filter Complaints
    // ─────────────────────────────────────────────────────────

    private void loadComplaints(Integer zoneId, Integer wardId,
            Integer categoryId, String status) {

        String token  = getToken();
        String corpId = String.valueOf(corporationId);

        try {
            // Build the target path with optional query params
            // The RestClient exposes corporateFilterComplaints; we extend it
            // inline here so we can append query parameters.
            jakarta.ws.rs.client.WebTarget target =
                    restClient.corporateFilterComplaintsTarget(
                            corpId, zoneId, wardId, categoryId, status, token);

            Response rs = target
                    .request(jakarta.ws.rs.core.MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + token)
                    .get();

            if (rs.getStatus() == 200) {
                complaints = rs.readEntity(
                        new GenericType<List<Complaint>>() {});
            } else {
                complaints = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("[CorporateDashboardCDI] filterComplaints error: "
                    + e.getMessage());
            complaints = new ArrayList<>();
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Public Actions
    // ─────────────────────────────────────────────────────────

    public void search() {
        loadComplaints(
                selectedZoneId,
                selectedWardId,
                selectedCategoryId,
                selectedStatus);
    }

    public void loadComplaintDetails(Integer complaintId) {

        String token = getToken();
        String cId   = String.valueOf(complaintId);

        // ── Complaint Details ────────────────────────────────
        try {
            Response rs = restClient.corporateComplaintDetails(
                    Response.class, cId, token);
            if (rs.getStatus() == 200) {
                selectedComplaint = rs.readEntity(Complaint.class);
            }
        } catch (Exception e) {
            System.err.println("[CorporateDashboardCDI] complaintDetails error: "
                    + e.getMessage());
        }

        // ── Citizen Details ──────────────────────────────────
        try {
            Response rs = restClient.corporateCitizenDetails(
                    Response.class, cId, token);
            if (rs.getStatus() == 200) {
                selectedCitizen = rs.readEntity(Users.class);
            }
        } catch (Exception e) {
            System.err.println("[CorporateDashboardCDI] citizenDetails error: "
                    + e.getMessage());
        }

        // ── Complaint Replies ────────────────────────────────
        try {
            Response rs = restClient.corporateComplaintReplies(
                    Response.class, cId, token);
            if (rs.getStatus() == 200) {
                complaintReplies = rs.readEntity(
                        new GenericType<List<ComplaintReply>>() {});
            } else {
                complaintReplies = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("[CorporateDashboardCDI] complaintReplies error: "
                    + e.getMessage());
            complaintReplies = new ArrayList<>();
        }

        // ── Status History ───────────────────────────────────
        try {
            Response rs = restClient.corporateStatusHistory(
                    Response.class, cId, token);
            if (rs.getStatus() == 200) {
                statusHistory = rs.readEntity(
                        new GenericType<List<ComplaintStatusHistory>>() {});
            } else {
                statusHistory = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("[CorporateDashboardCDI] statusHistory error: "
                    + e.getMessage());
            statusHistory = new ArrayList<>();
        }

        // ── Escalation History ───────────────────────────────
        try {
            Response rs = restClient.corporateEscalationHistory(
                    Response.class, cId, token);
            if (rs.getStatus() == 200) {
                escalationHistory = rs.readEntity(
                        new GenericType<List<ComplaintEscalation>>() {});
            } else {
                escalationHistory = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("[CorporateDashboardCDI] escalationHistory error: "
                    + e.getMessage());
            escalationHistory = new ArrayList<>();
        }
    }

    public String viewComplaint(Integer complaintId) {
        return "/complaintDetails.xhtml?faces-redirect=true&complaintId="
                + complaintId;
    }

    // ─────────────────────────────────────────────────────────
    //  Getters / Setters
    // ─────────────────────────────────────────────────────────

    public long getTotalComplaints()      { return totalComplaints; }
    public long getOpenComplaints()       { return openComplaints; }
    public long getAssignedComplaints()   { return assignedComplaints; }
    public long getResolvedComplaints()   { return resolvedComplaints; }
    public long getEscalatedComplaints()  { return escalatedComplaints; }

    public List<Zone> getZones()                    { return zones; }
    public List<Ward> getWards()                    { return wards; }
    public List<ComplaintCategory> getCategories()  { return categories; }

    public List<Complaint> getComplaints()          { return complaints; }
    public void setComplaints(List<Complaint> c)    { this.complaints = c; }

    public Complaint getSelectedComplaint()         { return selectedComplaint; }
    public void setSelectedComplaint(Complaint c)   { this.selectedComplaint = c; }

    public Users getSelectedCitizen()               { return selectedCitizen; }
    public void setSelectedCitizen(Users u)         { this.selectedCitizen = u; }

    public List<ComplaintReply> getComplaintReplies()             { return complaintReplies; }
    public void setComplaintReplies(List<ComplaintReply> r)       { this.complaintReplies = r; }

    public List<ComplaintStatusHistory> getStatusHistory()        { return statusHistory; }
    public void setStatusHistory(List<ComplaintStatusHistory> h)  { this.statusHistory = h; }

    public List<ComplaintEscalation> getEscalationHistory()       { return escalationHistory; }
    public void setEscalationHistory(List<ComplaintEscalation> e) { this.escalationHistory = e; }

    public Integer getSelectedZoneId()              { return selectedZoneId; }
    public void setSelectedZoneId(Integer id)       { this.selectedZoneId = id; }

    public Integer getSelectedWardId()              { return selectedWardId; }
    public void setSelectedWardId(Integer id)       { this.selectedWardId = id; }

    public Integer getSelectedCategoryId()          { return selectedCategoryId; }
    public void setSelectedCategoryId(Integer id)   { this.selectedCategoryId = id; }

    public String getSelectedStatus()               { return selectedStatus; }
    public void setSelectedStatus(String s)         { this.selectedStatus = s; }
}
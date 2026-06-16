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
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Named(value = "wardAdminBean")
@ViewScoped
public class WardAdminBean implements Serializable {

    private RestClient restClient = new RestClient();

    private Integer wardId;

    private Collection<Complaint> complaints;
    private Collection<Society>   societies;
    private Collection<Users>     citizens;
    private Collection<Officers>  officers;
    private Collection<Ward>      zoneWards;

    private Long totalComplaints;
    private Long pendingComplaints;
    private Long resolvedComplaints;
    private Long rejectedComplaints;

    private Users  wardAdmin;
    private String activeTab = "complaints";

    private Integer selectedOfficerId;
    private Integer selectedWardId;

    // ─────────────────────────────────────────────────────────
    //  Session Helpers
    // ─────────────────────────────────────────────────────────

    private Map<String, Object> getSession() {
        return FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap();
    }

    private String getToken() {
        String token = (String) getSession().get("token");
        System.out.println("[WardAdminBean] token = " + token);
        return token;
    }

    private Users getLoggedInUser() {
        return (Users) getSession().get("loggedInUser");
    }

    private String getLoggedInUserId() {
        Users user = getLoggedInUser();
        if (user == null) {
            System.err.println("[WardAdminBean] loggedInUser is NULL.");
            return null;
        }
        return String.valueOf(user.getUserId());
    }

    /**
     * Navigate: Users -> societyId -> wardId -> wardId (Integer)
     * Falls back to 1 if any step is null.
     */
    private Integer getLoggedInWardId() {
        try {
            Users user = getLoggedInUser();
            if (user == null) {
                System.err.println("[WardAdminBean] loggedInUser is NULL."
                        + " Falling back to wardId=1.");
                return 1;
            }

            Society society = user.getSocietyId();
            if (society == null) {
                System.err.println("[WardAdminBean] user.societyId is NULL."
                        + " Falling back to wardId=1.");
                return 1;
            }

            Ward ward = society.getWardId();
            if (ward == null) {
                System.err.println("[WardAdminBean] society.wardId is NULL."
                        + " Falling back to wardId=1.");
                return 1;
            }

            Integer extractedWardId = ward.getWardId();
            System.out.println("[WardAdminBean] Extracted wardId = "
                    + extractedWardId);
            return extractedWardId;

        } catch (Exception e) {
            System.err.println("[WardAdminBean] getLoggedInWardId error: "
                    + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    /**
     * Navigate: Users -> societyId -> wardId -> zoneId -> zoneId (Integer)
     * Falls back to 1 if any step is null.
     */
    private Integer getLoggedInZoneId() {
        try {
            Users user = getLoggedInUser();
            if (user == null) {
                System.err.println("[WardAdminBean] loggedInUser is NULL."
                        + " Falling back to zoneId=1.");
                return 1;
            }

            Society society = user.getSocietyId();
            if (society == null) {
                System.err.println("[WardAdminBean] user.societyId is NULL."
                        + " Falling back to zoneId=1.");
                return 1;
            }

            Ward ward = society.getWardId();
            if (ward == null) {
                System.err.println("[WardAdminBean] society.wardId is NULL."
                        + " Falling back to zoneId=1.");
                return 1;
            }

            Zone zone = ward.getZoneId();
            if (zone == null) {
                System.err.println("[WardAdminBean] ward.zoneId is NULL."
                        + " Falling back to zoneId=1.");
                return 1;
            }

            Integer extractedZoneId = zone.getZoneId();
            System.out.println("[WardAdminBean] Extracted zoneId = "
                    + extractedZoneId);
            return extractedZoneId;

        } catch (Exception e) {
            System.err.println("[WardAdminBean] getLoggedInZoneId error: "
                    + e.getMessage());
            e.printStackTrace();
            return 1;
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Init
    // ─────────────────────────────────────────────────────────

    @PostConstruct
    public void init() {

        System.out.println("[WardAdminBean] init() called.");

        try {
            // ── Resolve wardId from session ──────────────────
            wardId = getLoggedInWardId();
            System.out.println("[WardAdminBean] wardId resolved = "
                    + wardId);

            // ── Load all data ────────────────────────────────
            loadProfile();
            loadDashboard();
            loadComplaints();
            loadSocieties();
            loadCitizens();
            loadOfficers();
            loadZoneWards();

        } catch (Exception e) {
            System.err.println("[WardAdminBean] init() error: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Load Profile
    // ─────────────────────────────────────────────────────────

    public void loadProfile() {

        String token  = getToken();
        String userId = getLoggedInUserId();

        if (userId == null) {
            System.err.println("[WardAdminBean] Cannot load profile:"
                    + " userId is null.");
            return;
        }

        if (token == null) {
            System.err.println("[WardAdminBean] Cannot load profile:"
                    + " token is null.");
            return;
        }

        try {
            Response rs = restClient.getUserById(
                    Response.class, userId, token);

            if (rs.getStatus() == 200) {
                wardAdmin = rs.readEntity(Users.class);
                System.out.println("[WardAdminBean] Profile loaded: "
                        + wardAdmin.getFullName());
            } else {
                System.err.println("[WardAdminBean] loadProfile failed."
                        + " Status: " + rs.getStatus());
            }

        } catch (Exception e) {
            System.err.println("[WardAdminBean] loadProfile error: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Load Dashboard Stats
    // ─────────────────────────────────────────────────────────

    public void loadDashboard() {

        String token = getToken();
        String wId   = String.valueOf(wardId);

        // ── Total ────────────────────────────────────────────
        try {
            Response rs = restClient.wardTotalComplaints(
                    Response.class, wId, token);
            totalComplaints = rs.getStatus() == 200
                    ? rs.readEntity(Long.class) : 0L;
        } catch (Exception e) {
            System.err.println("[WardAdminBean] totalComplaints error: "
                    + e.getMessage());
            totalComplaints = 0L;
        }

        // ── Pending ──────────────────────────────────────────
        try {
            Response rs = restClient.wardPendingComplaints(
                    Response.class, wId, token);
            pendingComplaints = rs.getStatus() == 200
                    ? rs.readEntity(Long.class) : 0L;
        } catch (Exception e) {
            System.err.println("[WardAdminBean] pendingComplaints error: "
                    + e.getMessage());
            pendingComplaints = 0L;
        }

        // ── Resolved ─────────────────────────────────────────
        try {
            Response rs = restClient.wardResolvedComplaints(
                    Response.class, wId, token);
            resolvedComplaints = rs.getStatus() == 200
                    ? rs.readEntity(Long.class) : 0L;
        } catch (Exception e) {
            System.err.println("[WardAdminBean] resolvedComplaints error: "
                    + e.getMessage());
            resolvedComplaints = 0L;
        }

        // ── Rejected ─────────────────────────────────────────
        try {
            Response rs = restClient.wardRejectedComplaints(
                    Response.class, wId, token);
            rejectedComplaints = rs.getStatus() == 200
                    ? rs.readEntity(Long.class) : 0L;
        } catch (Exception e) {
            System.err.println("[WardAdminBean] rejectedComplaints error: "
                    + e.getMessage());
            rejectedComplaints = 0L;
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Load Complaints
    // ─────────────────────────────────────────────────────────

    public void loadComplaints() {

        String token = getToken();
        String wId   = String.valueOf(wardId);

        try {
            Response rs = restClient.getComplaintsByWard(
                    Response.class, wId, token);

            if (rs.getStatus() == 200) {
                complaints = rs.readEntity(
                        new GenericType<List<Complaint>>() {});
                System.out.println("[WardAdminBean] Complaints loaded: "
                        + complaints.size());
            } else {
                System.err.println("[WardAdminBean] loadComplaints failed."
                        + " Status: " + rs.getStatus());
                complaints = new ArrayList<>();
            }

        } catch (Exception e) {
            System.err.println("[WardAdminBean] loadComplaints error: "
                    + e.getMessage());
            e.printStackTrace();
            complaints = new ArrayList<>();
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Load Societies
    // ─────────────────────────────────────────────────────────

    public void loadSocieties() {

        String token = getToken();
        String wId   = String.valueOf(wardId);

        try {
            Response rs = restClient.getSocietiesByWard(
                    Response.class, wId, token);

            if (rs.getStatus() == 200) {
                societies = rs.readEntity(
                        new GenericType<List<Society>>() {});
                System.out.println("[WardAdminBean] Societies loaded: "
                        + societies.size());
            } else {
                System.err.println("[WardAdminBean] loadSocieties failed."
                        + " Status: " + rs.getStatus());
                societies = new ArrayList<>();
            }

        } catch (Exception e) {
            System.err.println("[WardAdminBean] loadSocieties error: "
                    + e.getMessage());
            e.printStackTrace();
            societies = new ArrayList<>();
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Load Citizens
    // ─────────────────────────────────────────────────────────

    public void loadCitizens() {

        String token = getToken();
        String wId   = String.valueOf(wardId);

        try {
            Response rs = restClient.getCitizensByWard(
                    Response.class, wId, token);

            if (rs.getStatus() == 200) {
                citizens = rs.readEntity(
                        new GenericType<List<Users>>() {});
                System.out.println("[WardAdminBean] Citizens loaded: "
                        + citizens.size());
            } else {
                System.err.println("[WardAdminBean] loadCitizens failed."
                        + " Status: " + rs.getStatus());
                citizens = new ArrayList<>();
            }

        } catch (Exception e) {
            System.err.println("[WardAdminBean] loadCitizens error: "
                    + e.getMessage());
            e.printStackTrace();
            citizens = new ArrayList<>();
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Load Officers
    // ─────────────────────────────────────────────────────────

    public void loadOfficers() {

        String token = getToken();
        String wId   = String.valueOf(wardId);

        try {
            Response rs = restClient.getOfficersByWard(
                    Response.class, wId, token);

            if (rs.getStatus() == 200) {
                officers = rs.readEntity(
                        new GenericType<List<Officers>>() {});
                System.out.println("[WardAdminBean] Officers loaded: "
                        + officers.size());
            } else {
                System.err.println("[WardAdminBean] loadOfficers failed."
                        + " Status: " + rs.getStatus());
                officers = new ArrayList<>();
            }

        } catch (Exception e) {
            System.err.println("[WardAdminBean] loadOfficers error: "
                    + e.getMessage());
            e.printStackTrace();
            officers = new ArrayList<>();
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Load Zone Wards (for Officer Transfer Dialog)
    // ─────────────────────────────────────────────────────────

    public void loadZoneWards() {

        String token  = getToken();
        String zoneId = String.valueOf(getLoggedInZoneId());

        try {
            Response rs = restClient.getWardsByZone(
                    Response.class, zoneId, token);

            if (rs.getStatus() == 200) {
                zoneWards = rs.readEntity(
                        new GenericType<List<Ward>>() {});
                System.out.println("[WardAdminBean] Zone wards loaded: "
                        + zoneWards.size());
            } else {
                System.err.println("[WardAdminBean] loadZoneWards failed."
                        + " Status: " + rs.getStatus());
                zoneWards = new ArrayList<>();
            }

        } catch (Exception e) {
            System.err.println("[WardAdminBean] loadZoneWards error: "
                    + e.getMessage());
            e.printStackTrace();
            zoneWards = new ArrayList<>();
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Activate / Deactivate Society
    // ─────────────────────────────────────────────────────────

    public void activateSociety(Integer societyId) {

        String token = getToken();

        Society s = findSociety(societyId);

        if (s == null) {
            System.err.println("[WardAdminBean] activateSociety:"
                    + " society not found. id=" + societyId);
            return;
        }

        try {
            restClient.updateSociety(
                    String.valueOf(s.getSocietyId()),
                    s.getSocietyName(),
                    s.getAddress(),
                    "ACTIVE",
                    String.valueOf(s.getWardId().getWardId()),
                    token);

            System.out.println("[WardAdminBean] Society activated. id="
                    + societyId);
            loadSocieties();

        } catch (Exception e) {
            System.err.println("[WardAdminBean] activateSociety error: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    public void deactivateSociety(Integer societyId) {

        String token = getToken();

        Society s = findSociety(societyId);

        if (s == null) {
            System.err.println("[WardAdminBean] deactivateSociety:"
                    + " society not found. id=" + societyId);
            return;
        }

        try {
            restClient.updateSociety(
                    String.valueOf(s.getSocietyId()),
                    s.getSocietyName(),
                    s.getAddress(),
                    "INACTIVE",
                    String.valueOf(s.getWardId().getWardId()),
                    token);

            System.out.println("[WardAdminBean] Society deactivated. id="
                    + societyId);
            loadSocieties();

        } catch (Exception e) {
            System.err.println("[WardAdminBean] deactivateSociety error: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    private Society findSociety(Integer societyId) {
        if (societies == null) return null;
        return societies.stream()
                .filter(s -> s.getSocietyId().equals(societyId))
                .findFirst()
                .orElse(null);
    }

    // ─────────────────────────────────────────────────────────
    //  Prepare Officer Transfer
    // ─────────────────────────────────────────────────────────

    public void prepareOfficerTransfer(Integer officerId) {

        selectedOfficerId = officerId;

        if (officers != null) {
            officers.stream()
                    .filter(o -> o.getOfficerId().equals(officerId))
                    .findFirst()
                    .ifPresent(officer -> {
                        if (officer.getWardId() != null) {
                            selectedWardId = officer.getWardId()
                                    .getWardId();
                            System.out.println(
                                    "[WardAdminBean] Transfer prepared."
                                    + " officerId=" + officerId
                                    + ", currentWardId=" + selectedWardId);
                        }
                    });
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Change Officer Ward
    // ─────────────────────────────────────────────────────────

    public void changeOfficerWard() {

        String token = getToken();

        if (selectedOfficerId == null || selectedWardId == null) {
            System.err.println("[WardAdminBean] changeOfficerWard:"
                    + " officerId or wardId is null.");
            return;
        }

        try {
            restClient.changeOfficerWard(
                    String.valueOf(selectedOfficerId),
                    String.valueOf(selectedWardId),
                    token);

            System.out.println("[WardAdminBean] Officer ward updated."
                    + " officerId=" + selectedOfficerId
                    + ", newWardId=" + selectedWardId);

            loadOfficers();

        } catch (Exception e) {
            System.err.println("[WardAdminBean] changeOfficerWard error: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Get Selected Officer
    // ─────────────────────────────────────────────────────────

    public Officers getSelectedOfficer() {
        if (selectedOfficerId == null || officers == null) return null;
        return officers.stream()
                .filter(o -> o.getOfficerId().equals(selectedOfficerId))
                .findFirst()
                .orElse(null);
    }

    // ─────────────────────────────────────────────────────────
    //  View Complaint Navigation
    // ─────────────────────────────────────────────────────────

    public String viewComplaint(Integer complaintId) {
        System.out.println("[WardAdminBean] viewComplaint id="
                + complaintId);
        return "/complaintDetails.xhtml?faces-redirect=true&complaintId="
                + complaintId;
    }

    // ─────────────────────────────────────────────────────────
    //  Refresh
    // ─────────────────────────────────────────────────────────

    public void refresh() {
        loadDashboard();
        loadComplaints();
        loadSocieties();
        loadCitizens();
        loadOfficers();
        loadZoneWards();
    }

    // ─────────────────────────────────────────────────────────
    //  Tab
    // ─────────────────────────────────────────────────────────

    public void setTab(String tab) {
        System.out.println("[WardAdminBean] setTab: " + tab);
        this.activeTab = tab;
    }

    // ─────────────────────────────────────────────────────────
    //  Getters / Setters
    // ─────────────────────────────────────────────────────────

    public String getActiveTab()                              { return activeTab; }

    public Collection<Complaint> getComplaints()              { return complaints; }
    public void setComplaints(Collection<Complaint> c)        { this.complaints = c; }

    public Collection<Society> getSocieties()                 { return societies; }
    public void setSocieties(Collection<Society> s)           { this.societies = s; }

    public Collection<Users> getCitizens()                    { return citizens; }
    public void setCitizens(Collection<Users> c)              { this.citizens = c; }

    public Collection<Officers> getOfficers()                 { return officers; }
    public void setOfficers(Collection<Officers> o)           { this.officers = o; }

    public Collection<Ward> getZoneWards()                    { return zoneWards; }
    public void setZoneWards(Collection<Ward> w)              { this.zoneWards = w; }

    public Long getTotalComplaints()                          { return totalComplaints; }
    public void setTotalComplaints(Long t)                    { this.totalComplaints = t; }

    public Long getPendingComplaints()                        { return pendingComplaints; }
    public void setPendingComplaints(Long p)                  { this.pendingComplaints = p; }

    public Long getResolvedComplaints()                       { return resolvedComplaints; }
    public void setResolvedComplaints(Long r)                 { this.resolvedComplaints = r; }

    public Long getRejectedComplaints()                       { return rejectedComplaints; }
    public void setRejectedComplaints(Long r)                 { this.rejectedComplaints = r; }

    public Integer getWardId()                                { return wardId; }
    public void setWardId(Integer wardId)                     { this.wardId = wardId; }

    public Integer getSelectedOfficerId()                     { return selectedOfficerId; }
    public void setSelectedOfficerId(Integer id)              { this.selectedOfficerId = id; }

    public Integer getSelectedWardId()                        { return selectedWardId; }
    public void setSelectedWardId(Integer id)                 { this.selectedWardId = id; }

    public Users getWardAdmin()                               { return wardAdmin; }
    public void setWardAdmin(Users u)                         { this.wardAdmin = u; }
}
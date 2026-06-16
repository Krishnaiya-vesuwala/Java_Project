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

@Named(value = "zoneAdminBean")
@ViewScoped
public class ZoneAdminBean implements Serializable {

    private RestClient restClient = new RestClient();

    private Integer zoneId;

    private Collection<Complaint> complaints;
    private Collection<Ward>      wards;
    private Collection<Society>   societies;
    private Collection<Users>     citizens;
    private Collection<Officers>  officers;
    private Collection<Zone>      zones;

    private Long totalComplaints;
    private Long pendingComplaints;
    private Long resolvedComplaints;
    private Long rejectedComplaints;

    private Users  zoneAdmin;
    private String activeTab = "complaints";

    private Integer selectedOfficerId;
    private Integer selectedZoneId;

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
        System.out.println("[ZoneAdminBean] token = " + token);
        return token;
    }

    private Users getLoggedInUser() {
        return (Users) getSession().get("loggedInUser");
    }

    // ─────────────────────────────────────────────────────────
    //  Extract IDs safely from session user
    // ─────────────────────────────────────────────────────────

    /**
     * Returns the userId of the logged-in user as String.
     * Returns null if not available.
     */
    private String getLoggedInUserId() {
        Users user = getLoggedInUser();
        if (user == null) {
            System.err.println("[ZoneAdminBean] loggedInUser is NULL in session.");
            return null;
        }
        System.out.println("[ZoneAdminBean] loggedInUser.userId = "
                + user.getUserId());
        return String.valueOf(user.getUserId());
    }

    /**
     * Returns the zoneId of the logged-in user by navigating:
     * Users -> societyId -> wardId -> zoneId -> zoneId (Integer)
     *
     * Falls back to "1" if any navigation step is null.
     */
    private Integer getLoggedInZoneId() {

        try {
            Users user = getLoggedInUser();

            if (user == null) {
                System.err.println("[ZoneAdminBean] loggedInUser is NULL."
                        + " Falling back to zoneId=1.");
                return 1;
            }

            // ── Path: user -> society -> ward -> zone -> zoneId ──
            Society society = user.getSocietyId();
            if (society == null) {
                System.err.println("[ZoneAdminBean] user.societyId is NULL."
                        + " Falling back to zoneId=1.");
                return 1;
            }

            Ward ward = society.getWardId();
            if (ward == null) {
                System.err.println("[ZoneAdminBean] society.wardId is NULL."
                        + " Falling back to zoneId=1.");
                return 1;
            }

            Zone zone = ward.getZoneId();
            if (zone == null) {
                System.err.println("[ZoneAdminBean] ward.zoneId is NULL."
                        + " Falling back to zoneId=1.");
                return 1;
            }

            Integer extractedZoneId = zone.getZoneId(); // ← correct: get the Integer
            System.out.println("[ZoneAdminBean] Extracted zoneId = "
                    + extractedZoneId);
            return extractedZoneId;

        } catch (Exception e) {
            System.err.println("[ZoneAdminBean] getLoggedInZoneId error: "
                    + e.getMessage());
            e.printStackTrace();
            return 1; // safe fallback
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Init
    // ─────────────────────────────────────────────────────────

    @PostConstruct
    public void init() {

        System.out.println("[ZoneAdminBean] init() called.");

        try {
            // ── Resolve zoneId from session ──────────────────
            zoneId = getLoggedInZoneId(); // returns Integer directly
            System.out.println("[ZoneAdminBean] zoneId resolved = " + zoneId);

            // ── Load all data ────────────────────────────────
            loadProfile();
            loadDashboard();
            loadComplaints();
            loadWards();
            loadSocieties();
            loadCitizens();
            loadOfficers();
            loadZones();

        } catch (Exception e) {
            System.err.println("[ZoneAdminBean] init() error: "
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
            System.err.println("[ZoneAdminBean] Cannot load profile:"
                    + " userId is null.");
            return;
        }

        if (token == null) {
            System.err.println("[ZoneAdminBean] Cannot load profile:"
                    + " token is null.");
            return;
        }

        try {
            Response rs = restClient.getUserById(
                    Response.class, userId, token);

            if (rs.getStatus() == 200) {
                zoneAdmin = rs.readEntity(Users.class);
                System.out.println("[ZoneAdminBean] Profile loaded: "
                        + zoneAdmin.getFullName());
            } else {
                System.err.println("[ZoneAdminBean] loadProfile failed."
                        + " Status: " + rs.getStatus());
            }

        } catch (Exception e) {
            System.err.println("[ZoneAdminBean] loadProfile error: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Load Dashboard Stats
    // ─────────────────────────────────────────────────────────

    public void loadDashboard() {

        String token = getToken();
        String zId   = String.valueOf(zoneId);

        // ── Total ────────────────────────────────────────────
        try {
            Response rs = restClient.zoneTotalComplaints(
                    Response.class, zId, token);
            totalComplaints = rs.getStatus() == 200
                    ? rs.readEntity(Long.class) : 0L;
        } catch (Exception e) {
            System.err.println("[ZoneAdminBean] totalComplaints error: "
                    + e.getMessage());
            totalComplaints = 0L;
        }

        // ── Pending ──────────────────────────────────────────
        try {
            Response rs = restClient.zonePendingComplaints(
                    Response.class, zId, token);
            pendingComplaints = rs.getStatus() == 200
                    ? rs.readEntity(Long.class) : 0L;
        } catch (Exception e) {
            System.err.println("[ZoneAdminBean] pendingComplaints error: "
                    + e.getMessage());
            pendingComplaints = 0L;
        }

        // ── Resolved ─────────────────────────────────────────
        try {
            Response rs = restClient.zoneResolvedComplaints(
                    Response.class, zId, token);
            resolvedComplaints = rs.getStatus() == 200
                    ? rs.readEntity(Long.class) : 0L;
        } catch (Exception e) {
            System.err.println("[ZoneAdminBean] resolvedComplaints error: "
                    + e.getMessage());
            resolvedComplaints = 0L;
        }

        // ── Rejected ─────────────────────────────────────────
        try {
            Response rs = restClient.zoneRejectedComplaints(
                    Response.class, zId, token);
            rejectedComplaints = rs.getStatus() == 200
                    ? rs.readEntity(Long.class) : 0L;
        } catch (Exception e) {
            System.err.println("[ZoneAdminBean] rejectedComplaints error: "
                    + e.getMessage());
            rejectedComplaints = 0L;
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Load Complaints
    // ─────────────────────────────────────────────────────────

    public void loadComplaints() {

        String token = getToken();
        String zId   = String.valueOf(zoneId);

        try {
            Response rs = restClient.getComplaintsByZone(
                    Response.class, zId, token);

            if (rs.getStatus() == 200) {
                complaints = rs.readEntity(
                        new GenericType<List<Complaint>>() {});
                System.out.println("[ZoneAdminBean] Complaints loaded: "
                        + complaints.size());
            } else {
                System.err.println("[ZoneAdminBean] loadComplaints failed."
                        + " Status: " + rs.getStatus());
                complaints = new ArrayList<>();
            }

        } catch (Exception e) {
            System.err.println("[ZoneAdminBean] loadComplaints error: "
                    + e.getMessage());
            e.printStackTrace();
            complaints = new ArrayList<>();
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Load Wards
    // ─────────────────────────────────────────────────────────

    public void loadWards() {

        String token = getToken();
        String zId   = String.valueOf(zoneId);

        try {
            Response rs = restClient.getWardsByZoneComplaint(
                    Response.class, zId, token);

            if (rs.getStatus() == 200) {
                wards = rs.readEntity(
                        new GenericType<List<Ward>>() {});
                System.out.println("[ZoneAdminBean] Wards loaded: "
                        + wards.size());
            } else {
                System.err.println("[ZoneAdminBean] loadWards failed."
                        + " Status: " + rs.getStatus());
                wards = new ArrayList<>();
            }

        } catch (Exception e) {
            System.err.println("[ZoneAdminBean] loadWards error: "
                    + e.getMessage());
            e.printStackTrace();
            wards = new ArrayList<>();
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Load Societies
    // ─────────────────────────────────────────────────────────

    public void loadSocieties() {

        String token = getToken();
        String zId   = String.valueOf(zoneId);

        try {
            Response rs = restClient.getSocietiesByZone(
                    Response.class, zId, token);

            if (rs.getStatus() == 200) {
                societies = rs.readEntity(
                        new GenericType<List<Society>>() {});
                System.out.println("[ZoneAdminBean] Societies loaded: "
                        + societies.size());
            } else {
                System.err.println("[ZoneAdminBean] loadSocieties failed."
                        + " Status: " + rs.getStatus());
                societies = new ArrayList<>();
            }

        } catch (Exception e) {
            System.err.println("[ZoneAdminBean] loadSocieties error: "
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
        String zId   = String.valueOf(zoneId);

        try {
            Response rs = restClient.getCitizensByZone(
                    Response.class, zId, token);

            if (rs.getStatus() == 200) {
                citizens = rs.readEntity(
                        new GenericType<List<Users>>() {});
                System.out.println("[ZoneAdminBean] Citizens loaded: "
                        + citizens.size());
            } else {
                System.err.println("[ZoneAdminBean] loadCitizens failed."
                        + " Status: " + rs.getStatus());
                citizens = new ArrayList<>();
            }

        } catch (Exception e) {
            System.err.println("[ZoneAdminBean] loadCitizens error: "
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
        String zId   = String.valueOf(zoneId);

        try {
            Response rs = restClient.getOfficersByZone(
                    Response.class, zId, token);

            if (rs.getStatus() == 200) {
                officers = rs.readEntity(
                        new GenericType<List<Officers>>() {});
                System.out.println("[ZoneAdminBean] Officers loaded: "
                        + officers.size());
            } else {
                System.err.println("[ZoneAdminBean] loadOfficers failed."
                        + " Status: " + rs.getStatus());
                officers = new ArrayList<>();
            }

        } catch (Exception e) {
            System.err.println("[ZoneAdminBean] loadOfficers error: "
                    + e.getMessage());
            e.printStackTrace();
            officers = new ArrayList<>();
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Load Zones
    // ─────────────────────────────────────────────────────────

    public void loadZones() {

        String token = getToken();

        try {
            Response rs = restClient.getAllZones(
                    Response.class, token);

            if (rs.getStatus() == 200) {
                zones = rs.readEntity(
                        new GenericType<List<Zone>>() {});
                System.out.println("[ZoneAdminBean] Zones loaded: "
                        + zones.size());
            } else {
                System.err.println("[ZoneAdminBean] loadZones failed."
                        + " Status: " + rs.getStatus());
                zones = new ArrayList<>();
            }

        } catch (Exception e) {
            System.err.println("[ZoneAdminBean] loadZones error: "
                    + e.getMessage());
            e.printStackTrace();
            zones = new ArrayList<>();
        }
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
                        if (officer.getWardId() != null
                                && officer.getWardId().getZoneId() != null) {
                            selectedZoneId = officer.getWardId()
                                    .getZoneId()
                                    .getZoneId(); // ← Integer, not Zone object
                            System.out.println(
                                    "[ZoneAdminBean] Transfer prepared."
                                    + " officerId=" + officerId
                                    + ", currentZoneId=" + selectedZoneId);
                        }
                    });
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Change Officer Zone
    // ─────────────────────────────────────────────────────────

    public void changeOfficerZone() {

        String token = getToken();

        if (selectedOfficerId == null || selectedZoneId == null) {
            System.err.println("[ZoneAdminBean] changeOfficerZone:"
                    + " officerId or zoneId is null.");
            return;
        }

        try {
            restClient.changeOfficerZone(
                    String.valueOf(selectedOfficerId),
                    String.valueOf(selectedZoneId),
                    token);

            System.out.println("[ZoneAdminBean] Officer zone updated."
                    + " officerId=" + selectedOfficerId
                    + ", newZoneId=" + selectedZoneId);

            loadOfficers();

        } catch (Exception e) {
            System.err.println("[ZoneAdminBean] changeOfficerZone error: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    // ─────────────────────────────────────────────────────────
    //  Refresh
    // ─────────────────────────────────────────────────────────

    public void refresh() {
        loadDashboard();
        loadComplaints();
        loadWards();
        loadSocieties();
        loadCitizens();
        loadOfficers();
    }

    // ─────────────────────────────────────────────────────────
    //  Tab
    // ─────────────────────────────────────────────────────────

    public void setTab(String tab) { activeTab = tab; }

    // ─────────────────────────────────────────────────────────
    //  Getters / Setters
    // ─────────────────────────────────────────────────────────

    public String getActiveTab()                              { return activeTab; }

    public Collection<Complaint> getComplaints()              { return complaints; }
    public void setComplaints(Collection<Complaint> c)        { this.complaints = c; }

    public Collection<Ward> getWards()                        { return wards; }
    public void setWards(Collection<Ward> w)                  { this.wards = w; }

    public Collection<Society> getSocieties()                 { return societies; }
    public void setSocieties(Collection<Society> s)           { this.societies = s; }

    public Collection<Users> getCitizens()                    { return citizens; }
    public void setCitizens(Collection<Users> c)              { this.citizens = c; }

    public Collection<Officers> getOfficers()                 { return officers; }
    public void setOfficers(Collection<Officers> o)           { this.officers = o; }

    public Collection<Zone> getZones()                        { return zones; }
    public void setZones(Collection<Zone> z)                  { this.zones = z; }

    public Long getTotalComplaints()                          { return totalComplaints; }
    public Long getPendingComplaints()                        { return pendingComplaints; }
    public Long getResolvedComplaints()                       { return resolvedComplaints; }
    public Long getRejectedComplaints()                       { return rejectedComplaints; }

    public Users getZoneAdmin()                               { return zoneAdmin; }
    public void setZoneAdmin(Users u)                         { this.zoneAdmin = u; }

    public Integer getZoneId()                                { return zoneId; }
    public void setZoneId(Integer id)                         { this.zoneId = id; }

    public Integer getSelectedOfficerId()                     { return selectedOfficerId; }
    public void setSelectedOfficerId(Integer id)              { this.selectedOfficerId = id; }

    public Integer getSelectedZoneId()                        { return selectedZoneId; }
    public void setSelectedZoneId(Integer id)                 { this.selectedZoneId = id; }
}
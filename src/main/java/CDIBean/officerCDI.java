package CDIBean;

import Client.RestClient;
import Entity.Departments;
import Entity.Officers;
import Entity.Users;
import Entity.Ward;
import Entity.Zone;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Named(value = "officerCDI")
@ViewScoped
public class officerCDI implements Serializable {

    private RestClient restClient = new RestClient();

    private List<Officers> officers;
    private List<Users> users;
    private List<Departments> departments;
    private List<Zone> zones;
    private List<Ward> wards;

    private Integer userId;
    private Integer departmentId;
    private Integer zoneId;
    private Integer wardId;
    private String designation;

    // Token
    private String token;

    // ═══════════════════════════════════════════════════════
    //  LIFECYCLE
    // ═══════════════════════════════════════════════════════

    @PostConstruct
    public void init() {

        try {

            // ── Get token from session ───────────────
            Map<String, Object> session = FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap();

            token = (String) session.get("token");

            System.out.println("[OfficerCDI] token = " + token);

            if (token == null || token.isEmpty()) {
                System.err.println("[OfficerCDI] Token is NULL!");
                return;
            }

            loadOfficers();
            loadUsers();
            loadDepartments();
            loadZones();
            loadWards();

        } catch (Exception e) {
            System.err.println("[OfficerCDI] init error: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    // ═══════════════════════════════════════════════════════
    //  LOAD DATA
    // ═══════════════════════════════════════════════════════

    private void loadOfficers() {

        try {

            Response rs = restClient.getAllOfficers(Response.class, token);

            System.out.println("[OfficerCDI] getAllOfficers status: "
                    + rs.getStatus());

            if (rs.getStatus() == 200) {
                officers = rs.readEntity(
                        new GenericType<List<Officers>>() {});
                System.out.println("[OfficerCDI] officers loaded: "
                        + (officers != null ? officers.size() : 0));
            } else {
                officers = new ArrayList<>();
                System.err.println("[OfficerCDI] getAllOfficers failed."
                        + " Status: " + rs.getStatus());
            }

        } catch (Exception e) {
            System.err.println("[OfficerCDI] loadOfficers error: "
                    + e.getMessage());
            e.printStackTrace();
            officers = new ArrayList<>();
        }
    }

    private void loadUsers() {

        try {

            Response rs = restClient.getAllUsers(Response.class,token);

            System.out.println("[OfficerCDI] getAllUsers status: "
                    + rs.getStatus());

            if (rs.getStatus() == 200) {
                users = rs.readEntity(
                        new GenericType<List<Users>>() {});
                System.out.println("[OfficerCDI] users loaded: "
                        + (users != null ? users.size() : 0));
            } else {
                users = new ArrayList<>();
                System.err.println("[OfficerCDI] getAllUsers failed."
                        + " Status: " + rs.getStatus());
            }

        } catch (Exception e) {
            System.err.println("[OfficerCDI] loadUsers error: "
                    + e.getMessage());
            e.printStackTrace();
            users = new ArrayList<>();
        }
    }

    private void loadDepartments() {

        try {

            Response rs = restClient.getAllDepartments(Response.class, token);

            System.out.println("[OfficerCDI] getAllDepartments status: "
                    + rs.getStatus());

            if (rs.getStatus() == 200) {
                departments = rs.readEntity(
                        new GenericType<List<Departments>>() {});
                System.out.println("[OfficerCDI] departments loaded: "
                        + (departments != null ? departments.size() : 0));
            } else {
                departments = new ArrayList<>();
                System.err.println("[OfficerCDI] getAllDepartments failed."
                        + " Status: " + rs.getStatus());
            }

        } catch (Exception e) {
            System.err.println("[OfficerCDI] loadDepartments error: "
                    + e.getMessage());
            e.printStackTrace();
            departments = new ArrayList<>();
        }
    }

    private void loadZones() {

        try {

            Response rs = restClient.getAllZones(Response.class, token);

            System.out.println("[OfficerCDI] getAllZones status: "
                    + rs.getStatus());

            if (rs.getStatus() == 200) {
                zones = rs.readEntity(
                        new GenericType<List<Zone>>() {});
                System.out.println("[OfficerCDI] zones loaded: "
                        + (zones != null ? zones.size() : 0));
            } else {
                zones = new ArrayList<>();
                System.err.println("[OfficerCDI] getAllZones failed."
                        + " Status: " + rs.getStatus());
            }

        } catch (Exception e) {
            System.err.println("[OfficerCDI] loadZones error: "
                    + e.getMessage());
            e.printStackTrace();
            zones = new ArrayList<>();
        }
    }

    private void loadWards() {

        try {

            Response rs = restClient.getAllWards(Response.class, token);

            System.out.println("[OfficerCDI] getAllWards status: "
                    + rs.getStatus());

            if (rs.getStatus() == 200) {
                wards = rs.readEntity(
                        new GenericType<List<Ward>>() {});
                System.out.println("[OfficerCDI] wards loaded: "
                        + (wards != null ? wards.size() : 0));
            } else {
                wards = new ArrayList<>();
                System.err.println("[OfficerCDI] getAllWards failed."
                        + " Status: " + rs.getStatus());
            }

        } catch (Exception e) {
            System.err.println("[OfficerCDI] loadWards error: "
                    + e.getMessage());
            e.printStackTrace();
            wards = new ArrayList<>();
        }
    }

    // ═══════════════════════════════════════════════════════
    //  CRUD ACTIONS
    // ═══════════════════════════════════════════════════════

    public void createOfficer() {

        System.out.println("[OfficerCDI] CREATE OFFICER CLICKED");
        System.out.println("[OfficerCDI] userId       = " + userId);
        System.out.println("[OfficerCDI] departmentId = " + departmentId);
        System.out.println("[OfficerCDI] zoneId       = " + zoneId);
        System.out.println("[OfficerCDI] wardId       = " + wardId);
        System.out.println("[OfficerCDI] designation  = " + designation);

        // ── Basic Validation ─────────────────────────────
        if (!validateOfficer()) {
            return;
        }

        try {

            restClient.createOfficer(
                    String.valueOf(userId),
                    String.valueOf(departmentId),
                    String.valueOf(zoneId),
                    String.valueOf(wardId),
                    designation.trim(),token);

            System.out.println("[OfficerCDI] createOfficer REST called.");

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_INFO,
                            "Success",
                            "Officer Created Successfully"));

            // Reset fields
            resetFields();

            // Reload officers list
            loadOfficers();

        } catch (Exception e) {
            System.err.println("[OfficerCDI] createOfficer error: "
                    + e.getMessage());
            e.printStackTrace();
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Error",
                            "Failed to create officer: "
                            + e.getMessage()));
        }
    }

    // ═══════════════════════════════════════════════════════
    //  VALIDATION
    // ═══════════════════════════════════════════════════════

    private boolean validateOfficer() {

        boolean      ok = true;
        FacesContext fc = FacesContext.getCurrentInstance();

        // 1) User
        if (userId == null) {
            fc.addMessage(null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Validation Error",
                            "Please select a User."));
            fc.validationFailed();
            ok = false;
        }

        // 2) Department
        if (departmentId == null) {
            fc.addMessage(null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Validation Error",
                            "Please select a Department."));
            fc.validationFailed();
            ok = false;
        }

        // 3) Zone
        if (zoneId == null) {
            fc.addMessage(null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Validation Error",
                            "Please select a Zone."));
            fc.validationFailed();
            ok = false;
        }

        // 4) Ward
        if (wardId == null) {
            fc.addMessage(null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Validation Error",
                            "Please select a Ward."));
            fc.validationFailed();
            ok = false;
        }

        // 5) Designation
        if (designation == null || designation.trim().isEmpty()) {
            fc.addMessage(null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Validation Error",
                            "Designation is required."));
            fc.validationFailed();
            ok = false;
        } else if (designation.trim().length() < 3) {
            fc.addMessage(null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Validation Error",
                            "Designation must be at least 3 characters."));
            fc.validationFailed();
            ok = false;
        } else if (designation.trim().length() > 50) {
            fc.addMessage(null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Validation Error",
                            "Designation cannot exceed 50 characters."));
            fc.validationFailed();
            ok = false;
        }

        return ok;
    }

    // ═══════════════════════════════════════════════════════
    //  HELPERS
    // ═══════════════════════════════════════════════════════

    private void resetFields() {
        userId       = null;
        departmentId = null;
        zoneId       = null;
        wardId       = null;
        designation  = "";
    }

    // ═══════════════════════════════════════════════════════
    //  Getters / Setters
    // ═══════════════════════════════════════════════════════

    public List<Officers> getOfficers()                      { return officers; }
    public void setOfficers(List<Officers> officers)         { this.officers = officers; }

    public List<Users> getUsers()                            { return users; }
    public void setUsers(List<Users> users)                  { this.users = users; }

    public List<Departments> getDepartments()                { return departments; }
    public void setDepartments(List<Departments> depts)      { this.departments = depts; }

    public List<Zone> getZones()                             { return zones; }
    public void setZones(List<Zone> zones)                   { this.zones = zones; }

    public List<Ward> getWards()                             { return wards; }
    public void setWards(List<Ward> wards)                   { this.wards = wards; }

    public Integer getUserId()                               { return userId; }
    public void setUserId(Integer userId)                    { this.userId = userId; }

    public Integer getDepartmentId()                         { return departmentId; }
    public void setDepartmentId(Integer departmentId)        { this.departmentId = departmentId; }

    public Integer getZoneId()                               { return zoneId; }
    public void setZoneId(Integer zoneId)                    { this.zoneId = zoneId; }

    public Integer getWardId()                               { return wardId; }
    public void setWardId(Integer wardId)                    { this.wardId = wardId; }

    public String getDesignation()                           { return designation; }
    public void setDesignation(String designation)           { this.designation = designation; }

    public String getToken()                                 { return token; }
    public void setToken(String token)                       { this.token = token; }
}
package CDIBean;

import Client.RestClient;
import Entity.Corporation;
import Entity.Zone;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Named
@ViewScoped
public class ZoneManagementCDI implements Serializable {

    private RestClient restClient = new RestClient();

    private List<Zone> zones;
    private List<Corporation> corporations;

    private Zone selectedZone;

    // Add Zone Fields
    private String newZoneName;
    private String newStatus;
    private Integer newCorporationId;

    // Edit Zone Fields
    private String editZoneName;
    private String editStatus;
    private Integer editCorporationId;

    // Token
    private String token;

    // ── Validation rules ──
    private static final Pattern ZONE_NAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9 .&\\-]+$");

    private static final int ZONE_NAME_MIN = 3;
    private static final int ZONE_NAME_MAX = 50;

    // ═══════════════════════════════════════════════════════
    //  INIT
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

            System.out.println("[ZoneManagementCDI] token = " + token);

            if (token == null || token.isEmpty()) {
                System.err.println("[ZoneManagementCDI] Token is NULL!");
                return;
            }

            loadZones();
            loadCorporations();

            newStatus = "ACTIVE";
            selectedZone = new Zone();

        } catch (Exception e) {
            System.err.println("[ZoneManagementCDI] init error: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    // ═══════════════════════════════════════════════════════
    //  LOAD DATA
    // ═══════════════════════════════════════════════════════

    private void loadZones() {

        try {

            Response rs = restClient.getAllZones(Response.class, token);

            System.out.println("[ZoneManagementCDI] getAllZones status: "
                    + rs.getStatus());

            if (rs.getStatus() == 200) {
                zones = rs.readEntity(new GenericType<List<Zone>>() {});
                System.out.println("[ZoneManagementCDI] zones loaded: "
                        + (zones != null ? zones.size() : 0));
            } else {
                zones = new java.util.ArrayList<>();
                System.err.println("[ZoneManagementCDI] getAllZones failed."
                        + " Status: " + rs.getStatus());
            }

        } catch (Exception e) {
            System.err.println("[ZoneManagementCDI] loadZones error: "
                    + e.getMessage());
            e.printStackTrace();
            zones = new java.util.ArrayList<>();
        }
    }

    private void loadCorporations() {

        try {

            Response rs = restClient.getAllCorporations(Response.class,token);

            System.out.println("[ZoneManagementCDI] getAllCorporations status: "
                    + rs.getStatus());

            if (rs.getStatus() == 200) {
                corporations = rs.readEntity(
                        new GenericType<List<Corporation>>() {});
                System.out.println("[ZoneManagementCDI] corporations loaded: "
                        + (corporations != null ? corporations.size() : 0));
            } else {
                corporations = new java.util.ArrayList<>();
                System.err.println(
                        "[ZoneManagementCDI] getAllCorporations failed."
                        + " Status: " + rs.getStatus());
            }

        } catch (Exception e) {
            System.err.println("[ZoneManagementCDI] loadCorporations error: "
                    + e.getMessage());
            e.printStackTrace();
            corporations = new java.util.ArrayList<>();
        }
    }

    // ═══════════════════════════════════════════════════════
    //  PREPARE
    // ═══════════════════════════════════════════════════════

    public void prepareCreate() {
        newZoneName = "";
        newStatus = "ACTIVE";
        newCorporationId = null;
    }

    // ═══════════════════════════════════════════════════════
    //  VALIDATION
    // ═══════════════════════════════════════════════════════

    /**
     * isEdit = false → errors go to "zoneForm:addMsgs"  (Add dialog)
     * isEdit = true  → errors go to "zoneForm:editMsgs" (Edit dialog)
     */
    private boolean validateZone(String zoneName,
                                  String status,
                                  Integer corporationId,
                                  Integer excludeZoneId,
                                  boolean isEdit) {

        boolean ok = true;
        FacesContext fc = FacesContext.getCurrentInstance();
        String target = isEdit ? "zoneForm:editMsgs" : "zoneForm:addMsgs";

        // 1) Zone name
        if (zoneName == null || zoneName.trim().isEmpty()) {
            addError(fc, target, "Zone Name is required.");
            ok = false;
        } else {

            String trimmed = zoneName.trim();

            if (trimmed.length() < ZONE_NAME_MIN) {
                addError(fc, target,
                        "Zone Name must be at least "
                        + ZONE_NAME_MIN + " characters.");
                ok = false;
            } else if (trimmed.length() > ZONE_NAME_MAX) {
                addError(fc, target,
                        "Zone Name cannot exceed "
                        + ZONE_NAME_MAX + " characters.");
                ok = false;
            }

            if (!ZONE_NAME_PATTERN.matcher(trimmed).matches()) {
                addError(fc, target,
                        "Zone Name can only contain letters, digits, "
                        + "spaces, hyphens, dots and '&'.");
                ok = false;
            }

            // Duplicate check (same corporation, same name, case-insensitive)
            if (corporationId != null && zones != null) {
                for (Zone z : zones) {
                    if (excludeZoneId != null
                            && z.getZoneId().equals(excludeZoneId)) {
                        continue;
                    }
                    if (z.getCorporationId() != null
                            && z.getCorporationId().getCorporationId()
                                .equals(corporationId)
                            && z.getZoneName() != null
                            && z.getZoneName().trim()
                                .equalsIgnoreCase(trimmed)) {

                        addError(fc, target,
                                "A zone with this name already exists "
                                + "in the selected corporation.");
                        ok = false;
                        break;
                    }
                }
            }
        }

        // 2) Corporation
        if (corporationId == null) {
            addError(fc, target, "Please select a Corporation.");
            ok = false;
        }

        // 3) Status
        if (status == null || status.trim().isEmpty()) {
            addError(fc, target, "Status is required.");
            ok = false;
        } else if (!status.equals("ACTIVE") && !status.equals("INACTIVE")) {
            addError(fc, target, "Invalid status value.");
            ok = false;
        }

        return ok;
    }

    private void addError(FacesContext fc, String clientId, String detail) {
        fc.addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                        "Validation Error", detail));
        fc.validationFailed();
    }

    private void addSuccess(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                        summary, detail));
    }

    // ═══════════════════════════════════════════════════════
    //  CRUD ACTIONS
    // ═══════════════════════════════════════════════════════

    public void createZone() {

        System.out.println("[ZoneManagementCDI] CREATE CLICKED");
        System.out.println("[ZoneManagementCDI] Zone Name  = " + newZoneName);
        System.out.println("[ZoneManagementCDI] Status     = " + newStatus);
        System.out.println("[ZoneManagementCDI] Corporation= " + newCorporationId);

        if (!validateZone(newZoneName, newStatus,
                newCorporationId, null, false)) {
            return;
        }

        try {

            // Call REST endpoint
            restClient.createZone(
                    newZoneName.trim(),
                    newStatus,
                    String.valueOf(newCorporationId),token);

            System.out.println("[ZoneManagementCDI] createZone REST called.");

            // Reload zones list
            loadZones();

            addSuccess("Success",
                    "Zone '" + newZoneName.trim()
                    + "' created successfully.");

            prepareCreate();

        } catch (Exception e) {
            System.err.println("[ZoneManagementCDI] createZone error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    "zoneForm:addMsgs",
                    "Failed to create zone: " + e.getMessage());
        }
    }

    public void editZone(Zone zone) {

        selectedZone = zone;

        editZoneName    = zone.getZoneName();
        editStatus      = zone.getStatus();

        if (zone.getCorporationId() != null) {
            editCorporationId =
                    zone.getCorporationId().getCorporationId();
        } else {
            editCorporationId = null;
        }

        System.out.println("[ZoneManagementCDI] Editing Zone = "
                + zone.getZoneId());
    }

    public void updateZone() {

        if (selectedZone == null || selectedZone.getZoneId() == null) {
            addError(FacesContext.getCurrentInstance(),
                    "zoneForm:editMsgs",
                    "No zone selected for update.");
            return;
        }

        if (!validateZone(editZoneName, editStatus, editCorporationId,
                selectedZone.getZoneId(), true)) {
            return;
        }

        try {

            restClient.updateZone(
                    String.valueOf(selectedZone.getZoneId()),
                    editZoneName.trim(),
                    editStatus,
                    String.valueOf(editCorporationId),token);

            System.out.println("[ZoneManagementCDI] updateZone REST called.");

            loadZones();

            addSuccess("Updated", "Zone updated successfully.");

        } catch (Exception e) {
            System.err.println("[ZoneManagementCDI] updateZone error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    "zoneForm:editMsgs",
                    "Failed to update zone: " + e.getMessage());
        }
    }

    public void activate(Integer zoneId) {

        try {

            restClient.activateZone(String.valueOf(zoneId),token);

            System.out.println("[ZoneManagementCDI] activateZone REST called."
                    + " zoneId=" + zoneId);

            loadZones();

            addSuccess("Activated", "Zone has been activated.");

        } catch (Exception e) {
            System.err.println("[ZoneManagementCDI] activate error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    null,
                    "Failed to activate zone: " + e.getMessage());
        }
    }

    public void deactivate(Integer zoneId) {

        try {

            restClient.deactivateZone(String.valueOf(zoneId),token);

            System.out.println("[ZoneManagementCDI] deactivateZone REST called."
                    + " zoneId=" + zoneId);

            loadZones();

            addSuccess("Deactivated", "Zone has been deactivated.");

        } catch (Exception e) {
            System.err.println("[ZoneManagementCDI] deactivate error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    null,
                    "Failed to deactivate zone: " + e.getMessage());
        }
    }

    public void deleteZone(Integer zoneId) {

        try {

            restClient.deleteZone(String.valueOf(zoneId),token);

            System.out.println("[ZoneManagementCDI] deleteZone REST called."
                    + " zoneId=" + zoneId);

            loadZones();

            addSuccess("Deleted", "Zone deleted successfully.");

        } catch (Exception e) {
            System.err.println("[ZoneManagementCDI] deleteZone error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    null,
                    "Failed to delete zone: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════
    //  Getters / Setters
    // ═══════════════════════════════════════════════════════

    public List<Zone> getZones()                            { return zones; }
    public void setZones(List<Zone> zones)                  { this.zones = zones; }

    public List<Corporation> getCorporations()              { return corporations; }
    public void setCorporations(List<Corporation> corps)    { this.corporations = corps; }

    public Zone getSelectedZone()                           { return selectedZone; }
    public void setSelectedZone(Zone selectedZone)          { this.selectedZone = selectedZone; }

    public String getNewZoneName()                          { return newZoneName; }
    public void setNewZoneName(String newZoneName)          { this.newZoneName = newZoneName; }

    public String getNewStatus()                            { return newStatus; }
    public void setNewStatus(String newStatus)              { this.newStatus = newStatus; }

    public Integer getNewCorporationId()                    { return newCorporationId; }
    public void setNewCorporationId(Integer id)             { this.newCorporationId = id; }

    public String getEditZoneName()                         { return editZoneName; }
    public void setEditZoneName(String editZoneName)        { this.editZoneName = editZoneName; }

    public String getEditStatus()                           { return editStatus; }
    public void setEditStatus(String editStatus)            { this.editStatus = editStatus; }

    public Integer getEditCorporationId()                   { return editCorporationId; }
    public void setEditCorporationId(Integer id)            { this.editCorporationId = id; }

    public String getToken()                                { return token; }
    public void setToken(String token)                      { this.token = token; }
}
package CDIBean;

import Client.RestClient;
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
import java.util.regex.Pattern;

@Named(value = "wardManagementCDI")
@ViewScoped
public class WardManagementCDI implements Serializable {

    private RestClient restClient = new RestClient();

    private List<Ward> wards;
    private List<Zone> zones;

    private Ward selectedWard;

    /* CREATE fields */
    private String  newWardName;
    private String  newStatus;
    private Integer newZoneId;

    /* EDIT fields */
    private String  editWardName;
    private String  editStatus;
    private Integer editZoneId;

    // Token
    private String token;

    // ── Validation rules ──────────────────────────────────
    private static final Pattern WARD_NAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9 .&\\-]+$");

    private static final int WARD_NAME_MIN = 3;
    private static final int WARD_NAME_MAX = 50;

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

            System.out.println("[WardManagementCDI] token = " + token);

            if (token == null || token.isEmpty()) {
                System.err.println("[WardManagementCDI] Token is NULL!");
                return;
            }

            loadWards();
            loadZones();

            selectedWard = new Ward();
            newStatus    = "ACTIVE";

        } catch (Exception e) {
            System.err.println("[WardManagementCDI] init error: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    // ═══════════════════════════════════════════════════════
    //  LOAD DATA
    // ═══════════════════════════════════════════════════════

    public void loadWards() {

        try {

            Response rs = restClient.getAllWards(Response.class, token);

            System.out.println("[WardManagementCDI] getAllWards status: "
                    + rs.getStatus());

            if (rs.getStatus() == 200) {
                wards = rs.readEntity(new GenericType<List<Ward>>() {});
                System.out.println("[WardManagementCDI] wards loaded: "
                        + (wards != null ? wards.size() : 0));
            } else {
                wards = new ArrayList<>();
                System.err.println("[WardManagementCDI] getAllWards failed."
                        + " Status: " + rs.getStatus());
            }

        } catch (Exception e) {
            System.err.println("[WardManagementCDI] loadWards error: "
                    + e.getMessage());
            e.printStackTrace();
            wards = new ArrayList<>();
        }
    }

    private void loadZones() {

        try {

            Response rs = restClient.getAllZones(Response.class, token);

            System.out.println("[WardManagementCDI] getAllZones status: "
                    + rs.getStatus());

            if (rs.getStatus() == 200) {
                zones = rs.readEntity(new GenericType<List<Zone>>() {});
                System.out.println("[WardManagementCDI] zones loaded: "
                        + (zones != null ? zones.size() : 0));
            } else {
                zones = new ArrayList<>();
                System.err.println("[WardManagementCDI] getAllZones failed."
                        + " Status: " + rs.getStatus());
            }

        } catch (Exception e) {
            System.err.println("[WardManagementCDI] loadZones error: "
                    + e.getMessage());
            e.printStackTrace();
            zones = new ArrayList<>();
        }
    }

    // Keep loadData() for backward compatibility with any XHTML calls
    public void loadData() {
        loadWards();
    }

    // ═══════════════════════════════════════════════════════
    //  PREPARE
    // ═══════════════════════════════════════════════════════

    /** Called by "Add Ward" button — resets the add-dialog fields. */
    public void prepareCreate() {
        newWardName = "";
        newStatus   = "ACTIVE";
        newZoneId   = null;
    }

    // ═══════════════════════════════════════════════════════
    //  VALIDATION
    // ═══════════════════════════════════════════════════════

    /**
     * isEdit = false → "wardForm:addWardName" / "wardForm:addZone" etc.
     * isEdit = true  → "wardForm:editWardName" / "wardForm:editZone" etc.
     */
    private boolean validateWard(String  wardName,
                                  String  status,
                                  Integer zoneId,
                                  Integer excludeWardId,
                                  boolean isEdit) {

        boolean      ok = true;
        FacesContext fc = FacesContext.getCurrentInstance();

        // Prefix matches the component IDs in the XHTML
        String prefix = isEdit ? "wardForm:edit" : "wardForm:add";

        // ── 1. Ward Name ──────────────────────────────────
        if (wardName == null || wardName.trim().isEmpty()) {
            addError(fc, prefix + "WardName", "Ward Name is required.");
            ok = false;
        } else {

            String trimmed = wardName.trim();

            if (trimmed.length() < WARD_NAME_MIN) {
                addError(fc, prefix + "WardName",
                        "Ward Name must be at least "
                        + WARD_NAME_MIN + " characters.");
                ok = false;
            } else if (trimmed.length() > WARD_NAME_MAX) {
                addError(fc, prefix + "WardName",
                        "Ward Name cannot exceed "
                        + WARD_NAME_MAX + " characters.");
                ok = false;
            }

            if (!WARD_NAME_PATTERN.matcher(trimmed).matches()) {
                addError(fc, prefix + "WardName",
                        "Ward Name can only contain letters, digits, "
                        + "spaces, hyphens, dots and '&'.");
                ok = false;
            }

            // Duplicate check (same zone + name, case-insensitive)
            if (zoneId != null && ok && wards != null) {
                for (Ward w : wards) {
                    if (excludeWardId != null
                            && w.getWardId().equals(excludeWardId)) {
                        continue; // skip the ward being edited
                    }
                    if (w.getZoneId() != null
                            && w.getZoneId().getZoneId().equals(zoneId)
                            && w.getWardName() != null
                            && w.getWardName().trim()
                                             .equalsIgnoreCase(trimmed)) {

                        addError(fc, prefix + "WardName",
                                "A ward with this name already exists "
                                + "in the selected zone.");
                        ok = false;
                        break;
                    }
                }
            }
        }

        // ── 2. Zone ───────────────────────────────────────
        if (zoneId == null) {
            addError(fc, prefix + "Zone", "Please select a Zone.");
            ok = false;
        }

        // ── 3. Status ─────────────────────────────────────
        if (status == null || status.trim().isEmpty()) {
            addError(fc, prefix + "Status", "Status is required.");
            ok = false;
        } else if (!"ACTIVE".equals(status) && !"INACTIVE".equals(status)) {
            addError(fc, prefix + "Status", "Invalid status value.");
            ok = false;
        }

        return ok;
    }

    // ── Message helpers ───────────────────────────────────

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

    /** Called by "Save Ward" button in the Add dialog. */
    public void createWard() {

        System.out.println("[WardManagementCDI] CREATE WARD CLICKED");
        System.out.println("[WardManagementCDI] Ward Name = " + newWardName);
        System.out.println("[WardManagementCDI] Status    = " + newStatus);
        System.out.println("[WardManagementCDI] Zone ID   = " + newZoneId);

        if (!validateWard(newWardName, newStatus, newZoneId,
                null, false)) {
            return;
        }

        try {

            restClient.createWard(
                    String.valueOf(newZoneId),
                    newWardName.trim(),
                    newStatus,token);

            System.out.println("[WardManagementCDI] createWard REST called.");

            loadWards();

            addSuccess("Success",
                    "Ward '" + newWardName.trim()
                    + "' created successfully.");

            prepareCreate();

        } catch (Exception e) {
            System.err.println("[WardManagementCDI] createWard error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    "wardForm:addWardName",
                    "Failed to create ward: " + e.getMessage());
        }
    }

    /** Called by "Edit" row button — populates edit-dialog fields. */
    public void editWard(Ward ward) {

        selectedWard = ward;
        editWardName = ward.getWardName();
        editStatus   = ward.getStatus();
        editZoneId   = (ward.getZoneId() != null)
                       ? ward.getZoneId().getZoneId()
                       : null;

        System.out.println("[WardManagementCDI] Editing Ward = "
                + ward.getWardId());
    }

    /** Called by "Update Ward" button in the Edit dialog. */
    public void updateWard() {

        if (selectedWard == null || selectedWard.getWardId() == null) {
            addError(FacesContext.getCurrentInstance(),
                     "wardForm:editWardName",
                     "No ward selected for update.");
            return;
        }

        if (!validateWard(editWardName, editStatus, editZoneId,
                selectedWard.getWardId(), true)) {
            return;
        }

        try {

            restClient.updateWard(
                    String.valueOf(selectedWard.getWardId()),
                    String.valueOf(editZoneId),
                    editWardName.trim(),
                    editStatus,token);

            System.out.println("[WardManagementCDI] updateWard REST called.");

            loadWards();

            addSuccess("Updated", "Ward updated successfully.");

        } catch (Exception e) {
            System.err.println("[WardManagementCDI] updateWard error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    "wardForm:editWardName",
                    "Failed to update ward: " + e.getMessage());
        }
    }

    /** Set ward status → ACTIVE. */
    public void activate(Integer wardId) {

        try {

            // Find ward in list to get current values
            Ward ward = null;
            if (wards != null) {
                ward = wards.stream()
                        .filter(w -> w.getWardId().equals(wardId))
                        .findFirst()
                        .orElse(null);
            }

            if (ward == null) {
                addError(FacesContext.getCurrentInstance(),
                        null,
                        "Ward not found.");
                return;
            }

            restClient.updateWard(
                    String.valueOf(wardId),
                    String.valueOf(ward.getZoneId().getZoneId()),
                    ward.getWardName(),
                    "ACTIVE",token);

            System.out.println("[WardManagementCDI] activate REST called."
                    + " wardId=" + wardId);

            loadWards();

            addSuccess("Activated", "Ward has been activated.");

        } catch (Exception e) {
            System.err.println("[WardManagementCDI] activate error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    null,
                    "Failed to activate ward: " + e.getMessage());
        }
    }

    /** Set ward status → INACTIVE. */
    public void deactivate(Integer wardId) {

        try {

            // Find ward in list to get current values
            Ward ward = null;
            if (wards != null) {
                ward = wards.stream()
                        .filter(w -> w.getWardId().equals(wardId))
                        .findFirst()
                        .orElse(null);
            }

            if (ward == null) {
                addError(FacesContext.getCurrentInstance(),
                        null,
                        "Ward not found.");
                return;
            }

            restClient.updateWard(
                    String.valueOf(wardId),
                    String.valueOf(ward.getZoneId().getZoneId()),
                    ward.getWardName(),
                    "INACTIVE",token);

            System.out.println("[WardManagementCDI] deactivate REST called."
                    + " wardId=" + wardId);

            loadWards();

            addSuccess("Deactivated", "Ward has been deactivated.");

        } catch (Exception e) {
            System.err.println("[WardManagementCDI] deactivate error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    null,
                    "Failed to deactivate ward: " + e.getMessage());
        }
    }

    /** Hard-delete a ward. */
    public void deleteWard(Integer wardId) {

        try {

            restClient.deleteWard(String.valueOf(wardId),token);

            System.out.println("[WardManagementCDI] deleteWard REST called."
                    + " wardId=" + wardId);

            loadWards();

            addSuccess("Deleted", "Ward deleted successfully.");

        } catch (Exception e) {
            System.err.println("[WardManagementCDI] deleteWard error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    null,
                    "Failed to delete ward: " + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════
    //  Getters / Setters
    // ═══════════════════════════════════════════════════════

    public List<Ward> getWards()                             { return wards; }
    public void setWards(List<Ward> wards)                   { this.wards = wards; }

    public List<Zone> getZones()                             { return zones; }
    public void setZones(List<Zone> zones)                   { this.zones = zones; }

    public Ward getSelectedWard()                            { return selectedWard; }
    public void setSelectedWard(Ward selectedWard)           { this.selectedWard = selectedWard; }

    public String getNewWardName()                           { return newWardName; }
    public void setNewWardName(String newWardName)           { this.newWardName = newWardName; }

    public String getNewStatus()                             { return newStatus; }
    public void setNewStatus(String newStatus)               { this.newStatus = newStatus; }

    public Integer getNewZoneId()                            { return newZoneId; }
    public void setNewZoneId(Integer newZoneId)              { this.newZoneId = newZoneId; }

    public String getEditWardName()                          { return editWardName; }
    public void setEditWardName(String editWardName)         { this.editWardName = editWardName; }

    public String getEditStatus()                            { return editStatus; }
    public void setEditStatus(String editStatus)             { this.editStatus = editStatus; }

    public Integer getEditZoneId()                           { return editZoneId; }
    public void setEditZoneId(Integer editZoneId)            { this.editZoneId = editZoneId; }

    public String getToken()                                 { return token; }
    public void setToken(String token)                       { this.token = token; }
}
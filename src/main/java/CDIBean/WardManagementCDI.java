package CDIBean;

import EJB.AdminBeanLocal;
import Entity.Ward;
import Entity.Zone;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;
import java.util.regex.Pattern;

@Named(value = "wardManagementCDI")
@ViewScoped
public class WardManagementCDI implements Serializable {

    @EJB
    private AdminBeanLocal adminService;

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
        wards        = adminService.getAllWards();
        zones        = adminService.getAllZones();
        selectedWard = new Ward();
        newStatus    = "ACTIVE";
    }

    public void loadData() {
        wards = adminService.getAllWards();
    }

    /** Called by "Add Ward" button — resets the add-dialog fields. */
    public void prepareCreate() {
        newWardName = "";
        newStatus   = "ACTIVE";
        newZoneId   = null;
    }

    // ═══════════════════════════════════════════════════════
    //  VALIDATION  (mirrors ZoneManagementCDI exactly)
    // ═══════════════════════════════════════════════════════

    /**
     * Posts FacesMessages to the correct dialog component so that
     * the p:message tags under each field light up, AND
     * calls fc.validationFailed() so that
     * oncomplete="if(!args.validationFailed)" keeps the dialog open.
     *
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
            if (zoneId != null && ok) {
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

    /**
     * Adds an error to a specific component AND marks validation as failed
     * so PrimeFaces sets args.validationFailed = true on the client.
     * Identical pattern to ZoneManagementCDI.addError().
     */
    private void addError(FacesContext fc, String clientId, String detail) {
        fc.addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                 "Validation Error", detail));
        fc.validationFailed(); // ← key: makes args.validationFailed true
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

        System.out.println("CREATE WARD CLICKED");
        System.out.println("Ward Name  = " + newWardName);
        System.out.println("Status     = " + newStatus);
        System.out.println("Zone ID    = " + newZoneId);

        if (!validateWard(newWardName, newStatus, newZoneId,
                          null, false)) {
            return; // validationFailed() already called → dialog stays open
        }

        adminService.createWard(
                newZoneId,
                newWardName.trim(),
                newStatus);

        loadData();

        addSuccess("Success",
                "Ward '" + newWardName.trim() + "' created successfully.");

        prepareCreate(); // reset fields for next use
    }

    /** Called by "Edit" row button — populates edit-dialog fields. */
    public void editWard(Ward ward) {

        selectedWard = ward;
        editWardName = ward.getWardName();
        editStatus   = ward.getStatus();
        editZoneId   = (ward.getZoneId() != null)
                       ? ward.getZoneId().getZoneId()
                       : null;

        System.out.println("Editing Ward = " + ward.getWardId());
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
            return; // validationFailed() already called → dialog stays open
        }

        adminService.updateWard(
                selectedWard.getWardId(),
                editZoneId,
                editWardName.trim(),
                editStatus);

        loadData();

        addSuccess("Updated", "Ward updated successfully.");
    }

    /** Set ward status → ACTIVE. */
    public void activate(Integer wardId) {

        Ward ward = wards.stream()
                .filter(w -> w.getWardId().equals(wardId))
                .findFirst().orElse(null);

        if (ward != null) {
            adminService.updateWard(
                    wardId,
                    ward.getZoneId().getZoneId(),
                    ward.getWardName(),
                    "ACTIVE");
            loadData();
            addSuccess("Activated", "Ward has been activated.");
        }
    }

    /** Set ward status → INACTIVE. */
    public void deactivate(Integer wardId) {

        Ward ward = wards.stream()
                .filter(w -> w.getWardId().equals(wardId))
                .findFirst().orElse(null);

        if (ward != null) {
            adminService.updateWard(
                    wardId,
                    ward.getZoneId().getZoneId(),
                    ward.getWardName(),
                    "INACTIVE");
            loadData();
            addSuccess("Deactivated", "Ward has been deactivated.");
        }
    }

    /** Hard-delete a ward. */
    public void deleteWard(Integer wardId) {
        adminService.deleteWard(wardId);
        loadData();
        addSuccess("Deleted", "Ward deleted successfully.");
    }
 

    // ═══════════════════════════════════════════════════════
    //  Getters / Setters
    // ═══════════════════════════════════════════════════════

    public AdminBeanLocal getAdminService()                  { return adminService; }
    public void setAdminService(AdminBeanLocal s)            { this.adminService = s; }

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
}
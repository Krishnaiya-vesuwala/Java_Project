package CDIBean;

import EJB.AdminBeanLocal;
import Entity.Corporation;
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

@Named
@ViewScoped
public class ZoneManagementCDI implements Serializable {

    @EJB
    private AdminBeanLocal adminService;

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

    // ── Validation rules ──
    private static final Pattern ZONE_NAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9 .&\\-]+$");

    private static final int ZONE_NAME_MIN = 3;
    private static final int ZONE_NAME_MAX = 50;

    @PostConstruct
    public void init() {
        zones = adminService.getAllZones();
        corporations = adminService.getAllCorporation();
        newStatus = "ACTIVE";
        selectedZone = new Zone();
        System.out.println("Corporations Loaded = " + corporations);
    }

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
            if (corporationId != null) {
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
                new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    // ═══════════════════════════════════════════════════════
    //  CRUD ACTIONS
    // ═══════════════════════════════════════════════════════

    public void createZone() {

        System.out.println("CREATE CLICKED");
        System.out.println("Zone Name = " + newZoneName);
        System.out.println("Status = " + newStatus);
        System.out.println("Corporation = " + newCorporationId);

        if (!validateZone(newZoneName, newStatus,
                          newCorporationId, null, false)) {
            return;
        }

        adminService.createZone(
                newZoneName.trim(),
                newStatus,
                newCorporationId);

        zones = adminService.getAllZones();

        addSuccess("Success",
                "Zone '" + newZoneName.trim() + "' created successfully.");

        prepareCreate();
    }

    public void editZone(Zone zone) {

        selectedZone = zone;

        editZoneName = zone.getZoneName();
        editStatus = zone.getStatus();

        if (zone.getCorporationId() != null) {
            editCorporationId =
                    zone.getCorporationId().getCorporationId();
        } else {
            editCorporationId = null;
        }

        System.out.println("Editing Zone = " + zone.getZoneId());
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

        adminService.updateZone(
                selectedZone.getZoneId(),
                editZoneName.trim(),
                editStatus,
                editCorporationId);

        zones = adminService.getAllZones();

        addSuccess("Updated", "Zone updated successfully.");
    }

  public void activate(Integer zoneId) {
    adminService.activateZone(zoneId);
    zones = adminService.getAllZones();
    addSuccess("Activated", "Zone has been activated.");
}

public void deactivate(Integer zoneId) {
    adminService.deactivateZone(zoneId);
    zones = adminService.getAllZones();
    addSuccess("Deactivated", "Zone has been deactivated.");
}

    public void deleteZone(Integer zoneId) {
        adminService.deleteZone(zoneId);
        zones = adminService.getAllZones();
        addSuccess("Deleted", "Zone deleted successfully.");
    }

    // ═══════════════════════════════════════════════════════
    //  Getters / Setters
    // ═══════════════════════════════════════════════════════

    public List<Zone> getZones() { return zones; }
    public void setZones(List<Zone> zones) { this.zones = zones; }

    public List<Corporation> getCorporations() { return corporations; }
    public void setCorporations(List<Corporation> corporations) { this.corporations = corporations; }

    public Zone getSelectedZone() { return selectedZone; }
    public void setSelectedZone(Zone selectedZone) { this.selectedZone = selectedZone; }

    public String getNewZoneName() { return newZoneName; }
    public void setNewZoneName(String newZoneName) { this.newZoneName = newZoneName; }

    public String getNewStatus() { return newStatus; }
    public void setNewStatus(String newStatus) { this.newStatus = newStatus; }

    public Integer getNewCorporationId() { return newCorporationId; }
    public void setNewCorporationId(Integer newCorporationId) { this.newCorporationId = newCorporationId; }

    public String getEditZoneName() { return editZoneName; }
    public void setEditZoneName(String editZoneName) { this.editZoneName = editZoneName; }

    public String getEditStatus() { return editStatus; }
    public void setEditStatus(String editStatus) { this.editStatus = editStatus; }

    public Integer getEditCorporationId() { return editCorporationId; }
    public void setEditCorporationId(Integer editCorporationId) { this.editCorporationId = editCorporationId; }
}
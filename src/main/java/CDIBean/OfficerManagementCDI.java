package CDIBean;

import EJB.MasterDataService;
import EJB.OfficerBeanLocal;
import Entity.Departments;
import Entity.Officers;
import Entity.Users;
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

@Named
@ViewScoped
public class OfficerManagementCDI implements Serializable {

    // ═══════════════════════════════════════════════════════
    //  EJBs
    // ═══════════════════════════════════════════════════════

    @EJB
    private OfficerBeanLocal officerBean;

    @EJB
    private MasterDataService masterService;

    // ═══════════════════════════════════════════════════════
    //  Lists
    // ═══════════════════════════════════════════════════════

    private List<Officers>    officers;
    private List<Users>       availableUsers;
    private List<Departments> departments;
    private List<Zone>        zones;
    private List<Ward>        wards;

    // ═══════════════════════════════════════════════════════
    //  State
    // ═══════════════════════════════════════════════════════

    private Officers selectedOfficer;

    // ── CREATE fields ──────────────────────────────────────
    private Integer selectedUserId;
    private String  designation;
    private Integer departmentId;
    private Integer zoneId;
    private Integer wardId;
    private String  status;

    // ── EDIT fields ────────────────────────────────────────
    private String newDesignation;

    // ═══════════════════════════════════════════════════════
    //  Validation constants
    // ═══════════════════════════════════════════════════════

    /** Values shown in the ADD dialog designation dropdown. */
    private static final List<String> CREATE_DESIGNATIONS = List.of(
            "WARD", "ZONE", "CORPORATE"
    );

    /** Values shown in the EDIT dialog designation dropdown. */
    private static final List<String> EDIT_DESIGNATIONS = List.of(
            "WARD_OFFICER", "ZONE_OFFICER",
            "ZONE_ADMIN",   "WARD_ADMIN",
            "CORPORATE_OFFICER"
    );

    // ═══════════════════════════════════════════════════════
    //  LIFECYCLE
    // ═══════════════════════════════════════════════════════

    @PostConstruct
    public void init() {
        loadData();

        // Safe default so the read-only box in the edit dialog
        // never throws a NullPointerException on first render.
        selectedOfficer = new Officers();
        selectedOfficer.setUserId(new Users());

        status = "ACTIVE";
    }

    // ── Reload helpers ─────────────────────────────────────

    private void loadData() {
        officers       = officerBean.getAllOfficers();
        availableUsers = officerBean.getAvailableUsers();
        departments    = masterService.getAllDepartments();
        zones          = masterService.getZones(1);
        wards          = masterService.getWards(1);
    }

    // ═══════════════════════════════════════════════════════
    //  DIALOG PREPARE
    // ═══════════════════════════════════════════════════════

    /** Called by the "Add Officer" command button (process="@this"). */
    public void prepareCreate() {
        selectedUserId = null;
        designation    = null;
        departmentId   = null;
        zoneId         = null;
        wardId         = null;
        status         = "ACTIVE";
    }

    // ═══════════════════════════════════════════════════════
    //  VALIDATION — CREATE
    // ═══════════════════════════════════════════════════════

    /**
     * Validates every field in the Add Officer dialog.
     *
     * Component IDs in the XHTML are:
     *   officerForm:addUser
     *   officerForm:addDesignation
     *   officerForm:addDepartment
     *   officerForm:addZone
     *   officerForm:addWard
     *   officerForm:addStatus
     *
     * The prefix "officerForm:add" + field suffix must match exactly.
     */
    private boolean validateCreate() {

        boolean      ok = true;
        FacesContext fc = FacesContext.getCurrentInstance();
        final String P  = "officerForm:add"; // matches XHTML id prefix

        // ── 1. User ───────────────────────────────────────
        if (selectedUserId == null) {
            addError(fc, P + "User",
                    "Please select a User.");
            ok = false;

        } else {
            // Duplicate check — user must not already be an officer
            boolean alreadyOfficer = officers.stream()
                    .anyMatch(o -> o.getUserId() != null
                            && o.getUserId().getUserId()
                                .equals(selectedUserId));

            if (alreadyOfficer) {
                addError(fc, P + "User",
                        "This user is already assigned as an officer.");
                ok = false;
            }
        }

        // ── 2. Designation ────────────────────────────────
        if (designation == null || designation.trim().isEmpty()) {
            addError(fc, P + "Designation",
                    "Please select a Designation.");
            ok = false;

        } else if (!CREATE_DESIGNATIONS.contains(designation.trim())) {
            addError(fc, P + "Designation",
                    "Invalid designation selected.");
            ok = false;
        }

        // ── 3. Department ─────────────────────────────────
        if (departmentId == null) {
            addError(fc, P + "Department",
                    "Please select a Department.");
            ok = false;
        }

        // ── 4. Zone ───────────────────────────────────────
        if (zoneId == null) {
            addError(fc, P + "Zone",
                    "Please select a Zone.");
            ok = false;
        }

        // ── 5. Ward ───────────────────────────────────────
        if (wardId == null) {
            addError(fc, P + "Ward",
                    "Please select a Ward.");
            ok = false;
        }

        // ── 6. Status ─────────────────────────────────────
        if (status == null || status.trim().isEmpty()) {
            addError(fc, P + "Status",
                    "Status is required.");
            ok = false;

        } else if (!"ACTIVE".equals(status)
                && !"INACTIVE".equals(status)) {
            addError(fc, P + "Status",
                    "Invalid status value.");
            ok = false;
        }

        return ok;
    }

    // ═══════════════════════════════════════════════════════
    //  VALIDATION — EDIT
    // ═══════════════════════════════════════════════════════

    /**
     * Validates the Edit Officer dialog.
     * Only designation is editable.
     *
     * Component ID in XHTML: officerForm:editDesignation
     */
    private boolean validateEdit() {

        boolean      ok = true;
        FacesContext fc = FacesContext.getCurrentInstance();
        final String P  = "officerForm:edit"; // matches XHTML id prefix

        // ── Guard — officer must be selected ──────────────
        if (selectedOfficer == null
                || selectedOfficer.getOfficerId() == null) {
            addError(fc, P + "Designation",
                    "No officer selected. Please try again.");
            return false;
        }

        // ── 1. Designation ────────────────────────────────
        if (newDesignation == null
                || newDesignation.trim().isEmpty()) {
            addError(fc, P + "Designation",
                    "Please select a Designation.");
            ok = false;

        } else if (!EDIT_DESIGNATIONS.contains(
                        newDesignation.trim())) {
            addError(fc, P + "Designation",
                    "Invalid designation selected.");
            ok = false;
        }

        return ok;
    }

    // ═══════════════════════════════════════════════════════
    //  MESSAGE HELPERS
    // ═══════════════════════════════════════════════════════

    /**
     * Attaches an error FacesMessage to a specific component and
     * marks the lifecycle as failed so the client receives
     * args.validationFailed = true → dialog stays open.
     */
    private void addError(FacesContext fc,
                          String clientId,
                          String detail) {
        fc.addMessage(clientId,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Validation Error",
                        detail));
        fc.validationFailed(); // keeps dialog open via oncomplete check
    }

    private void addSuccess(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        summary,
                        detail));
    }

    // ═══════════════════════════════════════════════════════
    //  CRUD ACTIONS
    // ═══════════════════════════════════════════════════════

    // ── CREATE ────────────────────────────────────────────

    /** Called by "Save Officer" button in the Add dialog. */
    public void createOfficer() {

        System.out.println("CREATE OFFICER CLICKED");
        System.out.println("selectedUserId = " + selectedUserId);
        System.out.println("designation    = " + designation);
        System.out.println("departmentId   = " + departmentId);
        System.out.println("zoneId         = " + zoneId);
        System.out.println("wardId         = " + wardId);
        System.out.println("status         = " + status);

        // Run validation — stops here if any field is invalid
        if (!validateCreate()) {
            return;
        }

        officerBean.createOfficer(
                selectedUserId,
                designation,
                departmentId,
                zoneId,
                wardId,
                status);

        loadData(); // refresh officers + availableUsers

        addSuccess("Success", "Officer created successfully.");

        prepareCreate(); // reset fields for next use
    }

    // ── EDIT ──────────────────────────────────────────────

    /**
     * Called by the "Edit" row button.
     * Populates the edit-dialog fields from the selected officer.
     */
    public void editOfficer(Officers officer) {

        System.out.println("EDIT CLICKED");
        System.out.println("Officer Id = " + officer.getOfficerId());

        this.selectedOfficer  = officer;
        this.newDesignation   = officer.getDesignation();
    }

    /** Called by "Update Officer" button in the Edit dialog. */
    public void saveOfficer() {

        System.out.println("SAVE CLICKED — newDesignation = "
                + newDesignation);

        // Run validation — stops here if any field is invalid
        if (!validateEdit()) {
            return;
        }

        officerBean.updateDesignation(
                selectedOfficer.getOfficerId(),
                newDesignation.trim());

        loadData();

        addSuccess("Updated",
                "Officer designation updated successfully.");
    }

    // ── STATUS TOGGLES ────────────────────────────────────

    /** Set officer status → INACTIVE. */
    public void deactivate(Integer officerId) {

        Officers officer = officerBean.getOfficerById(officerId);

        if (officer != null) {
            officer.setStatus("INACTIVE");
            officerBean.updateOfficer(officer);
            loadData();
            addSuccess("Deactivated",
                    "Officer has been deactivated.");
        }
    }

    /** Set officer status → ACTIVE. */
    public void activate(Integer officerId) {

        Officers officer = officerBean.getOfficerById(officerId);

        if (officer != null) {
            officer.setStatus("ACTIVE");
            officerBean.updateOfficer(officer);
            loadData();
            addSuccess("Activated",
                    "Officer has been activated.");
        }
    }

    // ═══════════════════════════════════════════════════════
    //  Getters / Setters
    // ═══════════════════════════════════════════════════════

    public OfficerBeanLocal getOfficerBean() {
        return officerBean;
    }

    public void setOfficerBean(OfficerBeanLocal officerBean) {
        this.officerBean = officerBean;
    }

    public MasterDataService getMasterService() {
        return masterService;
    }

    public void setMasterService(MasterDataService masterService) {
        this.masterService = masterService;
    }

    public List<Officers> getOfficers() {
        return officers;
    }

    public void setOfficers(List<Officers> officers) {
        this.officers = officers;
    }

    public List<Users> getAvailableUsers() {
        return availableUsers;
    }

    public void setAvailableUsers(List<Users> availableUsers) {
        this.availableUsers = availableUsers;
    }

    public List<Departments> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Departments> departments) {
        this.departments = departments;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public void setZones(List<Zone> zones) {
        this.zones = zones;
    }

    public List<Ward> getWards() {
        return wards;
    }

    public void setWards(List<Ward> wards) {
        this.wards = wards;
    }

    public Officers getSelectedOfficer() {
        return selectedOfficer;
    }

    public void setSelectedOfficer(Officers selectedOfficer) {
        this.selectedOfficer = selectedOfficer;
    }

    public Integer getSelectedUserId() {
        return selectedUserId;
    }

    public void setSelectedUserId(Integer selectedUserId) {
        this.selectedUserId = selectedUserId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNewDesignation() {
        return newDesignation;
    }

    public void setNewDesignation(String newDesignation) {
        System.out.println("setNewDesignation called: " + newDesignation);
        this.newDesignation = newDesignation;
    }
}
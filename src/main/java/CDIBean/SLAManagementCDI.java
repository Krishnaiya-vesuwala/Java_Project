package CDIBean;

import EJB.AdminBeanLocal;
import Entity.ComplaintCategory;
import Entity.SlaRules;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named("slaManagementCDI")
@ViewScoped
public class SLAManagementCDI implements Serializable {

    @EJB
    private AdminBeanLocal adminService;

    private List<SlaRules>          slaRules;
    private List<ComplaintCategory> categories;

    private SlaRules selectedSla;

    /* CREATE fields */
    private Integer newCategoryId;
    private Integer newMaxDays;
    private String  newLevel;

    /* EDIT fields */
    private Integer editMaxDays;
    private String  editLevel;

    // Valid escalation levels
    private static final List<String> VALID_LEVELS =
            List.of("WARD_OFFICER", "ZONE_OFFICER", "CORPORATE_ADMIN");

    private static final int MIN_DAYS =   1;
    private static final int MAX_DAYS = 365;

    // ═══════════════════════════════════════════════════════
    //  LIFECYCLE
    // ═══════════════════════════════════════════════════════

    @PostConstruct
    public void init() {
        loadData();
        categories  = adminService.getAllCategory();
        selectedSla = new SlaRules();
    }

    public void loadData() {
        slaRules = adminService.getAllSlaRules();
    }

    /** Called by "Add SLA Rule" button — resets add-dialog fields. */
    public void prepareCreate() {
        newCategoryId = null;
        newMaxDays    = null;
        newLevel      = null;
    }

    // ═══════════════════════════════════════════════════════
    //  VALIDATION
    // ═══════════════════════════════════════════════════════

    /**
     * Validates SLA fields and posts FacesMessages to the correct
     * component IDs so p:message tags show inline errors.
     * Calls fc.validationFailed() → args.validationFailed = true
     * on the client → dialog stays open.
     *
     * isEdit = false → prefix "slaForm:add..."
     * isEdit = true  → prefix "slaForm:edit..."
     */
    private boolean validateSla(Integer categoryId,
                                String  level,
                                Integer maxDays,
                                Integer excludeSlaId,
                                boolean isEdit) {

        boolean      ok = true;
        FacesContext fc = FacesContext.getCurrentInstance();

        String prefix = isEdit ? "slaForm:edit" : "slaForm:add";

        // ── 1. Category (Add only) ────────────────────────
        if (!isEdit) {
            if (categoryId == null) {
                addError(fc, prefix + "Category",
                        "Please select a Category.");
                ok = false;
            }
        }

        // ── 2. Escalation Level ───────────────────────────
        if (level == null || level.trim().isEmpty()) {
            addError(fc, prefix + "Level",
                    "Please select an Escalation Level.");
            ok = false;
        } else if (!VALID_LEVELS.contains(level)) {
            addError(fc, prefix + "Level",
                    "Invalid escalation level selected.");
            ok = false;
        } else {
            // Duplicate check: same category + same level
            // (only meaningful on create; on edit the category is fixed)
            if (!isEdit && categoryId != null) {
                for (SlaRules r : slaRules) {
                    if (excludeSlaId != null
                            && r.getSlaId().equals(excludeSlaId)) {
                        continue;
                    }
                    if (r.getCategoryId() != null
                            && r.getCategoryId().getCategoryId()
                                                .equals(categoryId)
                            && level.equals(r.getLevel())) {

                        addError(fc, prefix + "Level",
                                "An SLA rule for this category and "
                                + "level already exists.");
                        ok = false;
                        break;
                    }
                }
            }
        }

        // ── 3. Max Resolution Days ────────────────────────
        if (maxDays == null) {
            addError(fc, prefix + "MaxDays",
                    "Max Resolution Days is required.");
            ok = false;
        } else if (maxDays < MIN_DAYS) {
            addError(fc, prefix + "MaxDays",
                    "Max Resolution Days must be at least "
                    + MIN_DAYS + ".");
            ok = false;
        } else if (maxDays > MAX_DAYS) {
            addError(fc, prefix + "MaxDays",
                    "Max Resolution Days cannot exceed "
                    + MAX_DAYS + ".");
            ok = false;
        }

        return ok;
    }

    // ── Message helpers ───────────────────────────────────

    /**
     * Adds an error to a specific component AND marks validation
     * as failed so args.validationFailed = true on the client.
     * Same pattern as ZoneManagementCDI / WardManagementCDI.
     */
    private void addError(FacesContext fc,
                          String       clientId,
                          String       detail) {
        fc.addMessage(clientId,
                new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                 "Validation Error", detail));
        fc.validationFailed(); // ← keeps dialog open
    }

    private void addSuccess(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO,
                                 summary, detail));
    }

    // ═══════════════════════════════════════════════════════
    //  CRUD ACTIONS
    // ═══════════════════════════════════════════════════════

    /** Called by "Save Rule" button in the Add dialog. */
    public void createSlaRule() {

        System.out.println("CREATE SLA CLICKED");
        System.out.println("Category ID = " + newCategoryId);
        System.out.println("Level       = " + newLevel);
        System.out.println("Max Days    = " + newMaxDays);

        if (!validateSla(newCategoryId, newLevel, newMaxDays,
                         null, false)) {
            return; // validationFailed() set → dialog stays open
        }

        adminService.addSlaRules(
                newCategoryId,
                newMaxDays,
                newLevel);

        loadData();

        addSuccess("SLA Created",
                "SLA rule created successfully.");

        prepareCreate();
    }

    /** Called by "Edit" row button — populates edit-dialog fields. */
    public void editSlaRule(SlaRules sla) {

        selectedSla  = sla;
        editMaxDays  = sla.getMaxResolutionDays();
        editLevel    = sla.getLevel();

        System.out.println("Editing SLA ID = " + sla.getSlaId());
    }

    /** Called by "Update Rule" button in the Edit dialog. */
    public void updateSlaRule() {

        if (selectedSla == null || selectedSla.getSlaId() == null) {
            addError(FacesContext.getCurrentInstance(),
                     "slaForm:editMaxDays",
                     "No SLA rule selected for update.");
            return;
        }

        if (!validateSla(null, editLevel, editMaxDays,
                         selectedSla.getSlaId(), true)) {
            return; // validationFailed() set → dialog stays open
        }

        adminService.updateSlaRule(
                selectedSla.getSlaId(),
                editMaxDays,
                editLevel);

        loadData();

        addSuccess("SLA Updated", "SLA rule updated successfully.");
    }

    /** Hard-delete an SLA rule. */
    public void deleteSlaRule(Integer slaId) {
        adminService.deleteSlaRule(slaId);
        loadData();
        addSuccess("Deleted", "SLA rule deleted successfully.");
    }

    // ═══════════════════════════════════════════════════════
    //  Getters / Setters
    // ═══════════════════════════════════════════════════════

    public AdminBeanLocal getAdminService()                     { return adminService; }
    public void setAdminService(AdminBeanLocal adminService)    { this.adminService = adminService; }

    public List<SlaRules> getSlaRules()                         { return slaRules; }
    public void setSlaRules(List<SlaRules> slaRules)            { this.slaRules = slaRules; }

    public List<ComplaintCategory> getCategories()              { return categories; }
    public void setCategories(List<ComplaintCategory> c)        { this.categories = c; }

    public SlaRules getSelectedSla()                            { return selectedSla; }
    public void setSelectedSla(SlaRules selectedSla)            { this.selectedSla = selectedSla; }

    public Integer getNewCategoryId()                           { return newCategoryId; }
    public void setNewCategoryId(Integer newCategoryId)         { this.newCategoryId = newCategoryId; }

    public Integer getNewMaxDays()                              { return newMaxDays; }
    public void setNewMaxDays(Integer newMaxDays)               { this.newMaxDays = newMaxDays; }

    public String getNewLevel()                                 { return newLevel; }
    public void setNewLevel(String newLevel)                    { this.newLevel = newLevel; }

    public Integer getEditMaxDays()                             { return editMaxDays; }
    public void setEditMaxDays(Integer editMaxDays)             { this.editMaxDays = editMaxDays; }

    public String getEditLevel()                                { return editLevel; }
    public void setEditLevel(String editLevel)                  { this.editLevel = editLevel; }
}
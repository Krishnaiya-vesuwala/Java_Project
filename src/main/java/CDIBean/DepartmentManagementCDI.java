package CDIBean;

import EJB.AdminBeanLocal;
import Entity.Departments;
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
public class DepartmentManagementCDI implements Serializable {

    @EJB
    private AdminBeanLocal adminService;

    private List<Departments> departments;
    private Departments       selectedDepartment;

    // ── CREATE fields ──────────────────────────────────────
    private String newDepartmentName;
    private String newDescription;
    private String newStatus;

    // ── EDIT fields ────────────────────────────────────────
    private String editDepartmentName;
    private String editDescription;
    private String editStatus;

    // ── Validation constants ───────────────────────────────
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9 .&'\\-]+$");

    private static final Pattern DESC_PATTERN =
            Pattern.compile("^[a-zA-Z0-9 .,;:!?()&'\"\\-\\n\\r]+$");

    private static final int NAME_MIN = 3;
    private static final int NAME_MAX = 100;
    private static final int DESC_MAX = 500;

    // ═══════════════════════════════════════════════════════
    //  LIFECYCLE
    // ═══════════════════════════════════════════════════════

    @PostConstruct
    public void init() {
        loadData();
        selectedDepartment = new Departments();
        newStatus = "ACTIVE";
    }

    public void loadData() {
        departments = adminService.getAllDepartments();
    }

    // ── Prepare create ─────────────────────────────────────

    /** Called by "Add Department" button — resets create-dialog fields. */
    public void prepareCreate() {
        newDepartmentName = "";
        newDescription    = "";
        newStatus         = "ACTIVE";
    }

    // ═══════════════════════════════════════════════════════
    //  VALIDATION
    // ═══════════════════════════════════════════════════════

    /**
     * Validates Department Name, Description and Status.
     *
     * isEdit = false → component prefix "departmentForm:add"
     * isEdit = true  → component prefix "departmentForm:edit"
     *
     * excludeDeptId — the ID to skip during duplicate check (edit mode).
     */
    private boolean validateDepartment(String  deptName,
                                       String  description,
                                       String  status,
                                       Integer excludeDeptId,
                                       boolean isEdit) {

        boolean      ok = true;
        FacesContext fc = FacesContext.getCurrentInstance();
        String prefix   = isEdit
                          ? "departmentForm:edit"
                          : "departmentForm:add";

        // ── 1. Department Name ────────────────────────────
        if (deptName == null || deptName.trim().isEmpty()) {
            addError(fc, prefix + "DepartmentName",
                    "Department Name is required.");
            ok = false;

        } else {
            String trimmed = deptName.trim();

            if (trimmed.length() < NAME_MIN) {
                addError(fc, prefix + "DepartmentName",
                        "Department Name must be at least "
                        + NAME_MIN + " characters.");
                ok = false;

            } else if (trimmed.length() > NAME_MAX) {
                addError(fc, prefix + "DepartmentName",
                        "Department Name cannot exceed "
                        + NAME_MAX + " characters.");
                ok = false;
            }

            if (!NAME_PATTERN.matcher(trimmed).matches()) {
                addError(fc, prefix + "DepartmentName",
                        "Department Name can only contain letters, "
                        + "digits, spaces, hyphens, dots, "
                        + "apostrophes and '&'.");
                ok = false;
            }

            // Duplicate name check (case-insensitive)
            if (ok) {
                for (Departments d : departments) {
                    if (excludeDeptId != null
                            && d.getDepartmentId()
                                .equals(excludeDeptId)) {
                        continue; // skip the record being edited
                    }
                    if (d.getDepartmentName() != null
                            && d.getDepartmentName().trim()
                                .equalsIgnoreCase(trimmed)) {
                        addError(fc, prefix + "DepartmentName",
                                "A department with this name "
                                + "already exists.");
                        ok = false;
                        break;
                    }
                }
            }
        }

        // ── 2. Description (optional but validated if filled) ──
        if (description != null && !description.trim().isEmpty()) {
            String trimmedDesc = description.trim();

            if (trimmedDesc.length() > DESC_MAX) {
                addError(fc, prefix + "Description",
                        "Description cannot exceed "
                        + DESC_MAX + " characters.");
                ok = false;

            } else if (!DESC_PATTERN.matcher(trimmedDesc).matches()) {
                addError(fc, prefix + "Description",
                        "Description contains invalid characters.");
                ok = false;
            }
        }

        // ── 3. Status ─────────────────────────────────────
        if (status == null || status.trim().isEmpty()) {
            addError(fc, prefix + "Status",
                    "Status is required.");
            ok = false;

        } else if (!"ACTIVE".equals(status)
                && !"INACTIVE".equals(status)) {
            addError(fc, prefix + "Status",
                    "Invalid status value.");
            ok = false;
        }

        return ok;
    }

    // ── Message helpers ────────────────────────────────────

    private void addError(FacesContext fc,
                          String clientId,
                          String detail) {
        fc.addMessage(clientId,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Validation Error",
                        detail));
        fc.validationFailed(); // keeps dialog open
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

    /** Called by "Save Department" in the Add dialog. */
    public void createDepartment() {

        System.out.println("CREATE DEPARTMENT CLICKED");
        System.out.println("Name   = " + newDepartmentName);
        System.out.println("Desc   = " + newDescription);
        System.out.println("Status = " + newStatus);

        if (!validateDepartment(
                newDepartmentName,
                newDescription,
                newStatus,
                null,
                false)) {
            return; // validationFailed() set → dialog stays open
        }

        adminService.createDepartment(
                newDepartmentName.trim(),
                newDescription  != null
                        ? newDescription.trim() : "",
                newStatus);

        loadData();

        addSuccess("Success",
                "Department '" + newDepartmentName.trim()
                + "' created successfully.");

        prepareCreate(); // reset fields
    }

    /** Called by "Edit" row button — populates edit-dialog fields. */
    public void editDepartment(Departments dept) {

        System.out.println("EDIT DEPT = " + dept.getDepartmentId());

        selectedDepartment = dept;
        editDepartmentName = dept.getDepartmentName();
        editDescription    = dept.getDescription();
        editStatus         = dept.getStatus();
    }

    /** Called by "Update Department" in the Edit dialog. */
    public void updateDepartment() {

        if (selectedDepartment == null
                || selectedDepartment.getDepartmentId() == null) {
            addError(FacesContext.getCurrentInstance(),
                     "departmentForm:editDepartmentName",
                     "No department selected for update.");
            return;
        }

        if (!validateDepartment(
                editDepartmentName,
                editDescription,
                editStatus,
                selectedDepartment.getDepartmentId(),
                true)) {
            return;
        }

        adminService.updateDepartment(
                selectedDepartment.getDepartmentId(),
                editDepartmentName.trim(),
                editDescription != null
                        ? editDescription.trim() : "",
                editStatus);

        loadData();

        addSuccess("Updated",
                "Department updated successfully.");
    }

    /** Hard-delete a department. */
    public void deleteDepartment(Integer id) {

        System.out.println("Deleting Department: " + id);

        adminService.deleteDepartment(id);

        loadData();

        addSuccess("Deleted",
                "Department deleted successfully.");
    }

    /** Set department status → ACTIVE. */
    public void activate(Integer id) {

        Departments dept = departments.stream()
                .filter(d -> d.getDepartmentId().equals(id))
                .findFirst()
                .orElse(null);

        if (dept != null) {
            adminService.updateDepartment(
                    id,
                    dept.getDepartmentName(),
                    dept.getDescription(),
                    "ACTIVE");
            loadData();
            addSuccess("Activated",
                    "Department '"
                    + dept.getDepartmentName()
                    + "' has been activated.");
        }
    }

    /** Set department status → INACTIVE. */
    public void deactivate(Integer id) {

        Departments dept = departments.stream()
                .filter(d -> d.getDepartmentId().equals(id))
                .findFirst()
                .orElse(null);

        if (dept != null) {
            adminService.updateDepartment(
                    id,
                    dept.getDepartmentName(),
                    dept.getDescription(),
                    "INACTIVE");
            loadData();
            addSuccess("Deactivated",
                    "Department '"
                    + dept.getDepartmentName()
                    + "' has been deactivated.");
        }
    }

    // ═══════════════════════════════════════════════════════
    //  Getters / Setters
    // ═══════════════════════════════════════════════════════

    public AdminBeanLocal getAdminService() {
        return adminService;
    }

    public void setAdminService(AdminBeanLocal adminService) {
        this.adminService = adminService;
    }

    public List<Departments> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Departments> departments) {
        this.departments = departments;
    }

    public Departments getSelectedDepartment() {
        return selectedDepartment;
    }

    public void setSelectedDepartment(Departments selectedDepartment) {
        this.selectedDepartment = selectedDepartment;
    }

    public String getNewDepartmentName() {
        return newDepartmentName;
    }

    public void setNewDepartmentName(String newDepartmentName) {
        this.newDepartmentName = newDepartmentName;
    }

    public String getNewDescription() {
        return newDescription;
    }

    public void setNewDescription(String newDescription) {
        this.newDescription = newDescription;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public String getEditDepartmentName() {
        return editDepartmentName;
    }

    public void setEditDepartmentName(String editDepartmentName) {
        this.editDepartmentName = editDepartmentName;
    }

    public String getEditDescription() {
        return editDescription;
    }

    public void setEditDescription(String editDescription) {
        this.editDescription = editDescription;
    }

    public String getEditStatus() {
        return editStatus;
    }

    public void setEditStatus(String editStatus) {
        this.editStatus = editStatus;
    }
}
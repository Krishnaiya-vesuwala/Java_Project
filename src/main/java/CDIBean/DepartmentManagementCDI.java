package CDIBean;

import Client.RestClient;
import Entity.Departments;
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

@Named
@ViewScoped
public class DepartmentManagementCDI implements Serializable {

    private RestClient restClient = new RestClient();

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

    // Token
    private String token;

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

        try {

            // ── Get token from session ───────────────
            Map<String, Object> session = FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap();

            token = (String) session.get("token");

            System.out.println("[DepartmentManagementCDI] token = "
                    + token);

            if (token == null || token.isEmpty()) {
                System.err.println(
                        "[DepartmentManagementCDI] Token is NULL!");
                return;
            }

            loadData();

            selectedDepartment = new Departments();
            newStatus          = "ACTIVE";

        } catch (Exception e) {
            System.err.println(
                    "[DepartmentManagementCDI] init error: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    // ═══════════════════════════════════════════════════════
    //  LOAD DATA
    // ═══════════════════════════════════════════════════════

    public void loadData() {

        try {

            Response rs = restClient.getAllDepartments(
                    Response.class, token);

            System.out.println(
                    "[DepartmentManagementCDI] getAllDepartments status: "
                    + rs.getStatus());

            if (rs.getStatus() == 200) {
                departments = rs.readEntity(
                        new GenericType<List<Departments>>() {});
                System.out.println(
                        "[DepartmentManagementCDI] departments loaded: "
                        + (departments != null
                                ? departments.size() : 0));
            } else {
                departments = new ArrayList<>();
                System.err.println(
                        "[DepartmentManagementCDI] getAllDepartments"
                        + " failed. Status: " + rs.getStatus());
            }

        } catch (Exception e) {
            System.err.println(
                    "[DepartmentManagementCDI] loadData error: "
                    + e.getMessage());
            e.printStackTrace();
            departments = new ArrayList<>();
        }
    }

    // ═══════════════════════════════════════════════════════
    //  PREPARE
    // ═══════════════════════════════════════════════════════

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
     * isEdit = false → component prefix "departmentForm:add"
     * isEdit = true  → component prefix "departmentForm:edit"
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
            if (ok && departments != null) {
                for (Departments d : departments) {
                    if (excludeDeptId != null
                            && d.getDepartmentId()
                                .equals(excludeDeptId)) {
                        continue;
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
        fc.validationFailed();
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

        System.out.println("[DepartmentManagementCDI] CREATE CLICKED");
        System.out.println("[DepartmentManagementCDI] Name   = "
                + newDepartmentName);
        System.out.println("[DepartmentManagementCDI] Desc   = "
                + newDescription);
        System.out.println("[DepartmentManagementCDI] Status = "
                + newStatus);

        if (!validateDepartment(
                newDepartmentName,
                newDescription,
                newStatus,
                null,
                false)) {
            return;
        }

        try {

            restClient.createDepartment(
                    newDepartmentName.trim(),
                    newDescription != null
                            ? newDescription.trim() : "",
                    newStatus,token);

            System.out.println(
                    "[DepartmentManagementCDI] createDepartment"
                    + " REST called.");

            loadData();

            addSuccess("Success",
                    "Department '" + newDepartmentName.trim()
                    + "' created successfully.");

            prepareCreate();

        } catch (Exception e) {
            System.err.println(
                    "[DepartmentManagementCDI] createDepartment error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    "departmentForm:addDepartmentName",
                    "Failed to create department: "
                    + e.getMessage());
        }
    }

    /** Called by "Edit" row button — populates edit-dialog fields. */
    public void editDepartment(Departments dept) {

        System.out.println("[DepartmentManagementCDI] EDIT DEPT = "
                + dept.getDepartmentId());

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

        try {

            restClient.updateDepartment(
                    String.valueOf(
                            selectedDepartment.getDepartmentId()),
                    editDepartmentName.trim(),
                    editDescription != null
                            ? editDescription.trim() : "",
                    editStatus,token);

            System.out.println(
                    "[DepartmentManagementCDI] updateDepartment"
                    + " REST called.");

            loadData();

            addSuccess("Updated",
                    "Department updated successfully.");

        } catch (Exception e) {
            System.err.println(
                    "[DepartmentManagementCDI] updateDepartment error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    "departmentForm:editDepartmentName",
                    "Failed to update department: "
                    + e.getMessage());
        }
    }

    /** Hard-delete a department. */
    public void deleteDepartment(Integer id) {

        System.out.println(
                "[DepartmentManagementCDI] Deleting Department: " + id);

        try {

            restClient.deleteDepartment(String.valueOf(id),token);

            System.out.println(
                    "[DepartmentManagementCDI] deleteDepartment"
                    + " REST called. id=" + id);

            loadData();

            addSuccess("Deleted",
                    "Department deleted successfully.");

        } catch (Exception e) {
            System.err.println(
                    "[DepartmentManagementCDI] deleteDepartment error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    null,
                    "Failed to delete department: "
                    + e.getMessage());
        }
    }

    /** Set department status → ACTIVE. */
    public void activate(Integer id) {

        try {

            // Find dept in list to get current values
            Departments dept = null;
            if (departments != null) {
                dept = departments.stream()
                        .filter(d -> d.getDepartmentId().equals(id))
                        .findFirst()
                        .orElse(null);
            }

            if (dept == null) {
                addError(FacesContext.getCurrentInstance(),
                        null,
                        "Department not found.");
                return;
            }

            restClient.updateDepartment(
                    String.valueOf(id),
                    dept.getDepartmentName(),
                    dept.getDescription() != null
                            ? dept.getDescription() : "",
                    "ACTIVE",token);

            System.out.println(
                    "[DepartmentManagementCDI] activate REST called."
                    + " id=" + id);

            loadData();

            addSuccess("Activated",
                    "Department '"
                    + dept.getDepartmentName()
                    + "' has been activated.");

        } catch (Exception e) {
            System.err.println(
                    "[DepartmentManagementCDI] activate error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    null,
                    "Failed to activate department: "
                    + e.getMessage());
        }
    }

    /** Set department status → INACTIVE. */
    public void deactivate(Integer id) {

        try {

            // Find dept in list to get current values
            Departments dept = null;
            if (departments != null) {
                dept = departments.stream()
                        .filter(d -> d.getDepartmentId().equals(id))
                        .findFirst()
                        .orElse(null);
            }

            if (dept == null) {
                addError(FacesContext.getCurrentInstance(),
                        null,
                        "Department not found.");
                return;
            }

            restClient.updateDepartment(
                    String.valueOf(id),
                    dept.getDepartmentName(),
                    dept.getDescription() != null
                            ? dept.getDescription() : "",
                    "INACTIVE",token);

            System.out.println(
                    "[DepartmentManagementCDI] deactivate REST called."
                    + " id=" + id);

            loadData();

            addSuccess("Deactivated",
                    "Department '"
                    + dept.getDepartmentName()
                    + "' has been deactivated.");

        } catch (Exception e) {
            System.err.println(
                    "[DepartmentManagementCDI] deactivate error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    null,
                    "Failed to deactivate department: "
                    + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════
    //  Getters / Setters
    // ═══════════════════════════════════════════════════════

    public List<Departments> getDepartments()               { return departments; }
    public void setDepartments(List<Departments> d)         { this.departments = d; }

    public Departments getSelectedDepartment()              { return selectedDepartment; }
    public void setSelectedDepartment(Departments d)        { this.selectedDepartment = d; }

    public String getNewDepartmentName()                    { return newDepartmentName; }
    public void setNewDepartmentName(String n)              { this.newDepartmentName = n; }

    public String getNewDescription()                       { return newDescription; }
    public void setNewDescription(String d)                 { this.newDescription = d; }

    public String getNewStatus()                            { return newStatus; }
    public void setNewStatus(String s)                      { this.newStatus = s; }

    public String getEditDepartmentName()                   { return editDepartmentName; }
    public void setEditDepartmentName(String n)             { this.editDepartmentName = n; }

    public String getEditDescription()                      { return editDescription; }
    public void setEditDescription(String d)                { this.editDescription = d; }

    public String getEditStatus()                           { return editStatus; }
    public void setEditStatus(String s)                     { this.editStatus = s; }

    public String getToken()                                { return token; }
    public void setToken(String t)                          { this.token = t; }
}
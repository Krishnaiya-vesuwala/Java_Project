package CDIBean;

import Client.RestClient;
import Entity.ComplaintCategory;
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

@Named("categoryManagementCDI")
@ViewScoped
public class CategoryManagementCDI implements Serializable {

    private RestClient restClient = new RestClient();

    private List<ComplaintCategory> categories;
    private List<Departments>       departments;

    private ComplaintCategory selectedCategory;

    // ── Stores the ID being edited (survives loadData()) ──
    private Integer selectedCategoryId;

    // ── CREATE fields ──────────────────────────────────────
    private String  newCategoryName;
    private Integer newDepartmentId;

    // ── EDIT fields ────────────────────────────────────────
    private String  editCategoryName;
    private Integer editDepartmentId;

    // ── Token ──────────────────────────────────────────────
    private String token;

    // ── Validation constants ───────────────────────────────
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9 .&'\\-/]+$");

    private static final int NAME_MIN = 3;
    private static final int NAME_MAX = 100;

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

            System.out.println("[CategoryManagementCDI] token = "
                    + token);

            if (token == null || token.isEmpty()) {
                System.err.println(
                        "[CategoryManagementCDI] Token is NULL!");
                return;
            }

            loadData();
            loadDepartments();

            selectedCategory   = new ComplaintCategory();
            selectedCategoryId = null;

        } catch (Exception e) {
            System.err.println(
                    "[CategoryManagementCDI] init error: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    // ═══════════════════════════════════════════════════════
    //  LOAD DATA
    // ═══════════════════════════════════════════════════════

    public void loadData() {

        try {

            Response rs = restClient.getAllCategories(
                    Response.class, token);

            System.out.println(
                    "[CategoryManagementCDI] getAllCategories status: "
                    + rs.getStatus());

            if (rs.getStatus() == 200) {
                categories = rs.readEntity(
                        new GenericType<List<ComplaintCategory>>() {});
                System.out.println(
                        "[CategoryManagementCDI] categories loaded: "
                        + (categories != null
                                ? categories.size() : 0));
            } else {
                categories = new ArrayList<>();
                System.err.println(
                        "[CategoryManagementCDI] getAllCategories"
                        + " failed. Status: " + rs.getStatus());
            }

        } catch (Exception e) {
            System.err.println(
                    "[CategoryManagementCDI] loadData error: "
                    + e.getMessage());
            e.printStackTrace();
            categories = new ArrayList<>();
        }
    }

    public void loadDepartments() {

        try {

            Response rs = restClient.getAllDepartments(
                    Response.class, token);

            System.out.println(
                    "[CategoryManagementCDI] getAllDepartments status: "
                    + rs.getStatus());

            if (rs.getStatus() == 200) {
                departments = rs.readEntity(
                        new GenericType<List<Departments>>() {});
                System.out.println(
                        "[CategoryManagementCDI] departments loaded: "
                        + (departments != null
                                ? departments.size() : 0));
            } else {
                departments = new ArrayList<>();
                System.err.println(
                        "[CategoryManagementCDI] getAllDepartments"
                        + " failed. Status: " + rs.getStatus());
            }

        } catch (Exception e) {
            System.err.println(
                    "[CategoryManagementCDI] loadDepartments error: "
                    + e.getMessage());
            e.printStackTrace();
            departments = new ArrayList<>();
        }
    }

    // ═══════════════════════════════════════════════════════
    //  DIALOG PREPARE
    // ═══════════════════════════════════════════════════════

    /** Called by "Add Category" button — resets create fields. */
    public void prepareCreate() {
        newCategoryName = "";
        newDepartmentId = null;
    }

    /**
     * Called by "Edit" row button (process="@this").
     * Populates edit-dialog fields from the selected row.
     */
    public void editCategory(ComplaintCategory category) {

        System.out.println("[CategoryManagementCDI] EDIT CATEGORY ID = "
                + category.getCategoryId());

        selectedCategory   = category;
        selectedCategoryId = category.getCategoryId();

        editCategoryName = category.getCategoryName();

        editDepartmentId = (category.getDepartmentId() != null)
                ? category.getDepartmentId().getDepartmentId()
                : null;

        System.out.println("[CategoryManagementCDI] editCategoryName = "
                + editCategoryName);
        System.out.println("[CategoryManagementCDI] editDepartmentId = "
                + editDepartmentId);
    }

    // ═══════════════════════════════════════════════════════
    //  VALIDATION
    // ═══════════════════════════════════════════════════════

    /**
     * Validates Category Name and Department.
     *
     * isEdit = false → component prefix "categoryForm:add"
     * isEdit = true  → component prefix "categoryForm:edit"
     *
     * excludeCategoryId — ID to skip in duplicate check (edit mode).
     */
    private boolean validateCategory(String  categoryName,
                                     Integer departmentId,
                                     Integer excludeCategoryId,
                                     boolean isEdit) {

        boolean      ok = true;
        FacesContext fc = FacesContext.getCurrentInstance();
        String prefix   = isEdit ? "categoryForm:edit"
                                 : "categoryForm:add";

        // ── 1. Category Name ──────────────────────────────
        if (categoryName == null
                || categoryName.trim().isEmpty()) {
            addError(fc, prefix + "CategoryName",
                    "Category Name is required.");
            ok = false;

        } else {

            String t = categoryName.trim();

            if (t.length() < NAME_MIN) {
                addError(fc, prefix + "CategoryName",
                        "Category Name must be at least "
                        + NAME_MIN + " characters.");
                ok = false;

            } else if (t.length() > NAME_MAX) {
                addError(fc, prefix + "CategoryName",
                        "Category Name cannot exceed "
                        + NAME_MAX + " characters.");
                ok = false;
            }

            if (!NAME_PATTERN.matcher(t).matches()) {
                addError(fc, prefix + "CategoryName",
                        "Category Name can only contain "
                        + "letters, digits, spaces, hyphens, "
                        + "dots, slashes, apostrophes and '&'.");
                ok = false;
            }

            // Duplicate check — same name in same department
            // (case-insensitive, skip self in edit mode)
            if (ok && departmentId != null
                    && categories != null) {
                for (ComplaintCategory cat : categories) {
                    if (excludeCategoryId != null
                            && cat.getCategoryId()
                                  .equals(excludeCategoryId)) {
                        continue; // skip the record being edited
                    }
                    boolean sameDept =
                            cat.getDepartmentId() != null
                            && cat.getDepartmentId()
                                  .getDepartmentId()
                                  .equals(departmentId);
                    boolean sameName =
                            cat.getCategoryName() != null
                            && cat.getCategoryName().trim()
                                  .equalsIgnoreCase(t);

                    if (sameDept && sameName) {
                        addError(fc, prefix + "CategoryName",
                                "A category with this name "
                                + "already exists in the "
                                + "selected department.");
                        ok = false;
                        break;
                    }
                }
            }
        }

        // ── 2. Department ─────────────────────────────────
        if (departmentId == null) {
            addError(fc, prefix + "DepartmentId",
                    "Please select a Department.");
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
                        "Validation Error", detail));
        fc.validationFailed(); // keeps dialog open
    }

    private void addSuccess(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        summary, detail));
    }

    // ═══════════════════════════════════════════════════════
    //  CRUD ACTIONS
    // ═══════════════════════════════════════════════════════

    /** Called by "Save Category" in the Add dialog. */
    public void createCategory() {

        System.out.println("[CategoryManagementCDI] CREATE CLICKED");
        System.out.println("[CategoryManagementCDI] name   = "
                + newCategoryName);
        System.out.println("[CategoryManagementCDI] deptId = "
                + newDepartmentId);

        if (!validateCategory(
                newCategoryName, newDepartmentId,
                null, false)) {
            return; // validationFailed() set → dialog stays open
        }

        try {

            restClient.createCategory(
                    newCategoryName.trim(),
                    String.valueOf(newDepartmentId),
                    token);

            System.out.println(
                    "[CategoryManagementCDI] createCategory"
                    + " REST called.");

            loadData();

            addSuccess("Success",
                    "Category '"
                    + newCategoryName.trim()
                    + "' created successfully.");

            prepareCreate(); // reset fields

        } catch (Exception e) {
            System.err.println(
                    "[CategoryManagementCDI] createCategory error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    "categoryForm:addCategoryName",
                    "Failed to create category: "
                    + e.getMessage());
        }
    }

    /** Called by "Update Category" in the Edit dialog. */
    public void updateCategory() {

        System.out.println("[CategoryManagementCDI] UPDATE CLICKED");
        System.out.println("[CategoryManagementCDI] categoryId = "
                + selectedCategoryId);
        System.out.println("[CategoryManagementCDI] name       = "
                + editCategoryName);
        System.out.println("[CategoryManagementCDI] deptId     = "
                + editDepartmentId);

        // ── Guard ─────────────────────────────────────────
        if (selectedCategoryId == null) {
            addError(FacesContext.getCurrentInstance(),
                    "categoryForm:editCategoryName",
                    "No category selected. "
                    + "Please close and try again.");
            return;
        }

        if (!validateCategory(
                editCategoryName, editDepartmentId,
                selectedCategoryId, true)) {
            return;
        }

        try {

            restClient.updateCategory(
                    String.valueOf(selectedCategoryId),
                    editCategoryName.trim(),
                    String.valueOf(editDepartmentId),
                    token);

            System.out.println(
                    "[CategoryManagementCDI] updateCategory"
                    + " REST called.");

            loadData();

            addSuccess("Updated",
                    "Category updated successfully.");

        } catch (Exception e) {
            System.err.println(
                    "[CategoryManagementCDI] updateCategory error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    "categoryForm:editCategoryName",
                    "Failed to update category: "
                    + e.getMessage());
        }
    }

    /** Hard-delete a category. */
    public void deleteCategory(Integer id) {

        System.out.println(
                "[CategoryManagementCDI] DELETE CATEGORY ID = " + id);

        try {

            restClient.deleteCategory(
                    String.valueOf(id), token);

            System.out.println(
                    "[CategoryManagementCDI] deleteCategory"
                    + " REST called. id=" + id);

            loadData();

            addSuccess("Deleted",
                    "Category deleted successfully.");

        } catch (Exception e) {
            System.err.println(
                    "[CategoryManagementCDI] deleteCategory error: "
                    + e.getMessage());
            e.printStackTrace();
            addError(FacesContext.getCurrentInstance(),
                    null,
                    "Failed to delete category: "
                    + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════
    //  Getters / Setters
    // ═══════════════════════════════════════════════════════

    public List<ComplaintCategory> getCategories()          { return categories; }
    public void setCategories(List<ComplaintCategory> c)    { this.categories = c; }

    public List<Departments> getDepartments()               { return departments; }
    public void setDepartments(List<Departments> d)         { this.departments = d; }

    public ComplaintCategory getSelectedCategory()          { return selectedCategory; }
    public void setSelectedCategory(ComplaintCategory c)    { this.selectedCategory = c; }

    public Integer getSelectedCategoryId()                  { return selectedCategoryId; }
    public void setSelectedCategoryId(Integer id)           { this.selectedCategoryId = id; }

    public String getNewCategoryName()                      { return newCategoryName; }
    public void setNewCategoryName(String s)                { this.newCategoryName = s; }

    public Integer getNewDepartmentId()                     { return newDepartmentId; }
    public void setNewDepartmentId(Integer id)              { this.newDepartmentId = id; }

    public String getEditCategoryName()                     { return editCategoryName; }
    public void setEditCategoryName(String s)               { this.editCategoryName = s; }

    public Integer getEditDepartmentId()                    { return editDepartmentId; }
    public void setEditDepartmentId(Integer id)             { this.editDepartmentId = id; }

    public String getToken()                                { return token; }
    public void setToken(String t)                          { this.token = t; }
}
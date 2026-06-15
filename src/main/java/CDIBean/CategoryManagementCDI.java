package CDIBean;

import EJB.AdminBeanLocal;
import Entity.ComplaintCategory;
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

@Named("categoryManagementCDI")
@ViewScoped
public class CategoryManagementCDI implements Serializable {

    @EJB
    private AdminBeanLocal adminService;

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
        loadData();
        departments      = adminService.getAllDepartments();
        selectedCategory = new ComplaintCategory();
        selectedCategoryId = null;
    }

    public void loadData() {
        categories = adminService.getAllCategory();
    }

    // ═══════════════════════════════════════════════════════
    //  DIALOG PREPARE
    // ═══════════════════════════════════════════════════════

    /** Called by "Add Category" button — resets create fields. */
    public void prepareCreate() {
        newCategoryName  = "";
        newDepartmentId  = null;
    }

    /**
     * Called by "Edit" row button (process="@this").
     * Populates edit-dialog fields from the selected row.
     */
    public void editCategory(ComplaintCategory category) {

        System.out.println("EDIT CATEGORY ID = "
                + category.getCategoryId());

        selectedCategory   = category;
        selectedCategoryId = category.getCategoryId();

        editCategoryName = category.getCategoryName();

        editDepartmentId = (category.getDepartmentId() != null)
                ? category.getDepartmentId().getDepartmentId()
                : null;

        System.out.println("editCategoryName = " + editCategoryName);
        System.out.println("editDepartmentId = " + editDepartmentId);
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
        String P = isEdit ? "categoryForm:edit"
                          : "categoryForm:add";

        // ── 1. Category Name ──────────────────────────────
        if (categoryName == null
                || categoryName.trim().isEmpty()) {
            addError(fc, P + "CategoryName",
                    "Category Name is required.");
            ok = false;

        } else {
            String t = categoryName.trim();

            if (t.length() < NAME_MIN) {
                addError(fc, P + "CategoryName",
                        "Category Name must be at least "
                        + NAME_MIN + " characters.");
                ok = false;

            } else if (t.length() > NAME_MAX) {
                addError(fc, P + "CategoryName",
                        "Category Name cannot exceed "
                        + NAME_MAX + " characters.");
                ok = false;
            }

            if (!NAME_PATTERN.matcher(t).matches()) {
                addError(fc, P + "CategoryName",
                        "Category Name can only contain "
                        + "letters, digits, spaces, hyphens, "
                        + "dots, slashes, apostrophes and '&'.");
                ok = false;
            }

            // Duplicate check — same name in same department
            // (case-insensitive, skip self in edit mode)
            if (ok && departmentId != null) {
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
                        addError(fc, P + "CategoryName",
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
            addError(fc, P + "DepartmentId",
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

        System.out.println("CREATE CATEGORY CLICKED");
        System.out.println("name  = " + newCategoryName);
        System.out.println("deptId = " + newDepartmentId);

        if (!validateCategory(
                newCategoryName, newDepartmentId,
                null, false)) {
            return; // validationFailed() set → dialog stays open
        }

        adminService.createCategory(
                newCategoryName.trim(),
                newDepartmentId);

        loadData();

        addSuccess("Success",
                "Category '"
                + newCategoryName.trim()
                + "' created successfully.");

        prepareCreate(); // reset fields
    }

    /** Called by "Update Category" in the Edit dialog. */
    public void updateCategory() {

        System.out.println("UPDATE CATEGORY CLICKED");
        System.out.println("categoryId = " + selectedCategoryId);
        System.out.println("name       = " + editCategoryName);
        System.out.println("deptId     = " + editDepartmentId);

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

        adminService.updateCategory(
                selectedCategoryId,
                editCategoryName.trim(),
                editDepartmentId);

        loadData();

        addSuccess("Updated",
                "Category updated successfully.");
    }

    /** Hard-delete a category. */
    public void deleteCategory(Integer id) {

        System.out.println("DELETE CATEGORY ID = " + id);

        adminService.deleteCategory(id);

        loadData();

        addSuccess("Deleted",
                "Category deleted successfully.");
    }

    // ═══════════════════════════════════════════════════════
    //  Getters / Setters
    // ═══════════════════════════════════════════════════════

    public AdminBeanLocal getAdminService() { return adminService; }
    public void setAdminService(AdminBeanLocal s) { this.adminService = s; }

    public List<ComplaintCategory> getCategories() { return categories; }
    public void setCategories(List<ComplaintCategory> c) { this.categories = c; }

    public List<Departments> getDepartments() { return departments; }
    public void setDepartments(List<Departments> d) { this.departments = d; }

    public ComplaintCategory getSelectedCategory() { return selectedCategory; }
    public void setSelectedCategory(ComplaintCategory c) { this.selectedCategory = c; }

    public Integer getSelectedCategoryId() { return selectedCategoryId; }
    public void setSelectedCategoryId(Integer id) { this.selectedCategoryId = id; }

    public String getNewCategoryName() { return newCategoryName; }
    public void setNewCategoryName(String s) { this.newCategoryName = s; }

    public Integer getNewDepartmentId() { return newDepartmentId; }
    public void setNewDepartmentId(Integer id) { this.newDepartmentId = id; }

    public String getEditCategoryName() { return editCategoryName; }
    public void setEditCategoryName(String s) { this.editCategoryName = s; }

    public Integer getEditDepartmentId() { return editDepartmentId; }
    public void setEditDepartmentId(Integer id) { this.editDepartmentId = id; }
}
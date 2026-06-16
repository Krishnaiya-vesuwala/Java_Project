package CDIBean;

import Client.RestClient;
import Entity.ComplaintCategory;
import Entity.SlaRules;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.util.List;

@Named("slaManagementCDI")
@ViewScoped
public class SLAManagementCDI implements Serializable {

    private RestClient client = new RestClient();

    private String token;

    private List<SlaRules> slaRules;
    private List<ComplaintCategory> categories;

    private SlaRules selectedSla;

    // CREATE
    private Integer newCategoryId;
    private Integer newMaxDays;
    private String newLevel;

    // EDIT
    private Integer editMaxDays;
    private String editLevel;
    
    Response response;

    private static final List<String> VALID_LEVELS =
            List.of(
                    "WARD_OFFICER",
                    "ZONE_OFFICER",
                    "CORPORATE_ADMIN"
            );

    private static final int MIN_DAYS = 1;
    private static final int MAX_DAYS = 365;

    // =====================================================
    // INIT
    // =====================================================

    @PostConstruct
    public void init() {

        token = (String) FacesContext.getCurrentInstance()
                .getExternalContext()
                .getSessionMap()
                .get("token");

        loadData();

        response = client.getAllCategories(Response.class,token);
        
        categories=response.readEntity(new GenericType<List<ComplaintCategory>>(){});
                

        selectedSla = new SlaRules();
    }

    public void loadData() {

        response = client.getAllSlaRules(Response.class,token);
        slaRules=response.readEntity(new GenericType<List<SlaRules>>(){});
    }

    public void prepareCreate() {
        newCategoryId = null;
        newMaxDays = null;
        newLevel = null;
    }

    // =====================================================
    // VALIDATION
    // =====================================================

    private boolean validateSla(Integer categoryId,
                                String level,
                                Integer maxDays,
                                Integer excludeSlaId,
                                boolean isEdit) {

        boolean ok = true;

        FacesContext fc = FacesContext.getCurrentInstance();

        String prefix = isEdit ? "slaForm:edit" : "slaForm:add";

        // Category
        if (!isEdit) {

            if (categoryId == null) {

                addError(fc,
                        prefix + "Category",
                        "Please select a Category.");

                ok = false;
            }
        }

        // Escalation Level
        if (level == null || level.trim().isEmpty()) {

            addError(fc,
                    prefix + "Level",
                    "Please select an Escalation Level.");

            ok = false;

        } else if (!VALID_LEVELS.contains(level)) {

            addError(fc,
                    prefix + "Level",
                    "Invalid escalation level selected.");

            ok = false;

        } else {

            if (!isEdit && categoryId != null) {

                for (SlaRules r : slaRules) {

                    if (excludeSlaId != null
                            && r.getSlaId().equals(excludeSlaId)) {
                        continue;
                    }

                    if (r.getCategoryId() != null
                            && r.getCategoryId().getCategoryId().equals(categoryId)
                            && level.equals(r.getLevel())) {

                        addError(fc,
                                prefix + "Level",
                                "An SLA rule for this category and level already exists.");

                        ok = false;
                        break;
                    }
                }
            }
        }

        // Max Resolution Days
        if (maxDays == null) {

            addError(fc,
                    prefix + "MaxDays",
                    "Max Resolution Days is required.");

            ok = false;

        } else if (maxDays < MIN_DAYS) {

            addError(fc,
                    prefix + "MaxDays",
                    "Max Resolution Days must be at least "
                            + MIN_DAYS + ".");

            ok = false;

        } else if (maxDays > MAX_DAYS) {

            addError(fc,
                    prefix + "MaxDays",
                    "Max Resolution Days cannot exceed "
                            + MAX_DAYS + ".");

            ok = false;
        }

        return ok;
    }

    // =====================================================
    // MESSAGE HELPERS
    // =====================================================

    private void addError(FacesContext fc,
                          String clientId,
                          String detail) {

        fc.addMessage(
                clientId,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Validation Error",
                        detail));

        fc.validationFailed();
    }

    private void addSuccess(String summary,
                            String detail) {

        FacesContext.getCurrentInstance()
                .addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_INFO,
                                summary,
                                detail));
    }

    // =====================================================
    // CREATE
    // =====================================================

    public void createSlaRule() {

        if (!validateSla(
                newCategoryId,
                newLevel,
                newMaxDays,
                null,
                false)) {

            return;
        }

        client.addSlaRule(
                String.valueOf(newCategoryId),
                String.valueOf(newMaxDays),
                newLevel,
                token);

        loadData();

        addSuccess(
                "SLA Created",
                "SLA rule created successfully.");

        prepareCreate();
    }

    // =====================================================
    // EDIT
    // =====================================================

    public void editSlaRule(SlaRules sla) {

        selectedSla = sla;

        editMaxDays = sla.getMaxResolutionDays();

        editLevel = sla.getLevel();
    }

    // =====================================================
    // UPDATE
    // =====================================================

    public void updateSlaRule() {

        if (selectedSla == null
                || selectedSla.getSlaId() == null) {

            addError(
                    FacesContext.getCurrentInstance(),
                    "slaForm:editMaxDays",
                    "No SLA rule selected for update.");

            return;
        }

        if (!validateSla(
                null,
                editLevel,
                editMaxDays,
                selectedSla.getSlaId(),
                true)) {

            return;
        }

        client.updateSlaRule(
                String.valueOf(selectedSla.getSlaId()),
                String.valueOf(editMaxDays),
                editLevel,
                token);

        loadData();

        addSuccess(
                "SLA Updated",
                "SLA rule updated successfully.");
    }

    // =====================================================
    // DELETE
    // =====================================================

    public void deleteSlaRule(Integer slaId) {

        client.deleteSlaRule(
                String.valueOf(slaId),
                token);

        loadData();

        addSuccess(
                "Deleted",
                "SLA rule deleted successfully.");
    }

    // =====================================================
    // GETTERS & SETTERS
    // =====================================================

    public List<SlaRules> getSlaRules() {
        return slaRules;
    }

    public void setSlaRules(List<SlaRules> slaRules) {
        this.slaRules = slaRules;
    }

    public List<ComplaintCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<ComplaintCategory> categories) {
        this.categories = categories;
    }

    public SlaRules getSelectedSla() {
        return selectedSla;
    }

    public void setSelectedSla(SlaRules selectedSla) {
        this.selectedSla = selectedSla;
    }

    public Integer getNewCategoryId() {
        return newCategoryId;
    }

    public void setNewCategoryId(Integer newCategoryId) {
        this.newCategoryId = newCategoryId;
    }

    public Integer getNewMaxDays() {
        return newMaxDays;
    }

    public void setNewMaxDays(Integer newMaxDays) {
        this.newMaxDays = newMaxDays;
    }

    public String getNewLevel() {
        return newLevel;
    }

    public void setNewLevel(String newLevel) {
        this.newLevel = newLevel;
    }

    public Integer getEditMaxDays() {
        return editMaxDays;
    }

    public void setEditMaxDays(Integer editMaxDays) {
        this.editMaxDays = editMaxDays;
    }

    public String getEditLevel() {
        return editLevel;
    }

    public void setEditLevel(String editLevel) {
        this.editLevel = editLevel;
    }
}
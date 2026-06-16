package CDIBean;

import Client.RestClient;
import Entity.Society;
import Entity.Ward;
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

@Named(value = "societyManagementCDI")
@ViewScoped
public class societyManagementCDI implements Serializable {

    private RestClient restClient = new RestClient();

    private List<Society> societies;
    private List<Ward>    wards;

    private Society selectedSociety;

    /* CREATE fields */
    private String  newSocietyName;
    private String  newAddress;
    private String  newStatus;
    private Integer newWardId;

    /* EDIT fields */
    private String  editSocietyName;
    private String  editAddress;
    private String  editStatus;
    private Integer editWardId;
    
     private String token;

    // ── Validation rules ──────────────────────────────────
    private static final Pattern NAME_PATTERN =
            Pattern.compile("^[a-zA-Z0-9 .&'\\-]+$");

    private static final Pattern ADDRESS_PATTERN =
            Pattern.compile("^[a-zA-Z0-9 .,/#&'\\-]+$");

    private static final int NAME_MIN    = 3;
    private static final int NAME_MAX    = 100;
    private static final int ADDRESS_MIN = 5;
    private static final int ADDRESS_MAX = 200;

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
        
        societies       = new ArrayList<>();
        wards           = new ArrayList<>();
        selectedSociety = new Society();
        newStatus       = "ACTIVE";
        loadData();
        loadWards();
          } catch (Exception e) {
            System.err.println("[WardManagementCDI] init error: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadData() {
        try {
            Response rs = restClient.getAllSocities(Response.class);
            if (rs.getStatus() == 200) {
                societies = rs.readEntity(new GenericType<List<Society>>() {});
            } else {
                System.err.println("[societyManagementCDI] loadData failed. Status: " + rs.getStatus());
                societies = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("[societyManagementCDI] loadData error: " + e.getMessage());
            e.printStackTrace();
            societies = new ArrayList<>();
        }
    }

    private void loadWards() {
        try {
            Response rs = restClient.getAllWards(Response.class,token);
            if (rs.getStatus() == 200) {
                wards = rs.readEntity(new GenericType<List<Ward>>() {});
            } else {
                System.err.println("[societyManagementCDI] loadWards failed. Status: " + rs.getStatus());
                wards = new ArrayList<>();
            }
        } catch (Exception e) {
            System.err.println("[societyManagementCDI] loadWards error: " + e.getMessage());
            e.printStackTrace();
            wards = new ArrayList<>();
        }
    }

    /** Called by "Add Society" button — resets add-dialog fields. */
    public void prepareCreate() {
        newSocietyName = "";
        newAddress     = "";
        newStatus      = "ACTIVE";
        newWardId      = null;
    }

    // ═══════════════════════════════════════════════════════
    //  VALIDATION
    // ═══════════════════════════════════════════════════════

    private boolean validateSociety(String  societyName,
                                    String  address,
                                    String  status,
                                    Integer wardId,
                                    Integer excludeSocietyId,
                                    boolean isEdit) {

        boolean      ok = true;
        FacesContext fc = FacesContext.getCurrentInstance();

        String prefix = isEdit ? "societyForm:edit" : "societyForm:add";

        // ── 1. Society Name ───────────────────────────────
        if (societyName == null || societyName.trim().isEmpty()) {
            addError(fc, prefix + "SocietyName",
                    "Society Name is required.");
            ok = false;
        } else {
            String trimmed = societyName.trim();

            if (trimmed.length() < NAME_MIN) {
                addError(fc, prefix + "SocietyName",
                        "Society Name must be at least "
                        + NAME_MIN + " characters.");
                ok = false;
            } else if (trimmed.length() > NAME_MAX) {
                addError(fc, prefix + "SocietyName",
                        "Society Name cannot exceed "
                        + NAME_MAX + " characters.");
                ok = false;
            }

            if (!NAME_PATTERN.matcher(trimmed).matches()) {
                addError(fc, prefix + "SocietyName",
                        "Society Name can only contain letters, digits, "
                        + "spaces, hyphens, dots, apostrophes and '&'.");
                ok = false;
            }

            // Duplicate check (same ward + name, case-insensitive)
            if (wardId != null && ok) {
                for (Society s : societies) {
                    if (excludeSocietyId != null
                            && s.getSocietyId().equals(excludeSocietyId)) {
                        continue;
                    }
                    if (s.getWardId() != null
                            && s.getWardId().getWardId().equals(wardId)
                            && s.getSocietyName() != null
                            && s.getSocietyName().trim()
                                                 .equalsIgnoreCase(trimmed)) {
                        addError(fc, prefix + "SocietyName",
                                "A society with this name already exists "
                                + "in the selected ward.");
                        ok = false;
                        break;
                    }
                }
            }
        }

        // ── 2. Address ────────────────────────────────────
        if (address == null || address.trim().isEmpty()) {
            addError(fc, prefix + "Address", "Address is required.");
            ok = false;
        } else {
            String trimmedAddr = address.trim();

            if (trimmedAddr.length() < ADDRESS_MIN) {
                addError(fc, prefix + "Address",
                        "Address must be at least "
                        + ADDRESS_MIN + " characters.");
                ok = false;
            } else if (trimmedAddr.length() > ADDRESS_MAX) {
                addError(fc, prefix + "Address",
                        "Address cannot exceed "
                        + ADDRESS_MAX + " characters.");
                ok = false;
            }

            if (!ADDRESS_PATTERN.matcher(trimmedAddr).matches()) {
                addError(fc, prefix + "Address",
                        "Address contains invalid characters.");
                ok = false;
            }
        }

        // ── 3. Ward ───────────────────────────────────────
        if (wardId == null) {
            addError(fc, prefix + "Ward", "Please select a Ward.");
            ok = false;
        }

        // ── 4. Status ─────────────────────────────────────
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

    /** Called by "Save Society" button in the Add dialog. */
    public void createSociety() {

        System.out.println("CREATE SOCIETY CLICKED");
        System.out.println("Society Name = " + newSocietyName);
        System.out.println("Address      = " + newAddress);
        System.out.println("Ward ID      = " + newWardId);
        System.out.println("Status       = " + newStatus);

        if (!validateSociety(newSocietyName, newAddress,
                             newStatus, newWardId,
                             null, false)) {
            return;
        }

        try {
            restClient.createSociety(
                    String.valueOf(newWardId),
                    newSocietyName.trim(),
                    newAddress.trim(),
                    newStatus,token);

            loadData();
            addSuccess("Success",
                    "Society '" + newSocietyName.trim()
                    + "' created successfully.");
            prepareCreate();

        } catch (Exception e) {
            System.err.println("[societyManagementCDI] createSociety error: "
                               + e.getMessage());
            addError(FacesContext.getCurrentInstance(),
                     "societyForm:addSocietyName",
                     "Failed to create society. Please try again.");
        }
    }

    /** Called by "Edit" row button — populates edit-dialog fields. */
    public void editSociety(Society s) {
        selectedSociety = s;
        editSocietyName = s.getSocietyName();
        editAddress     = s.getAddress();
        editStatus      = s.getStatus();
        editWardId      = (s.getWardId() != null)
                          ? s.getWardId().getWardId()
                          : null;
        System.out.println("Editing Society = " + s.getSocietyId());
    }

    /** Called by "Update Society" button in the Edit dialog. */
    public void updateSociety() {

        if (selectedSociety == null
                || selectedSociety.getSocietyId() == null) {
            addError(FacesContext.getCurrentInstance(),
                     "societyForm:editSocietyName",
                     "No society selected for update.");
            return;
        }

        if (!validateSociety(editSocietyName, editAddress,
                             editStatus, editWardId,
                             selectedSociety.getSocietyId(), true)) {
            return;
        }

        try {
            restClient.updateSociety(
                    String.valueOf(selectedSociety.getSocietyId()),
                    editSocietyName.trim(),
                    editAddress.trim(),
                    editStatus,
                    String.valueOf(editWardId),token);

            loadData();
            addSuccess("Updated", "Society updated successfully.");

        } catch (Exception e) {
            System.err.println("[societyManagementCDI] updateSociety error: "
                               + e.getMessage());
            addError(FacesContext.getCurrentInstance(),
                     "societyForm:editSocietyName",
                     "Failed to update society. Please try again.");
        }
    }

    /** Set society status → ACTIVE. */
    public void activate(Integer id) {

        Society s = societies.stream()
                .filter(x -> x.getSocietyId().equals(id))
                .findFirst().orElse(null);

        if (s != null) {
            try {
                restClient.updateSociety(
                        String.valueOf(s.getSocietyId()),
                        s.getSocietyName(),
                        s.getAddress(),
                        "ACTIVE",
                        String.valueOf(s.getWardId().getWardId()),token);

                loadData();
                addSuccess("Activated",
                        "Society '" + s.getSocietyName()
                        + "' has been activated.");

            } catch (Exception e) {
                System.err.println("[societyManagementCDI] activate error: "
                                   + e.getMessage());
            }
        }
    }

    /** Set society status → INACTIVE. */
    public void deactivate(Integer id) {

        Society s = societies.stream()
                .filter(x -> x.getSocietyId().equals(id))
                .findFirst().orElse(null);

        if (s != null) {
            try {
                restClient.updateSociety(
                        String.valueOf(s.getSocietyId()),
                        s.getSocietyName(),
                        s.getAddress(),
                        "INACTIVE",
                        String.valueOf(s.getWardId().getWardId()),token);

                loadData();
                addSuccess("Deactivated",
                        "Society '" + s.getSocietyName()
                        + "' has been deactivated.");

            } catch (Exception e) {
                System.err.println("[societyManagementCDI] deactivate error: "
                                   + e.getMessage());
            }
        }
    }

    /** Hard-delete a society. */
    public void deleteSociety(Integer id) {
        System.out.println("DELETE Society ID: " + id);
        try {
            restClient.deleteSociety(String.valueOf(id),token);
            loadData();
            addSuccess("Deleted", "Society deleted successfully.");
        } catch (Exception e) {
            System.err.println("[societyManagementCDI] deleteSociety error: "
                               + e.getMessage());
        }
    }

    // ═══════════════════════════════════════════════════════
    //  Getters / Setters
    // ═══════════════════════════════════════════════════════

    public List<Society> getSocieties()                         { return societies; }
    public void setSocieties(List<Society> societies)           { this.societies = societies; }

    public List<Ward> getWards()                                { return wards; }
    public void setWards(List<Ward> wards)                      { this.wards = wards; }

    public Society getSelectedSociety()                         { return selectedSociety; }
    public void setSelectedSociety(Society selectedSociety)     { this.selectedSociety = selectedSociety; }

    public String getNewSocietyName()                           { return newSocietyName; }
    public void setNewSocietyName(String newSocietyName)        { this.newSocietyName = newSocietyName; }

    public String getNewAddress()                               { return newAddress; }
    public void setNewAddress(String newAddress)                { this.newAddress = newAddress; }

    public String getNewStatus()                                { return newStatus; }
    public void setNewStatus(String newStatus)                  { this.newStatus = newStatus; }

    public Integer getNewWardId()                               { return newWardId; }
    public void setNewWardId(Integer newWardId)                 { this.newWardId = newWardId; }

    public String getEditSocietyName()                          { return editSocietyName; }
    public void setEditSocietyName(String editSocietyName)      { this.editSocietyName = editSocietyName; }

    public String getEditAddress()                              { return editAddress; }
    public void setEditAddress(String editAddress)              { this.editAddress = editAddress; }

    public String getEditStatus()                               { return editStatus; }
    public void setEditStatus(String editStatus)                { this.editStatus = editStatus; }

    public Integer getEditWardId()                              { return editWardId; }
    public void setEditWardId(Integer editWardId)               { this.editWardId = editWardId; }
}
package CDIBean;

import Client.RestClient;
import Entity.Complaint;
import Entity.ComplaintCategory;
import Entity.ComplaintReply;
import Entity.ComplaintStatusHistory;
import Entity.Officers;
import Entity.Users;

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

@Named("officerDashboardBean")
@ViewScoped
public class OfficerDashboardBean implements Serializable {

    // ==================
    // REST CLIENT
    // ==================
    private RestClient restClient = new RestClient();

    // ==================
    // FIELDS
    // ==================
    private Officers officer;
    private List<Complaint> assignedComplaints;
    private List<ComplaintReply> replyList;
    private List<ComplaintStatusHistory> historyList;
    private List<ComplaintCategory> categoryList;

    private Complaint selectedComplaint;
    private String replyMessage;

    // Search Filters
    private Integer searchComplaintId;
    private Integer searchCategoryId;
    private String searchStatus;
    private String searchPriority;
    private String searchCitizenName;
    private Boolean overdueOnly = false;
    private Boolean slaBreached = false;
    private String complaintType;

    // ==================
    // INIT
    // ==================
    @PostConstruct
    public void init() {

        try {

            // ── Get token and loggedInUser from session ──
            Map<String, Object> session = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap();

            Users loggedInUser = (Users) session.get("loggedInUser");
            String token = (String) session.get("token");

            System.out.println("[OfficerDashboardBean] token = " + token);
            System.out.println("[OfficerDashboardBean] loggedInUser = "
                    + (loggedInUser != null ? loggedInUser.getUserId() : "NULL"));

            // ── Officer Profile ──────────────────────────
            try {
                String userId = String.valueOf(loggedInUser.getUserId());
                
                System.out.println("ID........"+ userId);

                officer = restClient.getOfficerProfile(
                        Officers.class, userId, token);

               System.out.println("Officer ID......"+officer.getOfficerId());

            } catch (Exception e) {
                System.err.println("[OfficerDashboardBean] getOfficerProfile error: "
                        + e.getMessage());
                e.printStackTrace();
            }

            // ── Assigned Complaints ──────────────────────
            try {
                if (officer != null) {

                    String officerId = String.valueOf(officer.getOfficerId());

                    Response rs = restClient.getAssignedComplaint(
                            Response.class, String.valueOf(officerId), token);

                    if (rs.getStatus() == 200) {
                        assignedComplaints = rs.readEntity(
                                new GenericType<List<Complaint>>() {
                        });
                        System.out.println("[OfficerDashboardBean] Loaded Complaints = "
                                + assignedComplaints.size());
                    } else {
                        assignedComplaints = new ArrayList<>();
                        System.err.println("[OfficerDashboardBean] getAssignedComplaints failed."
                                + " Status: " + rs.getStatus());
                    }

                }
            } catch (Exception e) {
                System.err.println("[OfficerDashboardBean] getAssignedComplaints error: "
                        + e.getMessage());
                assignedComplaints = new ArrayList<>();
                e.printStackTrace();
            }

            // ── Category List ────────────────────────────
            try {
                Response rs = restClient.getAllCategories(Response.class, token);

                if (rs.getStatus() == 200) {
                    categoryList = rs.readEntity(
                            new GenericType<List<ComplaintCategory>>() {
                    });
                    System.out.println("[OfficerDashboardBean] Categories loaded: "
                            + categoryList.size());
                } else {
                    categoryList = new ArrayList<>();
                    System.err.println("[OfficerDashboardBean] getAllCategories failed. Status: "
                            + rs.getStatus());
                }

            } catch (Exception e) {
                System.err.println("[OfficerDashboardBean] getAllCategories error: "
                        + e.getMessage());
                categoryList = new ArrayList<>();
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================
    // UPDATE PROFILE
    // ==================
    public void updateAdminProfile() {

        try {

            Map<String, Object> session = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap();

            String token = (String) session.get("token");

            Response rs = restClient.updateUser(
                    String.valueOf(officer.getUserId().getUserId()),
                    officer.getUserId().getFullName(),
                    officer.getUserId().getEmail(),
                    officer.getUserId().getMobile(),
                    officer.getUserId().getUsername(),
                    token);

            if (rs.getStatus() == 200) {

                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_INFO,
                                "Success",
                                "Profile updated successfully"));

            } else {

                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_ERROR,
                                "Error",
                                "Failed to update profile. Status: "
                                + rs.getStatus()));
            }

        } catch (Exception e) {

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Error",
                            e.getMessage()));
            e.printStackTrace();
        }
    }

    // ==================
    // SEARCH METHODS
    // ==================
    public void searchComplaints() {

        try {

            Map<String, Object> session = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap();

            String token = (String) session.get("token");

            String officerId = String.valueOf(officer.getOfficerId());

            Response rs = restClient.filterAssignedComplaintsAdvanced(
                    Response.class,
                    String.valueOf(officerId),
                    String.valueOf(searchComplaintId),
                    String.valueOf(searchCategoryId),
                    searchStatus,
                    searchPriority,
                    searchCitizenName,
                    overdueOnly,
                    slaBreached,
                    complaintType,
                    token);

            if (rs.getStatus() == 200) {
                assignedComplaints = rs.readEntity(
                        new GenericType<List<Complaint>>() {
                });
            } else {
                assignedComplaints = new ArrayList<>();
                addErrorMessage("Search Failed",
                        "Server returned status: " + rs.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
            addErrorMessage("Search Failed", e.getMessage());
        }
    }

    public void resetSearch() {

        // Clear all filters
        searchComplaintId = null;
        searchCategoryId = null;
        searchStatus = null;
        searchPriority = null;
        searchCitizenName = null;
        overdueOnly = false;
        slaBreached = false;
        complaintType = null;

        try {

            Map<String, Object> session = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap();

            String token = (String) session.get("token");
            String officerId = String.valueOf(officer.getOfficerId());

            Response rs = restClient.getAssignedComplaint(
                    Response.class, String.valueOf(officerId), token);

            if (rs.getStatus() == 200) {
                assignedComplaints = rs.readEntity(
                        new GenericType<List<Complaint>>() {
                });
            } else {
                assignedComplaints = new ArrayList<>();
            }

        } catch (Exception e) {
            e.printStackTrace();
            addErrorMessage("Error", "Failed to reset complaints");
        }
    }

    // ==================
    // QUICK FILTERS
    // ==================
    public void filterActive() {
        complaintType = "ACTIVE";
        applyQuickFilter(null, null, null, null, null,
                false, false, complaintType);
    }

    public void filterToday() {
        complaintType = "TODAY";
        applyQuickFilter(null, null, null, null, null,
                false, false, complaintType);
    }

    public void filterOverdue() {
        complaintType = null;
        applyQuickFilter(null, null, null, null, null,
                true, false, null);
    }

    public void filterResolved() {
        complaintType = "RESOLVED";
        applyQuickFilter(null, null, null, null, null,
                false, false, complaintType);
    }

    // ── Internal helper used by all quick filters ─────────
    private void applyQuickFilter(
            Integer complaintId, Integer categoryId,
            String status, String priority, String citizenName,
            Boolean overdue, Boolean slaBreach, String type) {

        try {

            Map<String, Object> session = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap();

            String token = (String) session.get("token");
            String officerId = String.valueOf(officer.getOfficerId());

            Response rs = restClient.filterAssignedComplaintsAdvanced(
                    Response.class,
                    String.valueOf(officerId),
                    complaintId != null ? String.valueOf(complaintId) : null,
                    categoryId != null ? String.valueOf(categoryId) : null,
                    status,
                    priority,
                    citizenName,
                    overdue,
                    slaBreach,
                    type,
                    token
            );

            if (rs.getStatus() == 200) {
                assignedComplaints = rs.readEntity(
                        new GenericType<List<Complaint>>() {
                });
            } else {
                assignedComplaints = new ArrayList<>();
                addErrorMessage("Filter Failed",
                        "Server returned status: " + rs.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
            addErrorMessage("Filter Failed", e.getMessage());
        }
    }

    // ==================
    // COMPLAINT ACTIONS
    // ==================
    public void updateStatus(Complaint complaint) {

        try {

            Map<String, Object> session = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap();

            String token = (String) session.get("token");

            restClient.updateComplaintStatus(
                    String.valueOf(complaint.getComplaintId()),
                    complaint.getStatus(),
                    String.valueOf(officer.getOfficerId()),
                    token);

        } catch (Exception e) {
            e.printStackTrace();
            addErrorMessage("Error", "Failed to Update Status");
        }
    }

    public void openReplyDialog(Complaint complaint) {
        this.selectedComplaint = complaint;
        this.replyMessage = "";
    }

    public void submitReply() {

        try {

            Map<String, Object> session = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap();

            String token = (String) session.get("token");

            restClient.createComplaintReply(
                    String.valueOf(selectedComplaint.getComplaintId()),
                    String.valueOf(officer.getOfficerId()),
                    replyMessage,
                    token);

        } catch (Exception e) {
            e.printStackTrace();
            addErrorMessage("Error", "Failed to Submit Reply");
        }
    }

    public void loadHistory(Complaint complaint) {

        try {

            Map<String, Object> session = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap();

            String token = (String) session.get("token");

            selectedComplaint = complaint;

            Response rs = restClient.getHistory(
                    Response.class,
                    String.valueOf(complaint.getComplaintId()),
                    token);

            if (rs.getStatus() == 200) {
                historyList = rs.readEntity(
                        new GenericType<List<ComplaintStatusHistory>>() {
                });
            } else {
                historyList = new ArrayList<>();
                System.err.println("[OfficerDashboardBean] loadHistory failed. Status: "
                        + rs.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
            historyList = new ArrayList<>();
        }
    }

    public void loadReplies(Complaint complaint) {

        try {

            Map<String, Object> session = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap();

            String token = (String) session.get("token");

            selectedComplaint = complaint;

            Response rs = restClient.getComplaintReplies(
                    Response.class,
                    String.valueOf(complaint.getComplaintId()),
                    token);

            if (rs.getStatus() == 200) {
                replyList = rs.readEntity(
                        new GenericType<List<ComplaintReply>>() {
                });
            } else {
                replyList = new ArrayList<>();
                System.err.println("[OfficerDashboardBean] loadReplies failed. Status: "
                        + rs.getStatus());
            }

        } catch (Exception e) {
            e.printStackTrace();
            replyList = new ArrayList<>();
        }
    }

    // ==================
    // NAVIGATION
    // ==================
    public String viewComplaint(Integer complaintId) {
        return "/complaintDetails.xhtml?faces-redirect=true&complaintId=" + complaintId;
    }

    public String goToProfile() {
        return "/officerProfile.xhtml?faces-redirect=true";
    }

    // ==================
    // INITIALS HELPER
    // ==================
    public String getInitials() {

        if (officer == null
                || officer.getUserId() == null
                || officer.getUserId().getFullName() == null
                || officer.getUserId().getFullName().trim().isEmpty()) {
            return "O";
        }

        String[] parts = officer.getUserId().getFullName().trim().split("\\s+");

        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }

        return (parts[0].substring(0, 1)
                + parts[parts.length - 1].substring(0, 1))
                .toUpperCase();
    }

    // ==================
    // HELPER MESSAGES
    // ==================
    private void addInfoMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, summary, detail));
    }

    private void addErrorMessage(String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_ERROR, summary, detail));
    }

    // ==================
    // GETTERS & SETTERS
    // ==================
    public Officers getOfficer() {
        return officer;
    }

    public void setOfficer(Officers o) {
        this.officer = o;
    }

    public List<Complaint> getAssignedComplaints() {
        return assignedComplaints;
    }

    public void setAssignedComplaints(List<Complaint> l) {
        this.assignedComplaints = l;
    }

    public List<ComplaintReply> getReplyList() {
        return replyList;
    }

    public void setReplyList(List<ComplaintReply> l) {
        this.replyList = l;
    }

    public List<ComplaintStatusHistory> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<ComplaintStatusHistory> l) {
        this.historyList = l;
    }

    public List<ComplaintCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<ComplaintCategory> l) {
        this.categoryList = l;
    }

    public Complaint getSelectedComplaint() {
        return selectedComplaint;
    }

    public void setSelectedComplaint(Complaint c) {
        this.selectedComplaint = c;
    }

    public String getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(String s) {
        this.replyMessage = s;
    }

    public Integer getSearchComplaintId() {
        return searchComplaintId;
    }

    public void setSearchComplaintId(Integer i) {
        this.searchComplaintId = i;
    }

    public Integer getSearchCategoryId() {
        return searchCategoryId;
    }

    public void setSearchCategoryId(Integer i) {
        this.searchCategoryId = i;
    }

    public String getSearchStatus() {
        return searchStatus;
    }

    public void setSearchStatus(String s) {
        this.searchStatus = s;
    }

    public String getSearchPriority() {
        return searchPriority;
    }

    public void setSearchPriority(String s) {
        this.searchPriority = s;
    }

    public String getSearchCitizenName() {
        return searchCitizenName;
    }

    public void setSearchCitizenName(String s) {
        this.searchCitizenName = s;
    }

    public Boolean getOverdueOnly() {
        return overdueOnly;
    }

    public void setOverdueOnly(Boolean b) {
        this.overdueOnly = b;
    }

    public Boolean getSlaBreached() {
        return slaBreached;
    }

    public void setSlaBreached(Boolean b) {
        this.slaBreached = b;
    }

    public String getComplaintType() {
        return complaintType;
    }

    public void setComplaintType(String s) {
        this.complaintType = s;
    }
}

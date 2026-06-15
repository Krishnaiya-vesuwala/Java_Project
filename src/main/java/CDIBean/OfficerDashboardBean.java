package CDIBean;

import EJB.AdminBeanLocal;
import EJB.ComplaintBeanLocal;
import EJB.CorporateDashboardService;
import EJB.OfficerBeanLocal;
import EJB.UserBeanLocal;
import Entity.Complaint;
import Entity.ComplaintCategory;
import Entity.ComplaintReply;
import Entity.ComplaintStatusHistory;
import Entity.Officers;
import Entity.Users;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named("officerDashboardBean")
@ViewScoped
public class OfficerDashboardBean implements Serializable {

    // ==================
    // EJB SERVICES
    // ==================
    @EJB private OfficerBeanLocal officerService;
    @EJB private ComplaintBeanLocal complaintService;
    @EJB private CorporateDashboardService corporateService;
    @EJB private AdminBeanLocal categoryService;
    @EJB private UserBeanLocal userService;

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
    private String  searchStatus;
    private String  searchPriority;
    private String  searchCitizenName;
    private Boolean overdueOnly  = false;
    private Boolean slaBreached  = false;
    private String  complaintType;

    // ==================
    // INIT
    // ==================
    @PostConstruct
    public void init() {
        try {
            Users user = (Users) FacesContext
                    .getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap()
                    .get("user");

            officer           = officerService.getOfficerProfile(10);
            assignedComplaints = officerService.getAssignedComplaint(5);
            categoryList      = categoryService.getAllCategory();

            System.out.println("Loaded Complaints = " + assignedComplaints.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ==================
    // FIX: renamed to updateOfficerProfile
    //      + fixed officer.getStatus() → officer.getUserId().getStatus()
    // ==================
//  Must be exactly this name
   public void updateAdminProfile() {

    try {

        userService.updateUser(
                officer.getUserId().getUserId(),
                officer.getUserId().getFullName(),
                officer.getUserId().getEmail(),
                officer.getUserId().getMobile(),
                officer.getUserId().getUsername(),
                officer.getUserId().getRole(),
                officer.getUserId().getStatus(),
                officer.getUserId().getSocietyId() != null
                        ? officer.getUserId().getSocietyId().getSocietyId()
                        : null
        );

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_INFO,
                        "Success",
                        "Profile updated successfully"
                )
        );

    } catch (Exception e) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Error",
                        e.getMessage()
                )
        );

        e.printStackTrace();
    }
}

    // ==================
    // SEARCH METHODS
    // ==================
    public void searchComplaints() {
        try {
            assignedComplaints = corporateService.filterAssignedComplaintsAdvanced(
                    officer.getOfficerId(),
                    searchComplaintId, searchCategoryId,
                    searchStatus, searchPriority, searchCitizenName,
                    overdueOnly, slaBreached, complaintType);
        } catch (Exception e) {
            e.printStackTrace();
            addErrorMessage("Search Failed", e.getMessage());
        }
    }

    public void resetSearch() {
        searchComplaintId = null;
        searchCategoryId  = null;
        searchStatus      = null;
        searchPriority    = null;
        searchCitizenName = null;
        overdueOnly       = false;
        slaBreached       = false;
        complaintType     = null;
        assignedComplaints = officerService.getAssignedComplaint(officer.getOfficerId());
    }

    // ==================
    // QUICK FILTERS
    // ==================
    public void filterActive() {
        complaintType = "ACTIVE";
        assignedComplaints = corporateService.filterAssignedComplaintsAdvanced(
                officer.getOfficerId(), null, null, null, null, null,
                false, false, complaintType);
    }

    public void filterToday() {
        complaintType = "TODAY";
        assignedComplaints = corporateService.filterAssignedComplaintsAdvanced(
                officer.getOfficerId(), null, null, null, null, null,
                false, false, complaintType);
    }

    public void filterOverdue() {
        complaintType = null;
        assignedComplaints = corporateService.filterAssignedComplaintsAdvanced(
                officer.getOfficerId(), null, null, null, null, null,
                true, false, null);
    }

    public void filterResolved() {
        complaintType = "RESOLVED";
        assignedComplaints = corporateService.filterAssignedComplaintsAdvanced(
                officer.getOfficerId(), null, null, null, null, null,
                false, false, complaintType);
    }

    // ==================
    // COMPLAINT ACTIONS
    // ==================
    public void updateStatus(Complaint complaint) {
        try {
            officerService.updateComplaintStatus(
                    complaint.getComplaintId(),
                    complaint.getStatus(),
                    officer.getOfficerId());
            addInfoMessage("Success", "Complaint Status Updated");
        } catch (Exception e) {
            e.printStackTrace();
            addErrorMessage("Error", "Failed to Update Status");
        }
    }

    public void openReplyDialog(Complaint complaint) {
        this.selectedComplaint = complaint;
        this.replyMessage      = "";
    }

    public void submitReply() {
        try {
            complaintService.createComplaintReply(
                    selectedComplaint.getComplaintId(),
                    officer.getOfficerId(),
                    replyMessage);
            addInfoMessage("Success", "Reply Added Successfully");
            replyMessage = "";
        } catch (Exception e) {
            e.printStackTrace();
            addErrorMessage("Error", "Failed to Submit Reply");
        }
    }

    public void loadHistory(Complaint complaint) {
        try {
            selectedComplaint = complaint;
            historyList = complaintService.getComplaintStatusHistory(
                    complaint.getComplaintId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadReplies(Complaint complaint) {
        try {
            selectedComplaint = complaint;
            replyList = complaintService.getComplaintReplies(
                    complaint.getComplaintId());
        } catch (Exception e) {
            e.printStackTrace();
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
    public Officers getOfficer() { return officer; }
    public void setOfficer(Officers o) { this.officer = o; }

    public List<Complaint> getAssignedComplaints() { return assignedComplaints; }
    public void setAssignedComplaints(List<Complaint> l) { this.assignedComplaints = l; }

    public List<ComplaintReply> getReplyList() { return replyList; }
    public void setReplyList(List<ComplaintReply> l) { this.replyList = l; }

    public List<ComplaintStatusHistory> getHistoryList() { return historyList; }
    public void setHistoryList(List<ComplaintStatusHistory> l) { this.historyList = l; }

    public List<ComplaintCategory> getCategoryList() { return categoryList; }
    public void setCategoryList(List<ComplaintCategory> l) { this.categoryList = l; }

    public Complaint getSelectedComplaint() { return selectedComplaint; }
    public void setSelectedComplaint(Complaint c) { this.selectedComplaint = c; }

    public String getReplyMessage() { return replyMessage; }
    public void setReplyMessage(String s) { this.replyMessage = s; }

    public Integer getSearchComplaintId() { return searchComplaintId; }
    public void setSearchComplaintId(Integer i) { this.searchComplaintId = i; }

    public Integer getSearchCategoryId() { return searchCategoryId; }
    public void setSearchCategoryId(Integer i) { this.searchCategoryId = i; }

    public String getSearchStatus() { return searchStatus; }
    public void setSearchStatus(String s) { this.searchStatus = s; }

    public String getSearchPriority() { return searchPriority; }
    public void setSearchPriority(String s) { this.searchPriority = s; }

    public String getSearchCitizenName() { return searchCitizenName; }
    public void setSearchCitizenName(String s) { this.searchCitizenName = s; }

    public Boolean getOverdueOnly() { return overdueOnly; }
    public void setOverdueOnly(Boolean b) { this.overdueOnly = b; }

    public Boolean getSlaBreached() { return slaBreached; }
    public void setSlaBreached(Boolean b) { this.slaBreached = b; }

    public String getComplaintType() { return complaintType; }
    public void setComplaintType(String s) { this.complaintType = s; }
}
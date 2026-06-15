package CDIBean;

import EJB.CorporateDashboardService;
import EJB.MasterDataService;
import Entity.*;
import jakarta.annotation.PostConstruct;

import jakarta.ejb.EJB;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class CorporateDashboardCDI implements Serializable {

    @EJB
    private CorporateDashboardService dashboardService;

    @EJB
    private MasterDataService masterService;

    private Integer selectedZoneId;
    private Integer selectedWardId;
    private Integer selectedCategoryId;
    private String selectedStatus;

    private List<Complaint> complaints;
    private final Integer corporationId = 1;
    
    private Complaint selectedComplaint;

private Users selectedCitizen;

private List<ComplaintReply> complaintReplies;

private List<ComplaintStatusHistory> statusHistory;

private List<ComplaintEscalation> escalationHistory;

  @PostConstruct
public void init() {

    complaints = dashboardService.filterComplaints(
            corporationId,
            null,
            null,
            null,
            null);
}

 public void search() {

    complaints = dashboardService.filterComplaints(
            corporationId,
            selectedZoneId,
            selectedWardId,
            selectedCategoryId,
            selectedStatus);
}
 
 public void loadComplaintDetails(Integer complaintId) {

    selectedComplaint =
            dashboardService.getComplaintDetails(complaintId);

    selectedCitizen =
            dashboardService.getCitizenDetails(complaintId);

    complaintReplies =
            dashboardService.getComplaintReplies(complaintId);

    statusHistory =
            dashboardService.getStatusHistory(complaintId);

    escalationHistory =
            dashboardService.getEscalationHistory(complaintId);
}
 
 public String viewComplaint(Integer complaintId) {

    return "/complaintDetails.xhtml?faces-redirect=true&complaintId="
            + complaintId;
}
 
 public long getTotalComplaints() {
    return dashboardService.getTotalComplaints(corporationId);
}

public long getOpenComplaints() {
    return dashboardService.getOpenComplaints(corporationId);
}

public long getAssignedComplaints() {
    return dashboardService.getAssignedComplaints(corporationId);
}

public long getResolvedComplaints() {
    return dashboardService.getResolvedComplaints(corporationId);
}

public long getEscalatedComplaints() {
    return dashboardService.getEscalatedComplaints(corporationId);
}


    public List<Zone> getZones() {
        return masterService.getZones(corporationId);
    }

    public List<Ward> getWards() {
        return masterService.getWards(corporationId);
    }

    public List<ComplaintCategory> getCategories() {
        return masterService.getCategories();
    }

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public Integer getSelectedZoneId() {
        return selectedZoneId;
    }

    public void setSelectedZoneId(Integer selectedZoneId) {
        this.selectedZoneId = selectedZoneId;
    }

    public Integer getSelectedWardId() {
        return selectedWardId;
    }

    public void setSelectedWardId(Integer selectedWardId) {
        this.selectedWardId = selectedWardId;
    }

    public Integer getSelectedCategoryId() {
        return selectedCategoryId;
    }

    public void setSelectedCategoryId(Integer selectedCategoryId) {
        this.selectedCategoryId = selectedCategoryId;
    }

    public String getSelectedStatus() {
        return selectedStatus;
    }

    public void setSelectedStatus(String selectedStatus) {
        this.selectedStatus = selectedStatus;
    }

}

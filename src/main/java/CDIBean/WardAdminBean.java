package CDIBean;


import EJB.AdminBeanLocal;
import EJB.ComplaintBeanLocal;
import EJB.UserBeanLocal;
import Entity.Complaint;
import Entity.Officers;
import Entity.Society;
import Entity.Users;
import Entity.Ward;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.Collection;

@Named(value = "wardAdminBean")
@SessionScoped
public class WardAdminBean implements Serializable {

    @EJB
    ComplaintBeanLocal complaintLocal;
    
    @EJB
    private AdminBeanLocal adminService;
    
    @EJB
    private UserBeanLocal userService;

    private Collection<Complaint> complaints;
    private Collection<Society> societies;
    private Collection<Users> citizens;
    private Collection<Officers> officers;

    private Long totalComplaints;
    private Long pendingComplaints;
    private Long resolvedComplaints;
    private Long rejectedComplaints;
    private Users wardAdmin;
    private Integer wardId;
    private String activeTab = "complaints";
    Integer userId=2;
    
    private Collection<Ward> zoneWards;
private Integer selectedOfficerId;
private Integer selectedWardId;

    @PostConstruct
    public void init() {

        wardId = 1; // later get from login user

        loadDashboard();
        loadComplaints();
        loadSocieties();
        loadCitizens();
        loadOfficers();
        loadProfile();
        loadZoneWards();
    }
    
    public void loadProfile(){
         userId=1;
        wardAdmin=userService.getAdminProfile(userId);
    }

    public void loadDashboard() {

        totalComplaints =
                complaintLocal.totalComplaints(wardId);

        pendingComplaints =
                complaintLocal.pendingComplaints(wardId);

        resolvedComplaints =
                complaintLocal.resolvedComplaints(wardId);

        rejectedComplaints =
                complaintLocal.rejectedComplaints(wardId);
    }

    public void loadComplaints() {

        complaints =
                complaintLocal.getComplaintsByWard(wardId);
    }

    public void loadSocieties() {

        societies =
                complaintLocal.getSocietiesByWard(wardId);
    }

    public void loadCitizens() {

        citizens =
                complaintLocal.getCitizensByWard(wardId);
    }

    public void loadOfficers() {

        officers =
                complaintLocal.getOfficersByWard(wardId);
    }

    public void refresh() {

        loadDashboard();
        loadComplaints();
        loadSocieties();
        loadCitizens();
        loadOfficers();
        loadZoneWards();
    }
    
    public void activateSociety(Integer societyId) {

    Society s = societies.stream()
            .filter(x -> x.getSocietyId().equals(societyId))
            .findFirst()
            .orElse(null);

    if (s != null) {

        adminService.updateSociety(
                s.getSocietyId(),
                s.getSocietyName(),
                s.getAddress(),
                "ACTIVE",
                s.getWardId().getWardId()
        );
    }

    loadSocieties();
}

public void deactivateSociety(Integer societyId) {

    Society s = societies.stream()
            .filter(x -> x.getSocietyId().equals(societyId))
            .findFirst()
            .orElse(null);

    if (s != null) {

        adminService.updateSociety(
                s.getSocietyId(),
                s.getSocietyName(),
                s.getAddress(),
                "INACTIVE",
                s.getWardId().getWardId()
        );
    }

    loadSocieties();
}
    public String viewComplaint(Integer complaintId) {
        
         System.out.println("Complaint ID = " + complaintId);

    return "/complaintDetails.xhtml?faces-redirect=true&complaintId="
            + complaintId;
}

private void loadZoneWards() {

    zoneWards =
            adminService.getWardsByZone(
                    wardAdmin.getSocietyId().getWardId().getZoneId().getZoneId()
            );
    System.out.println("Wards.....");
    System.out.println("Wards....." + zoneWards);
}
public void changeOfficerWard() {

    try {

        adminService.changeOfficerWard(
                selectedOfficerId,
                selectedWardId
        );

        loadOfficers();

        System.out.println(
                "Officer Ward Updated Successfully");

    } catch (Exception e) {

        e.printStackTrace();
    }
}
public void prepareOfficerTransfer(
        Integer officerId) {

    selectedOfficerId = officerId;

    Officers officer =
            officers.stream()
                    .filter(o ->
                            o.getOfficerId()
                             .equals(officerId))
                    .findFirst()
                    .orElse(null);

    if (officer != null) {

        selectedWardId =
                officer.getWardId()
                       .getWardId();
    }
}
public Officers getSelectedOfficer() {
    if (selectedOfficerId == null || officers == null) return null;
    return officers.stream()
        .filter(o -> o.getOfficerId().equals(selectedOfficerId))
        .findFirst().orElse(null);
}



    // getters and setters
public Collection<Ward> getZoneWards() {
    return zoneWards;
}

public void setZoneWards(Collection<Ward> zoneWards) {
    this.zoneWards = zoneWards;
}

public Integer getSelectedOfficerId() {
    return selectedOfficerId;
}

public void setSelectedOfficerId(Integer selectedOfficerId) {
    this.selectedOfficerId = selectedOfficerId;
}

public Integer getSelectedWardId() {
    return selectedWardId;
}

public void setSelectedWardId(Integer selectedWardId) {
    this.selectedWardId = selectedWardId;
}

    public Collection<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(Collection<Complaint> complaints) {
        this.complaints = complaints;
    }

    public Collection<Society> getSocieties() {
        return societies;
    }

    public void setSocieties(Collection<Society> societies) {
        this.societies = societies;
    }

    public Collection<Users> getCitizens() {
        return citizens;
    }

    public void setCitizens(Collection<Users> citizens) {
        this.citizens = citizens;
    }

    public Collection<Officers> getOfficers() {
        return officers;
    }

    public void setOfficers(Collection<Officers> officers) {
        this.officers = officers;
    }

    public Long getTotalComplaints() {
        return totalComplaints;
    }

    public void setTotalComplaints(Long totalComplaints) {
        this.totalComplaints = totalComplaints;
    }

    public Long getPendingComplaints() {
        return pendingComplaints;
    }

    public void setPendingComplaints(Long pendingComplaints) {
        this.pendingComplaints = pendingComplaints;
    }

    public Long getResolvedComplaints() {
        return resolvedComplaints;
    }

    public void setResolvedComplaints(Long resolvedComplaints) {
        this.resolvedComplaints = resolvedComplaints;
    }

    public Long getRejectedComplaints() {
        return rejectedComplaints;
    }

    public void setRejectedComplaints(Long rejectedComplaints) {
        this.rejectedComplaints = rejectedComplaints;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

public void setTab(String tab) {
    System.out.println("Called..." + tab);
    this.activeTab = tab;
}

public String getActiveTab() {
    return activeTab;
}

    public Users getWardAdmin() {
        return wardAdmin;
    }

    public void setWardAdmin(Users wardAdmin) {
        this.wardAdmin = wardAdmin;
    }
    
}
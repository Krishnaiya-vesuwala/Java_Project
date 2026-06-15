package CDIBean;

import EJB.AdminBeanLocal;
import EJB.ComplaintBeanLocal;
import EJB.UserBeanLocal;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import Entity.Complaint;
import Entity.Ward;
import Entity.Officers;
import Entity.Society;
import Entity.Users;
import Entity.Zone;

import java.io.Serializable;
import java.util.Collection;

@Named(value = "zoneAdminBean")
@SessionScoped
public class ZoneAdminBean implements Serializable {

    @EJB
    private ComplaintBeanLocal complaintLocal;
    @EJB
    private UserBeanLocal userService;
    @EJB
    private AdminBeanLocal adminService;

    private Integer zoneId;

    private Collection<Complaint> complaints;
    private Collection<Ward> wards;
    private Collection<Society> societies;
    private Collection<Users> citizens;
    private Collection<Officers> officers;

    private Long totalComplaints;
    private Long pendingComplaints;
    private Long resolvedComplaints;
    private Long rejectedComplaints;
    private Users zoneAdmin;

    private String activeTab = "complaints";
    
    private Collection<Zone> zones;

private Integer selectedOfficerId;
private Integer selectedZoneId;
    
    

    @PostConstruct
    public void init() {
        
        System.out.println("Called....");

        zoneId = 1; // fetch from login later
        
        loadDashboard();
        loadComplaints();
        loadWards();
        loadSocieties();
        loadCitizens();
        loadOfficers();
        loadProfile();
          loadZones();
    }
    public void loadProfile() {

    // replace with logged-in user id from session
    Integer userId = 1;

    zoneAdmin = userService.getAdminProfile(userId);
}
    public void loadZones() {

    zones = adminService.getAllZones();
}
    public void prepareOfficerTransfer(Integer officerId) {

    selectedOfficerId = officerId;

    Officers officer =
            officers.stream()
                    .filter(o ->
                            o.getOfficerId().equals(officerId))
                    .findFirst()
                    .orElse(null);

    if (officer != null) {

        selectedZoneId =
                officer.getWardId()
                       .getZoneId()
                       .getZoneId();
    }
}
    public void changeOfficerZone() {

    try {

        adminService.changeOfficerZone(
                selectedOfficerId,
                selectedZoneId
        );

        loadOfficers();

        System.out.println(
                "Officer Zone Updated Successfully");

    } catch (Exception e) {

        e.printStackTrace();
    }
}

    public void loadDashboard() {

        totalComplaints =
                complaintLocal.totalComplaintsByZone(zoneId);

        pendingComplaints =
                complaintLocal.pendingComplaintsByZone(zoneId);

        resolvedComplaints =
                complaintLocal.resolvedComplaintsByZone(zoneId);

        rejectedComplaints =
                complaintLocal.rejectedComplaintsByZone(zoneId);
    }

    public void loadComplaints() {
        complaints =
                complaintLocal.getComplaintsByZone(zoneId);
    }

    public void loadWards() {
        wards =
                complaintLocal.getWardsByZone(zoneId);
    }

    public void loadSocieties() {
        societies =
                complaintLocal.getSocietiesByZone(zoneId);
    }

    public void loadCitizens() {
        citizens =
                complaintLocal.getCitizensByZone(zoneId);
    }

    public void loadOfficers() {
        officers =
                complaintLocal.getOfficersByZone(zoneId);
    }

    public void refresh() {

        loadDashboard();
        loadComplaints();
        loadWards();
        loadSocieties();
        loadCitizens();
        loadOfficers();
    }

    public void setTab(String tab) {
        activeTab = tab;
    }

    public String getActiveTab() {
        return activeTab;
    }

    public Collection<Complaint> getComplaints() {
        return complaints;
    }

    public Collection<Ward> getWards() {
        return wards;
    }

    public Collection<Society> getSocieties() {
        return societies;
    }

    public Collection<Users> getCitizens() {
        return citizens;
    }

    public Collection<Officers> getOfficers() {
        return officers;
    }

    public Long getTotalComplaints() {
        return totalComplaints;
    }

    public Long getPendingComplaints() {
        return pendingComplaints;
    }

    public Long getResolvedComplaints() {
        return resolvedComplaints;
    }

    public Long getRejectedComplaints() {
        return rejectedComplaints;
    }

    public Users getZoneAdmin() {
        return zoneAdmin;
    }

    public void setZoneAdmin(Users zoneAdmin) {
        this.zoneAdmin = zoneAdmin;
    }
    public Collection<Zone> getZones() {
    return zones;
}

public void setZones(Collection<Zone> zones) {
    this.zones = zones;
}

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }

    public Integer getSelectedOfficerId() {
        return selectedOfficerId;
    }

    public void setSelectedOfficerId(Integer selectedOfficerId) {
        this.selectedOfficerId = selectedOfficerId;
    }

    public Integer getSelectedZoneId() {
        return selectedZoneId;
    }

    public void setSelectedZoneId(Integer selectedZoneId) {
        this.selectedZoneId = selectedZoneId;
    }

    
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package CDIBean;

import EJB.AdminBeanLocal;
import EJB.ComplaintBeanLocal;
import EJB.UserBeanLocal;
import Entity.Users;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import java.io.Serializable;

/**
 *
 * @author krishnaiya
 */
@Named(value = "adminDashboardCDI")
@ViewScoped
public class AdminDashboardCDI implements Serializable {

    @EJB
    private AdminBeanLocal adminService;
    @EJB
    private ComplaintBeanLocal complaintService;
    @EJB 
    private UserBeanLocal userService;
    
     private Users admin;

    private Long totalZones;
    private Long totalWards;
    private Long totalSocieties;
    private Long totalDepartments;
    private Long totalCategories;
    private Long totalOfficers;
    private Long totalComplaints;
    private Long pendingComplaints;

    @PostConstruct
    public void init(){

        totalZones =
                (long) adminService.getAllZones().size();

        totalWards =
                (long) adminService.getAllWards().size();

        totalSocieties =
                (long) adminService.getAllSocieties().size();

        totalDepartments =
                (long) adminService.getAllDepartments().size();

        totalCategories =
                (long) adminService.getAllCategory().size();

        totalOfficers =
                (long) adminService.getAllOfficers().size();

        totalComplaints =
                (long) complaintService.getAllComplaints().size();

        pendingComplaints =
                (long) complaintService.getPendingComplaints().size();
        
         try {

            Users loggedUser =
                    (Users) FacesContext.getCurrentInstance()
                            .getExternalContext()
                            .getSessionMap()
                            .get("user");

//            admin =
//                    userService.getAdminProfile(
//                            loggedUser.getUserId());
               admin =
                    userService.getAdminProfile(
                            1);
               System.out.println(admin.getFullName());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void updateAdminProfile() {

    try {

        userService.updateUser(
                admin.getUserId(),
                admin.getFullName(),
                admin.getEmail(),
                admin.getMobile(),
                admin.getUsername(),
                admin.getRole(),
                admin.getStatus(),
                admin.getSocietyId() != null
                        ? admin.getSocietyId().getSocietyId()
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
      public String getInitials() {

        if (admin == null || admin.getFullName() == null || admin.getFullName().trim().isEmpty()) {
            return "A";
        }

        String[] parts = admin.getFullName().trim().split("\\s+");

        if (parts.length == 1) {
            return parts[0].substring(0, 1).toUpperCase();
        }

        return (parts[0].substring(0, 1)
                + parts[parts.length - 1].substring(0, 1))
                .toUpperCase();
    }
    

    // getters setters

    public AdminBeanLocal getAdminService() {
        return adminService;
    }

    public void setAdminService(AdminBeanLocal adminService) {
        this.adminService = adminService;
    }

    public Long getTotalZones() {
        return totalZones;
    }

    public void setTotalZones(Long totalZones) {
        this.totalZones = totalZones;
    }

    public Long getTotalWards() {
        return totalWards;
    }

    public void setTotalWards(Long totalWards) {
        this.totalWards = totalWards;
    }

    public Long getTotalSocieties() {
        return totalSocieties;
    }

    public void setTotalSocieties(Long totalSocieties) {
        this.totalSocieties = totalSocieties;
    }

    public Long getTotalDepartments() {
        return totalDepartments;
    }

    public void setTotalDepartments(Long totalDepartments) {
        this.totalDepartments = totalDepartments;
    }

    public Long getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(Long totalCategories) {
        this.totalCategories = totalCategories;
    }

    public Long getTotalOfficers() {
        return totalOfficers;
    }

    public void setTotalOfficers(Long totalOfficers) {
        this.totalOfficers = totalOfficers;
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

    public Users getAdmin() {
        return admin;
    }

    public void setAdmin(Users admin) {
        this.admin = admin;
    }
    
    
    
}

package CDIBean;

import Client.RestClient;
import Entity.Users;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Named(value = "adminDashboardCDI")
@ViewScoped
public class AdminDashboardCDI implements Serializable {

    private RestClient restClient = new RestClient();

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
    public void init() {

        try {

            // ── Get token and loggedInUser from session ──
            Map<String, Object> session = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap();

            Users loggedInUser = (Users) session.get("loggedInUser");
            String token = (String) session.get("token");

            System.out.println("[AdminDashboardCDI] token = " + token);
            System.out.println("[AdminDashboardCDI] loggedInUser = "
                    + (loggedInUser != null ? loggedInUser.getUserId() : "NULL"));

            // ── Total Zones ──────────────────────────────
            try {
                Response rs = restClient.getAllZones(Response.class,token);
                if (rs.getStatus() == 200) {
                    List<?> list = rs.readEntity(new GenericType<List<?>>() {});
                    totalZones = (long) list.size();
                } else {
                    totalZones = 0L;
                }
            } catch (Exception e) {
                System.err.println("[AdminDashboardCDI] totalZones error: " + e.getMessage());
                totalZones = 0L;
            }

            // ── Total Wards ──────────────────────────────
            try {
                Response rs = restClient.getAllWards(Response.class,token);
                if (rs.getStatus() == 200) {
                    List<?> list = rs.readEntity(new GenericType<List<?>>() {});
                    totalWards = (long) list.size();
                } else {
                    totalWards = 0L;
                }
            } catch (Exception e) {
                System.err.println("[AdminDashboardCDI] totalWards error: " + e.getMessage());
                totalWards = 0L;
            }

            // ── Total Societies ──────────────────────────
            try {
                Response rs = restClient.getAllSocities(Response.class);
                if (rs.getStatus() == 200) {
                    List<?> list = rs.readEntity(new GenericType<List<?>>() {});
                    totalSocieties = (long) list.size();
                } else {
                    totalSocieties = 0L;
                }
            } catch (Exception e) {
                System.err.println("[AdminDashboardCDI] totalSocieties error: " + e.getMessage());
                totalSocieties = 0L;
            }

            // ── Total Departments ────────────────────────
            try {
                Response rs = restClient.getAllDepartments(Response.class,token);
                if (rs.getStatus() == 200) {
                    List<?> list = rs.readEntity(new GenericType<List<?>>() {});
                    totalDepartments = (long) list.size();
                } else {
                    totalDepartments = 0L;
                }
            } catch (Exception e) {
                System.err.println("[AdminDashboardCDI] totalDepartments error: " + e.getMessage());
                totalDepartments = 0L;
            }

            // ── Total Categories ─────────────────────────
            try {
                Response rs = restClient.getAllCategories(Response.class, token);
                if (rs.getStatus() == 200) {
                    List<?> list = rs.readEntity(new GenericType<List<?>>() {});
                    totalCategories = (long) list.size();
                } else {
                    totalCategories = 0L;
                }
            } catch (Exception e) {
                System.err.println("[AdminDashboardCDI] totalCategories error: " + e.getMessage());
                totalCategories = 0L;
            }

            // ── Total Officers ───────────────────────────
            try {
                Response rs = restClient.getAllOfficers(Response.class,token);
                if (rs.getStatus() == 200) {
                    List<?> list = rs.readEntity(new GenericType<List<?>>() {});
                    totalOfficers = (long) list.size();
                } else {
                    totalOfficers = 0L;
                }
            } catch (Exception e) {
                System.err.println("[AdminDashboardCDI] totalOfficers error: " + e.getMessage());
                totalOfficers = 0L;
            }

            // ── Total Complaints ─────────────────────────
            try {
                Response rs = restClient.getAllComplaints(Response.class,token);
                if (rs.getStatus() == 200) {
                    List<?> list = rs.readEntity(new GenericType<List<?>>() {});
                    totalComplaints = (long) list.size();
                } else {
                    totalComplaints = 0L;
                }
            } catch (Exception e) {
                System.err.println("[AdminDashboardCDI] totalComplaints error: " + e.getMessage());
                totalComplaints = 0L;
            }

            // ── Pending Complaints ───────────────────────
            try {
                Response rs = restClient.getPendingComplaints(Response.class,token);
                if (rs.getStatus() == 200) {
                    List<?> list = rs.readEntity(new GenericType<List<?>>() {});
                    pendingComplaints = (long) list.size();
                } else {
                    pendingComplaints = 0L;
                }
            } catch (Exception e) {
                System.err.println("[AdminDashboardCDI] pendingComplaints error: " + e.getMessage());
                pendingComplaints = 0L;
            }

            // ── Admin Profile ────────────────────────────
            try {
                // Use loggedInUser ID from session instead of static ID
                String userId = String.valueOf(loggedInUser.getUserId());

                Response rs = restClient.getUserById(Response.class, userId, token);
                if (rs.getStatus() == 200) {
                    admin = rs.readEntity(Users.class);
                    System.out.println("[AdminDashboardCDI] admin = " + admin.getFullName());
                } else {
                    System.err.println("[AdminDashboardCDI] getAdminProfile failed. Status: "
                            + rs.getStatus());
                }
            } catch (Exception e) {
                System.err.println("[AdminDashboardCDI] getAdminProfile error: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateAdminProfile() {

        try {

            Map<String, Object> session = FacesContext.getCurrentInstance()
                    .getExternalContext()
                    .getSessionMap();

            String token = (String) session.get("token");

            Response rs = restClient.updateUser(
                    String.valueOf(admin.getUserId()),
                    admin.getFullName(),
                    admin.getEmail(),
                    admin.getMobile(),
                    admin.getUsername(),
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

    public String getInitials() {

        if (admin == null
                || admin.getFullName() == null
                || admin.getFullName().trim().isEmpty()) {
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

    // ═══════════════════════════════════════════════════════
    //  Getters / Setters
    // ═══════════════════════════════════════════════════════

    public Long getTotalZones()                          { return totalZones; }
    public void setTotalZones(Long totalZones)           { this.totalZones = totalZones; }

    public Long getTotalWards()                          { return totalWards; }
    public void setTotalWards(Long totalWards)           { this.totalWards = totalWards; }

    public Long getTotalSocieties()                      { return totalSocieties; }
    public void setTotalSocieties(Long totalSocieties)   { this.totalSocieties = totalSocieties; }

    public Long getTotalDepartments()                    { return totalDepartments; }
    public void setTotalDepartments(Long totalDepartments) { this.totalDepartments = totalDepartments; }

    public Long getTotalCategories()                     { return totalCategories; }
    public void setTotalCategories(Long totalCategories) { this.totalCategories = totalCategories; }

    public Long getTotalOfficers()                       { return totalOfficers; }
    public void setTotalOfficers(Long totalOfficers)     { this.totalOfficers = totalOfficers; }

    public Long getTotalComplaints()                     { return totalComplaints; }
    public void setTotalComplaints(Long totalComplaints) { this.totalComplaints = totalComplaints; }

    public Long getPendingComplaints()                   { return pendingComplaints; }
    public void setPendingComplaints(Long pendingComplaints) { this.pendingComplaints = pendingComplaints; }

    public Users getAdmin()                              { return admin; }
    public void setAdmin(Users admin)                    { this.admin = admin; }
}
package CDIBean;

import EJB.AdminBeanLocal;
import EJB.UserBeanLocal;
import Entity.Departments;
import Entity.Officers;
import Entity.Users;
import Entity.Ward;
import Entity.Zone;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.List;

@Named(value = "officerCDI")
@ViewScoped
public class officerCDI implements Serializable {

    @EJB
    private AdminBeanLocal adminService;
    @EJB
    private UserBeanLocal userService;

    private List<Officers> officers;
    private List<Users> users;
    private List<Departments> departments;
    private List<Zone> zones;
    private List<Ward> wards;

    private Integer userId;
    private Integer departmentId;
    private Integer zoneId;
    private Integer wardId;
    private String designation;

    @PostConstruct
    public void init() {

        officers = adminService.getAllOfficers();
        users = userService.getAllUsers();
        departments = adminService.getAllDepartments();
        zones = adminService.getAllZones();
        wards = adminService.getAllWards();

    }

    public void createOfficer() {

        adminService.createOfficer(
                userId,
                departmentId,
                zoneId,
                wardId,
                designation
        );

        FacesContext.getCurrentInstance()
                .addMessage(null,
                        new FacesMessage("Officer Created Successfully"));

        userId = null;
        departmentId = null;
        zoneId = null;
        wardId = null;
        designation = "";

        officers = adminService.getAllOfficers();
    }

    public List<Officers> getOfficers() {
        return officers;
    }

    public List<Users> getUsers() {
        return users;
    }

    public List<Departments> getDepartments() {
        return departments;
    }

    public List<Zone> getZones() {
        return zones;
    }

    public List<Ward> getWards() {
        return wards;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }
}
package CDIBean;

import Entity.Society;
import Client.RestClient;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Named(value = "registerBean")
@SessionScoped
public class RegisterBean implements Serializable {

    private String fullname;
    private String email;
    private String mobile;
    private String username;
    private String password;
    private String confirmPassword;
    private Integer societyId;

    private Collection<Society> societies;

    private RestClient rl = new RestClient();

    public RegisterBean() {
    }

   @PostConstruct
public void init() {
    try {
        Response rs = rl.getAllSocities(Response.class);
            System.out.println("...............Hello");
        if (rs.getStatus() == 200) {
            System.out.println("...............Hello");
            societies = rs.readEntity(
                new GenericType<Collection<Society>>() {});
            System.out.println("...............Hello"+societies);
        } else {
            System.err.println("..............Failed to fetch societies. Status: " + rs.getStatus());
            societies = new ArrayList<>();
        }

    } catch (Exception e) {
        e.printStackTrace();
        societies = new ArrayList<>();
    }
}

    public void register() {

        try {

            if (!password.equals(confirmPassword)) {

                addError("Password and Confirm Password do not match");
                return;
            }

            Response response = rl.registerUser(
                    fullname,
                    email,
                    mobile,
                    username,
                    password,
                    String.valueOf(societyId));

            if (response.getStatus() == 200) {

                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_INFO,
                                "Success",
                                "Registration completed successfully."));

                clearForm();

            } else {

                addError(response.readEntity(String.class));
            }

        } catch (Exception e) {

            addError("Email or Username already exists.");
        }
    }

    private void addError(String message) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Validation Error",
                        message
                )
        );
    }

    private void clearForm() {

        fullname = "";
        email = "";
        mobile = "";
        username = "";
        password = "";
        confirmPassword = "";
        societyId = null;
    }

    // ================= GETTERS & SETTERS =================

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Integer getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Integer societyId) {
        this.societyId = societyId;
    }

    public Collection<Society> getSocieties() {
        return societies;
    }

    public void setSocieties(Collection<Society> societies) {
        this.societies = societies;
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package CDIBean;

import Entity.Society;
import Client.RestClient;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Named;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author riya vesuwala
 */
@Named(value = "registerBean")
@SessionScoped
public class RegisterBean implements Serializable {

    private String fullname;
    private String email;
    private String mobile;
    private String username;
    private String password;
    private Integer societyId;

    private Collection<Society> societies;

    RestClient rl = new RestClient();

    Response rs;

    public RegisterBean() {
    }

    @PostConstruct
    public void init() {

        try {

            rs = rl.getAllSocities(Response.class, "1");

            societies = rs.readEntity(
                    new GenericType<Collection<Society>>() {
                    }
            );

            System.out.println("Society Count : " + societies.size());

            for (Society s : societies) {

                System.out.println(s.getSocietyName());
            }

        } catch (Exception e) {

            e.printStackTrace();

            societies = new ArrayList<>();
        }
    }

    public void register() {

        try {

            rl.registerUser(
                    fullname,
                    email,
                    mobile,
                    username,
                    password,
                    String.valueOf(societyId)
            );

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_INFO,
                            "Registration Successful",
                            null
                    )
            );

            clearForm();

        } catch (Exception e) {

            e.printStackTrace();

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Registration Failed",
                            null
                    )
            );
        }
    }

    public void clearForm() {

        fullname = null;
        email = null;
        mobile = null;
        username = null;
        password = null;
        societyId = null;
    }

    // Getters and Setters

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

    public void setPassword(String password) {
        this.password = password;
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
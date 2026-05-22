/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package CDIBean;

import Client.RestClient;
import Entity.Users;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.ws.rs.core.Response;
import java.io.Serializable;

/**
 *
 * @author riya vesuwala
 */
@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    RestClient rl = new RestClient();

    private String username;
    private String password;

    private Users loggedInUser;

    Response rs;

    public LoginBean() {
    }

    public String login() {

        try {

            Users requestUser = new Users();

            requestUser.setUsername(username);
            requestUser.setPassword(password);

            rs = rl.login(requestUser);

            loggedInUser = rs.readEntity(Users.class);

            if (loggedInUser != null) {

                FacesContext.getCurrentInstance().addMessage(
                        null,
                        new FacesMessage(
                                FacesMessage.SEVERITY_INFO,
                                "Login Successful",
                                null
                        )
                );

                // Role Based Redirection

                if (loggedInUser.getRole().equalsIgnoreCase("Admin")) {

                    return "/admin/dashboard.xhtml?faces-redirect=true";
                }

                if (loggedInUser.getRole().equalsIgnoreCase("Officer")) {

                    return "/officer/dashboard.xhtml?faces-redirect=true";
                }

                if (loggedInUser.getRole().equalsIgnoreCase("Citizen")) {

                    return "/citizen/dashboard.xhtml?faces-redirect=true";
                }
            }

        } catch (Exception e) {

            e.printStackTrace();

            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(
                            FacesMessage.SEVERITY_ERROR,
                            "Invalid Username or Password",
                            null
                    )
            );
        }

        return null;
    }

    public String logout() {

        FacesContext.getCurrentInstance()
                .getExternalContext()
                .invalidateSession();

        return "/login.xhtml?faces-redirect=true";
    }

    // Getters and Setters

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

    public Users getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(Users loggedInUser) {
        this.loggedInUser = loggedInUser;
    }
}
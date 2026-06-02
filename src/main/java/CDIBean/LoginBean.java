package CDIBean;

import Client.RestClient;
import Entity.Users;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import jakarta.ws.rs.core.Response;

import java.io.Serializable;
import java.util.Map;

@Named(value = "loginBean")
@SessionScoped
public class LoginBean implements Serializable {

    private String username;
    private String password;

    private Users loggedInUser;

    private RestClient rl = new RestClient();

    public LoginBean() {
    }

    public String login() {

        try {

            // Username Validation
            if (username == null || username.trim().isEmpty()) {
                addError("Username is required");
                return null;
            }

            if (username.length() < 4) {
                addError("Username must be at least 4 characters");
                return null;
            }

            // Password Validation
            if (password == null || password.trim().isEmpty()) {
                addError("Password is required");
                return null;
            }

            if (!password.matches(
                    "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$")) {

                addError(
                        "Password must contain uppercase, lowercase, number and minimum 8 characters");
                return null;
            }

            Users requestUser = new Users();
            requestUser.setUsername(username);
            requestUser.setPassword(password);

            Response rs = rl.login(requestUser);
            
            System.out.println("Status Code = " + rs.getStatus());

            if (rs.getStatus() == 404) {

                addError("User not found. Please register first.");
                return null;
            }

            if (rs.getStatus() == 401) {

                addError("Incorrect password.");
                return null;
            }

            if (rs.getStatus() != 200) {

                addError("Unable to login. Please try again.");
                return null;
            }

            
            loggedInUser = rs.readEntity(Users.class);
            System.out.println("Name = " + loggedInUser.getFullName());
            System.out.println("Email = " + loggedInUser.getEmail());
            System.out.println("Mobile = " + loggedInUser.getMobile());
            System.out.println("Role = " + loggedInUser.getRole());

            if (loggedInUser == null) {

                addError("Invalid Username or Password");
                return null;
            }

            // Store in Session

            ExternalContext ec =
                    FacesContext.getCurrentInstance()
                            .getExternalContext();

            Map<String, Object> session =
                    ec.getSessionMap();

            session.put("loggedInUser", loggedInUser);

            FacesContext.getCurrentInstance()
                    .addMessage(
                            null,
                            new FacesMessage(
                                    FacesMessage.SEVERITY_INFO,
                                    "Login Successful",
                                    "Welcome "
                                    + loggedInUser.getFullName()
                            )
                    );

            // Role Based Navigation

            if ("Admin".equalsIgnoreCase(
                    loggedInUser.getRole())) {

                return "/admin/dashboard.xhtml?faces-redirect=true";
            }

            if ("Officer".equalsIgnoreCase(
                    loggedInUser.getRole())) {

                return "/officer/dashboard.xhtml?faces-redirect=true";
            }

            if ("Citizen".equalsIgnoreCase(
                    loggedInUser.getRole())) {

                return "/citizen/dashboard.xhtml?faces-redirect=true";
            }

        } catch (Exception e) {

            e.printStackTrace();

            addError("Invalid Username or Password");
        }

        return null;
    }

    public String logout() {

        FacesContext.getCurrentInstance()
                .getExternalContext()
                .invalidateSession();

        return "/public/login.xhtml?faces-redirect=true";
    }

    private void addError(String msg) {

        FacesContext.getCurrentInstance().addMessage(
                null,
                new FacesMessage(
                        FacesMessage.SEVERITY_ERROR,
                        "Login Failed",
                        msg
                )
        );
    }

    // =========================
    // Getters & Setters
    // =========================

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
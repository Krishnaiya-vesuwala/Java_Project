/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package CDIBean;

import Client.RestClient;
import Entity.Users;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;

/**
 *
 * @author riya vesuwala
 */
@Named("profileBean")
@SessionScoped
public class ProfileCDIBean implements Serializable {

    private Users user;
    private boolean editMode = false;

    private RestClient rl = new RestClient();

    @Inject
    private LoginBean loginBean;

    public void loadProfile() {

        try {

            Integer userId =
                    loginBean.getLoggedInUser().getUserId();

            user = rl.getUserById(
                        Users.class,
                        String.valueOf(userId),
                        loginBean.getToken());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public void enableEdit() {

        editMode = true;
    }

    public void cancelEdit() {

        editMode = false;
        loadProfile();
    }

    public void updateProfile() {
        
          System.out.println("Update button clicked");
        try {
            System.out.println("User ID = " + user.getUserId());
            System.out.println("Name = " + user.getFullName());
            rl.updateUser(
                user,
                loginBean.getToken());

            editMode = false;

            loadProfile();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void setEditMode(boolean editMode) {
        this.editMode = editMode;
    }
}
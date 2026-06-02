/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSF/JSFManagedBean.java to edit this template
 */
package CDIBean;

import Entity.Users;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Named;
import java.io.IOException;

/**
 *
 * @author riya vesuwala
 */
@Named
@RequestScoped
public class AuthBean {

    public void checkCitizenAccess() throws IOException {

        ExternalContext ec =
                FacesContext.getCurrentInstance()
                        .getExternalContext();

        Users user =
                (Users) ec.getSessionMap()
                        .get("loggedInUser");

        if (user == null) {

            ec.redirect(
                    ec.getRequestContextPath()
                    + "/public/login.jsf");

            return;
        }

        if (!"Citizen".equalsIgnoreCase(user.getRole())) {

            ec.redirect(
                    ec.getRequestContextPath()
                    + "/public/login.xhtml");
        }
    }
}

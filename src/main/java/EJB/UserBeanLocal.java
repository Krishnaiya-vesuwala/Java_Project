/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Users;
import jakarta.ejb.Local;

/**
 *
 * @author riya vesuwala
 */
@Local
public interface UserBeanLocal {
    public Users login(String username,String password);
    public void registerUser(String fullName,
                             String email,
                             String mobile,
                             String username,
                             String password,
                             String role,
                             Integer societyId);
    Users getUserById(int userId);
    public Users forgotPassword(String username);
    public void resetPassword(int userId, String newPassword);
}

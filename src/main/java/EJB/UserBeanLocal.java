/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SessionLocal.java to edit this template
 */
package EJB;

import Entity.Users;
import jakarta.ejb.Local;
import java.util.List;

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
                             Integer societyId);
    Users getUserById(int userId);
    public void updateUser(Integer userId,
                            String fullName,
                            String email,
                            String mobile,
                            String username);
    public Users forgotPassword(String username);
    public void resetPassword(int userId, String newPassword);
    public void submitFeedback(int complaintId,String rating,String comments);
    public List<Users> getAllUsers();
    public Users createUser(Users user);
    public Users getAdminProfile(Integer userId);
    public void updateUser(Integer userId,
                       String fullName,
                       String email,
                       String mobile,
                       String username,
                       String role,
                       String status,
                       Integer societyId);
}

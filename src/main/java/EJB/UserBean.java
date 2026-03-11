/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Society;
import Entity.Users;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Date;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author riya vesuwala
 */
@Stateless
public class UserBean implements UserBeanLocal {
    @PersistenceContext(unitName="jpu1")
    EntityManager em;
    
    @Override
    public Users login(String username, String password) {

        try {

            Users user = em.createQuery(
                    "SELECT u FROM Users u WHERE u.username=:username AND u.status='Active'",
                    Users.class)
                    .setParameter("username", username)
                    .getSingleResult();

            // check hashed password
            if(BCrypt.checkpw(password, user.getPassword())) {
                return user;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void registerUser(String fullName,
                             String email,
                             String mobile,
                             String username,
                             String password,
                             String role,
                             Integer societyId) {

        try {

            Users user = new Users();

            user.setFullName(fullName);
            user.setEmail(email);
            user.setMobile(mobile);
            user.setUsername(username);

            // encrypt password
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
            user.setPassword(hashedPassword);

            user.setRole(role);
            user.setStatus("Active");
            user.setCreatedAt(new Date());

            if(societyId != null){
                Society s = em.find(Society.class, societyId);
                user.setSocietyId(s);
            }

            em.persist(user);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public Users getUserById(int userId) {
        
        Users user=em.find(Users.class, userId);
        if(user == null){
            System.out.println("User not found");
        }
        return user;
    }
    
    @Override
    public Users forgotPassword(String username) {
        try{
            return em.createQuery(
                    "SELECT u FROM Users u WHERE u.username=:username",
                    Users.class)
                    .setParameter("username", username)
                    .getSingleResult();
        }catch(Exception e){
            return null;
        }
    }
    
    @Override
    public void resetPassword(int userId, String newPassword) {

        Users user = em.find(Users.class, userId);

        if(user != null){

            String hashedPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt());

            user.setPassword(hashedPassword);

            em.merge(user);
        }
    }
}

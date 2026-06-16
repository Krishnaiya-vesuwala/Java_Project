/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/StatelessEjbClass.java to edit this template
 */
package EJB;

import Entity.Complaint;
import Entity.Departments;
import Entity.Officers;
import Entity.Users;
import Entity.Ward;
import Entity.Zone;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

/**
 *
 * @author krishnaiya
 */
@Stateless
public class OfficerBean implements OfficerBeanLocal {

    @PersistenceContext(unitName = "jpu1")
    EntityManager em;

    @EJB
    ComplaintBeanLocal complaintBean;

    @EJB
    NotificationBeanLocal notifyBean;

    @Override
    public List<Complaint> getAssignedComplaint(int officerId) {
        return em.createQuery("SELECT c FROM Complaint c WHERE c.assignedOfficerId.officerId = :oid", Complaint.class)
                .setParameter("oid", officerId)
                .getResultList();
    }

    @Override
    public void updateComplaintStatus(int complaintId, String status, int logenInUser) {

        Complaint c = em.find(Complaint.class, complaintId);

        if (c != null) {

            String oldStatus = c.getStatus();
            Users user = em.find(Users.class, logenInUser);

            c.setStatus(status);

            complaintBean.createComplaintStatusHistory(c.getComplaintId(), oldStatus, status, user);

            em.merge(c);

            if (status.equalsIgnoreCase("RESOLVED") || status.equalsIgnoreCase("SOLVED")) {

                Users citizen = c.getCitizenId();

                notifyBean.sendSMS(
                        citizen.getMobile(),
                        "Your Complaint ID: " + c.getComplaintId()
                        + " has been resolved. Thank you for using the system."
                );
            }
        }
    }

   @Override
public Officers getOfficerProfile(int userId) {

    Users user = em.find(Users.class, userId);

    if (user == null) {
        System.out.println("User not found : " + userId);
        return null;
    }

    List<Officers> officers = em.createQuery(
            "SELECT o FROM Officers o WHERE o.userId = :user",
            Officers.class)
            .setParameter("user", user)
            .getResultList();

    if (officers.isEmpty()) {
        System.out.println("Officer not found for user : " + userId);
        return null;
    }

    return officers.get(0);
}

    @Override
    public List<Complaint> getComplaintByOfficer(int officerId) {
        Officers officer = em.find(Officers.class, officerId);

        if (officer == null) {
            return null;
        }
        String designation = officer.getDesignation();

        //WARD OFFICER
        if (designation.equalsIgnoreCase("WARD")) {
            return em.createQuery("SELECT c FROM Complaint c WHERE c.wardId = :ward",
                    Complaint.class).setParameter("ward", officer.getWardId())
                    .getResultList();
        } //ZONE OFFICER
        else if (designation.equalsIgnoreCase("ZONE")) {
            return em.createQuery("SELECT c FROM Complaint c WHERE c.zoneId = :zone",
                    Complaint.class)
                    .setParameter("zone", officer.getZoneId())
                    .getResultList();
        } else if (designation.equalsIgnoreCase("CORPORATE")) {
            return em.createQuery("SELECT c FROM Complaint c", Complaint.class)
                    .getResultList();
        }
        return null;
    }

    // Krishnaiya
    @Override
    public void createOfficer(Integer userId,
            String designation,
            Integer departmentId,
            Integer zoneId,
            Integer wardId,
            String status) {

        Users user = em.find(Users.class, userId);

        if (user == null) {
            throw new RuntimeException("User not found");
        }

        Long count = em.createQuery(
                "SELECT COUNT(o) FROM Officers o WHERE o.userId.userId=:uid",
                Long.class)
                .setParameter("uid", userId)
                .getSingleResult();

        if (count > 0) {
            throw new RuntimeException("User already registered as Officer");
        }

        Officers officer = new Officers();

        officer.setUserId(user);

        officer.setDesignation(designation);

        officer.setStatus(status);

        if (departmentId != null) {
            officer.setDepartmentId(
                    em.find(Departments.class, departmentId));
        }

        if (zoneId != null) {
            officer.setZoneId(
                    em.find(Zone.class, zoneId));
        }

        if (wardId != null) {
            officer.setWardId(
                    em.find(Ward.class, wardId));
        }

        em.persist(officer);

        user.setRole("OFFICER");

        em.merge(user);
    }

    @Override
    public List<Users> getAvailableUsers() {

        return em.createQuery(
                "SELECT u FROM Users u "
                + "WHERE u.role <> 'OFFICER'",
                Users.class)
                .getResultList();
    }

    @Override
    public List<Officers> getAllOfficers() {

        return em.createQuery(
                "SELECT o FROM Officers o",
                Officers.class)
                .getResultList();
    }

    @Override
    public void removeOfficer(Integer officerId) {

        Officers officer = em.find(Officers.class, officerId);

        if (officer != null) {

            Users user = officer.getUserId();

            user.setRole("CITIZEN");

            em.merge(user);

            em.remove(officer);
        }
    }

    @Override
    public void transferOfficer(
            Integer officerId,
            Integer zoneId,
            Integer wardId) {

        Officers officer
                = em.find(Officers.class, officerId);

        officer.setZoneId(
                em.find(Zone.class, zoneId));

        officer.setWardId(
                em.find(Ward.class, wardId));

        em.merge(officer);
    }

    @Override
    public List<Officers> getOfficersByCorporation(Integer corporationId) {

        return em.createQuery(
                "SELECT o FROM Officers o "
                + "WHERE o.corporationId.corporationId=:cid "
                + "ORDER BY o.userId.fullName",
                Officers.class)
                .setParameter("cid", corporationId)
                .getResultList();
    }

    @Override
    public void addOfficer(Users user, Officers officer) {

        em.persist(user);

        em.flush();

        officer.setUserId(user);

        officer.setStatus("ACTIVE");

        em.persist(officer);
    }

    @Override
    public void updateOfficer(Officers officer) {
        em.merge(officer);
    }

    @Override
    public Officers findOfficer(Integer officerId) {
        return em.find(Officers.class, officerId);
    }

    @Override
    public void deactivateOfficer(Integer officerId) {

        Officers officer = em.find(Officers.class, officerId);

        officer.setStatus("INACTIVE");

        em.merge(officer);
    }

    @Override
    public void activateOfficer(Integer officerId) {

        Officers officer = em.find(Officers.class, officerId);

        officer.setStatus("ACTIVE");

        em.merge(officer);
    }

    @Override
    public Long getAssignedComplaintCount(
            Integer officerId) {

        return em.createQuery(
                "SELECT COUNT(c) "
                + "FROM Complaint c "
                + "WHERE c.assignedOfficerId.officerId=:id",
                Long.class)
                .setParameter("id", officerId)
                .getSingleResult();
    }

    @Override
    public void updateOfficerStatus(
            Integer officerId,
            String status) {

        Officers officer
                = em.find(Officers.class, officerId);

        if (officer != null) {

            officer.setStatus(status);

            em.merge(officer);
        }
    }

    @Override
    public void updateDesignation(
            Integer officerId,
            String designation) {

        Officers officer
                = em.find(Officers.class, officerId);

        officer.setDesignation(designation);

        em.merge(officer);
    }

    @Override
    public void updateDepartment(
            Integer officerId,
            Integer departmentId) {

        Officers officer
                = em.find(Officers.class, officerId);

        officer.setDepartmentId(
                em.find(
                        Departments.class,
                        departmentId));

        em.merge(officer);
    }

    @Override
    public void revokeOfficerRole(
            Integer officerId) {

        Officers officer
                = em.find(Officers.class, officerId);

        if (officer != null) {

            officer.setStatus("INACTIVE");

            Users user
                    = officer.getUserId();

            user.setRole("CITIZEN");

            em.merge(user);
            em.merge(officer);
        }
    }

    @Override
    public List<Officers> searchOfficers(
            Integer zoneId,
            Integer wardId,
            String designation,
            String status) {

        String jpql
                = "SELECT o FROM Officers o WHERE 1=1";

        if (zoneId != null) {
            jpql += " AND o.zoneId.zoneId=:zoneId";
        }

        if (wardId != null) {
            jpql += " AND o.wardId.wardId=:wardId";
        }

        if (designation != null
                && !designation.isEmpty()) {
            jpql += " AND o.designation=:designation";
        }

        if (status != null
                && !status.isEmpty()) {
            jpql += " AND o.status=:status";
        }

        TypedQuery<Officers> q
                = em.createQuery(
                        jpql,
                        Officers.class);

        if (zoneId != null) {
            q.setParameter("zoneId", zoneId);
        }

        if (wardId != null) {
            q.setParameter("wardId", wardId);
        }

        if (designation != null
                && !designation.isEmpty()) {
            q.setParameter(
                    "designation",
                    designation);
        }

        if (status != null
                && !status.isEmpty()) {
            q.setParameter(
                    "status",
                    status);
        }

        return q.getResultList();
    }

    @Override
    public Officers getOfficerById(Integer officerId) {

        return em.find(Officers.class, officerId);
    }
}

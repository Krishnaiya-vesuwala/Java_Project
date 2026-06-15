package EJB;

import Entity.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;

import java.util.List;

@Stateless
public class OfficerManagementService {

    @PersistenceContext(unitName = "jpu1")
    private EntityManager em;

    public List<Officers> getOfficersByCorporation(Integer corporationId) {

        return em.createQuery(
                "SELECT o FROM Officers o " +
                "WHERE o.corporationId.corporationId=:cid " +
                "ORDER BY o.userId.fullName",
                Officers.class)
                .setParameter("cid", corporationId)
                .getResultList();
    }

    public void addOfficer(Users user, Officers officer) {

    em.persist(user);

    em.flush();

    officer.setUserId(user);

    officer.setStatus("ACTIVE");

    em.persist(officer);
}

    public void updateOfficer(Officers officer) {
        em.merge(officer);
    }

    public Officers findOfficer(Integer officerId) {
        return em.find(Officers.class, officerId);
    }

    public void deactivateOfficer(Integer officerId) {

        Officers officer = em.find(Officers.class, officerId);

        officer.setStatus("INACTIVE");

        em.merge(officer);
    }

    public void activateOfficer(Integer officerId) {

        Officers officer = em.find(Officers.class, officerId);

        officer.setStatus("ACTIVE");

        em.merge(officer);
    }

   public Long getAssignedComplaintCount(
        Integer officerId){

    return em.createQuery(
        "SELECT COUNT(c) " +
        "FROM Complaint c " +
        "WHERE c.assignedOfficerId.officerId=:id",
        Long.class)
        .setParameter("id", officerId)
        .getSingleResult();
}
public List<Officers> getAllOfficers() {

    return em.createQuery(
        "SELECT o FROM Officers o",
        Officers.class)
        .getResultList();
}
public void updateOfficerStatus(
        Integer officerId,
        String status) {

    Officers officer =
            em.find(Officers.class, officerId);

    if(officer != null){

        officer.setStatus(status);

        em.merge(officer);
    }
}
public void updateDesignation(
        Integer officerId,
        String designation){

    Officers officer =
            em.find(Officers.class, officerId);

    officer.setDesignation(designation);

    em.merge(officer);
}
public void updateDepartment(
        Integer officerId,
        Integer departmentId){

    Officers officer =
            em.find(Officers.class, officerId);

    officer.setDepartmentId(
            em.find(
                Departments.class,
                departmentId));

    em.merge(officer);
}
public void revokeOfficerRole(
        Integer officerId){

    Officers officer =
            em.find(Officers.class, officerId);

    if(officer != null){

        officer.setStatus("INACTIVE");

        Users user =
                officer.getUserId();

        user.setRole("CITIZEN");

        em.merge(user);
        em.merge(officer);
    }
}
public List<Officers> searchOfficers(
        Integer zoneId,
        Integer wardId,
        String designation,
        String status){

    String jpql =
            "SELECT o FROM Officers o WHERE 1=1";

    if(zoneId != null)
        jpql += " AND o.zoneId.zoneId=:zoneId";

    if(wardId != null)
        jpql += " AND o.wardId.wardId=:wardId";

    if(designation != null &&
       !designation.isEmpty())
        jpql += " AND o.designation=:designation";

    if(status != null &&
       !status.isEmpty())
        jpql += " AND o.status=:status";

    TypedQuery<Officers> q =
            em.createQuery(
                    jpql,
                    Officers.class);

    if(zoneId != null)
        q.setParameter("zoneId", zoneId);

    if(wardId != null)
        q.setParameter("wardId", wardId);

    if(designation != null &&
       !designation.isEmpty())
        q.setParameter(
                "designation",
                designation);

    if(status != null &&
       !status.isEmpty())
        q.setParameter(
                "status",
                status);

    return q.getResultList();
}

}
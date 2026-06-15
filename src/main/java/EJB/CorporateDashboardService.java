package EJB;

import Entity.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.util.List;

@Stateless
public class CorporateDashboardService {

    @PersistenceContext(unitName = "jpu1")
    private EntityManager em;

   public long getTotalComplaints(Integer corpId){

    return em.createQuery(
        "SELECT COUNT(c) " +
        "FROM Complaint c " +
        "WHERE c.zoneId.corporationId.corporationId=:corpId ",
        Long.class)
        .setParameter("corpId", corpId)
        .getSingleResult();
}
public long getOpenComplaints(Integer corpId){

    return em.createQuery(
        "SELECT COUNT(c) " +
        "FROM Complaint c " +
        "WHERE c.zoneId.corporationId.corporationId=:corpId " +
        "AND c.status='OPEN'",
        Long.class)
        .setParameter("corpId", corpId)
        .getSingleResult();
}

    public long getAssignedComplaints(Integer corpId) {
       return em.createQuery(
        "SELECT COUNT(c) " +
        "FROM Complaint c " +
        "WHERE c.zoneId.corporationId.corporationId=:corpId " +
        "AND c.status='ASSIGNED'",
        Long.class)
        .setParameter("corpId", corpId)
        .getSingleResult();
    }

    public long getResolvedComplaints(Integer corpId) {
        return em.createQuery(
        "SELECT COUNT(c) " +
        "FROM Complaint c " +
        "WHERE c.zoneId.corporationId.corporationId=:corpId " +
        "AND c.status='RESOLVED'",
        Long.class)
        .setParameter("corpId", corpId)
        .getSingleResult();
    }

    public long getEscalatedComplaints(Integer corpId) {
        return em.createQuery(
                "SELECT COUNT(c) FROM Complaint c WHERE c.status LIKE 'ESCALATED%' AND c.zoneId.corporationId.corporationId=:corpId",
                Long.class).setParameter("corpId", corpId)
                .getSingleResult();
    }

  public List<Complaint> filterComplaints(
        Integer corporationId,
        Integer zoneId,
        Integer wardId,
        Integer categoryId,
        String status) {

    String jpql =
        "SELECT c FROM Complaint c " +
        "WHERE c.zoneId.corporationId.corporationId = :corpId";

    if(zoneId != null)
        jpql += " AND c.zoneId.zoneId = :zoneId";

    if(wardId != null)
        jpql += " AND c.wardId.wardId = :wardId";

    if(categoryId != null)
        jpql += " AND c.categoryId.categoryId = :categoryId";

    if(status != null && !status.isEmpty())
        jpql += " AND c.status = :status";

    var query = em.createQuery(jpql, Complaint.class);

    query.setParameter("corpId", corporationId);

    if(zoneId != null)
        query.setParameter("zoneId", zoneId);

    if(wardId != null)
        query.setParameter("wardId", wardId);

    if(categoryId != null)
        query.setParameter("categoryId", categoryId);

    if(status != null && !status.isEmpty())
        query.setParameter("status", status);

    return query.getResultList();
}
  public Complaint getComplaintDetails(Integer complaintId) {

    return em.find(Complaint.class, complaintId);
}
  public Users getCitizenDetails(Integer complaintId) {

    Complaint complaint = em.find(Complaint.class, complaintId);

    if(complaint == null)
        return null;

    return complaint.getCitizenId();
}
  public List<ComplaintReply> getComplaintReplies(Integer complaintId) {

    return em.createQuery(
        "SELECT r " +
        "FROM ComplaintReply r " +
        "WHERE r.complaintId.complaintId = :id " +
        "ORDER BY r.repliedAt DESC",
        ComplaintReply.class)
        .setParameter("id", complaintId)
        .getResultList();
}
  public List<ComplaintStatusHistory> getStatusHistory(
        Integer complaintId) {

    return em.createQuery(
        "SELECT h " +
        "FROM ComplaintStatusHistory h " +
        "WHERE h.complaintId.complaintId = :id " +
        "ORDER BY h.changedAt DESC",
        ComplaintStatusHistory.class)
        .setParameter("id", complaintId)
        .getResultList();
}
  public List<ComplaintEscalation> getEscalationHistory(
        Integer complaintId) {

    return em.createQuery(
        "SELECT e " +
        "FROM ComplaintEscalation e " +
        "WHERE e.complaintId.complaintId = :id " +
        "ORDER BY e.escalatedAt DESC",
        ComplaintEscalation.class)
        .setParameter("id", complaintId)
        .getResultList();
}

public List<Complaint> filterAssignedComplaintsAdvanced(
        Integer officerId,
        Integer complaintId,
        Integer categoryId,
        String status,
        String priority,
        String citizenName,
        Boolean overdueOnly,
        Boolean slaBreached,
        String complaintType) {

    String jpql =
        "SELECT c FROM Complaint c " +
        "WHERE c.assignedOfficerId.officerId = :officerId";

    if (complaintId != null)
        jpql += " AND c.complaintId = :complaintId";

    if (categoryId != null)
        jpql += " AND c.categoryId.categoryId = :categoryId";

    if (status != null && !status.isEmpty())
        jpql += " AND c.status = :status";

    if (priority != null && !priority.isEmpty())
        jpql += " AND c.priority = :priority";

    if (citizenName != null && !citizenName.isEmpty())
        jpql += " AND LOWER(c.citizenId.fullName) LIKE :citizenName";

    if (Boolean.TRUE.equals(overdueOnly))
        jpql += " AND c.dueDate < CURRENT_TIMESTAMP";

    if (Boolean.TRUE.equals(slaBreached))
        jpql += " AND c.dueDate < CURRENT_TIMESTAMP "
              + "AND c.status <> 'RESOLVED'";

    if ("TODAY".equalsIgnoreCase(complaintType))
        jpql += " AND FUNCTION('DATE', c.createdAt)=CURRENT_DATE";

    if ("ACTIVE".equalsIgnoreCase(complaintType))
        jpql += " AND c.status IN ('ASSIGNED','IN_PROGRESS')";

    TypedQuery<Complaint> query =
            em.createQuery(jpql, Complaint.class);

    query.setParameter("officerId", officerId);

    if (complaintId != null)
        query.setParameter("complaintId", complaintId);

    if (categoryId != null)
        query.setParameter("categoryId", categoryId);

    if (status != null && !status.isEmpty())
        query.setParameter("status", status);

    if (priority != null && !priority.isEmpty())
        query.setParameter("priority", priority);

    if (citizenName != null && !citizenName.isEmpty())
        query.setParameter(
                "citizenName",
                "%" + citizenName.toLowerCase() + "%");

    return query.getResultList();
}
public Long getTotalAssigned(Integer officerId){

    return em.createQuery(
            "SELECT COUNT(c) FROM Complaint c " +
            "WHERE c.assignedOfficerId.officerId=:id",
            Long.class)
            .setParameter("id", officerId)
            .getSingleResult();
}
public Long getPendingCount(Integer officerId){

    return em.createQuery(
            "SELECT COUNT(c) FROM Complaint c " +
            "WHERE c.assignedOfficerId.officerId=:id " +
            "AND c.status='PENDING'",
            Long.class)
            .setParameter("id", officerId)
            .getSingleResult();
}
public Long getInProgressCount(Integer officerId){

    return em.createQuery(
            "SELECT COUNT(c) FROM Complaint c " +
            "WHERE c.assignedOfficerId.officerId=:id " +
            "AND c.status='IN_PROGRESS'",
            Long.class)
            .setParameter("id", officerId)
            .getSingleResult();
}
public Long getResolvedCount(Integer officerId){

    return em.createQuery(
            "SELECT COUNT(c) FROM Complaint c " +
            "WHERE c.assignedOfficerId.officerId=:id " +
            "AND c.status='RESOLVED'",
            Long.class)
            .setParameter("id", officerId)
            .getSingleResult();
}
public Long getOverdueCount(Integer officerId){

    return em.createQuery(
            "SELECT COUNT(c) FROM Complaint c " +
            "WHERE c.assignedOfficerId.officerId=:id " +
            "AND c.dueDate < CURRENT_TIMESTAMP " +
            "AND c.status <> 'RESOLVED'",
            Long.class)
            .setParameter("id", officerId)
            .getSingleResult();
}
public Long getSlaWarningCount(Integer officerId){

    return em.createQuery(
            "SELECT COUNT(c) FROM Complaint c " +
            "WHERE c.assignedOfficerId.officerId=:id " +
            "AND c.status <> 'RESOLVED' " +
            "AND c.dueDate BETWEEN CURRENT_TIMESTAMP " +
            "AND CURRENT_TIMESTAMP + 1",
            Long.class)
            .setParameter("id", officerId)
            .getSingleResult();
}
public List<Complaint> getTodayComplaints(Integer officerId){

    return em.createQuery(
        "SELECT c FROM Complaint c " +
        "WHERE c.assignedOfficerId.officerId=:id " +
        "AND DATE(c.createdAt)=CURRENT_DATE",
        Complaint.class)
        .setParameter("id", officerId)
        .getResultList();
}
public List<Complaint> getOverdueComplaints(Integer officerId){

    return em.createQuery(
        "SELECT c FROM Complaint c " +
        "WHERE c.assignedOfficerId.officerId=:id " +
        "AND c.dueDate < CURRENT_TIMESTAMP " +
        "AND c.status<>'RESOLVED'",
        Complaint.class)
        .setParameter("id", officerId)
        .getResultList();
}
public List<Complaint> getHighPriorityComplaints(
        Integer officerId){

    return em.createQuery(
        "SELECT c FROM Complaint c " +
        "WHERE c.assignedOfficerId.officerId=:id " +
        "AND c.priority='HIGH'",
        Complaint.class)
        .setParameter("id", officerId)
        .getResultList();
}

}
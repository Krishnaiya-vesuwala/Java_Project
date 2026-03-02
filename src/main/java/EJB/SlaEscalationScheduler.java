/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/J2EE/EJB40/SingletonEjbClass.java to edit this template
 */
package EJB;

import Entity.Complaint;
import Entity.ComplaintEscalation;
import Entity.ComplaintStatusHistory;
import Entity.Officers;
import Entity.SlaRules;
import Entity.Users;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Schedule;
import jakarta.ejb.Startup;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author krishnaiya
 */
@Singleton
@LocalBean
@Startup
public class SlaEscalationScheduler {

    @PersistenceContext(unitName = "jpu1")
    private EntityManager em;
    
    @EJB
    ComplaintBeanLocal complaintBean;

    // Run every minute
     @Schedule(hour = "*", minute = "*", second = "0", persistent = false)
    public void checkEscalation() {

        System.out.println("Running SLA Escalation Check...");

        LocalDateTime now = LocalDateTime.now();
        System.out.println("Java Time: " + now);

        //get all the complaints which not solved and sla timeout is happen
        List<Complaint> breachedComplaints = em.createQuery(
                "SELECT c FROM Complaint c WHERE c.dueDate IS NOT NULL "
                + "AND c.dueDate <= :now "
                + "AND c.status NOT IN ('RESOLVED','CLOSED')",
                Complaint.class)
                .setParameter("now", now)
                .getResultList();
        System.out.println("Breached count: " + breachedComplaints.size());

        for (Complaint complaint : breachedComplaints) {

           Officers currentOfficer = complaint.getAssignedOfficerId();

    // If no officer assigned yet treat as WARD level
    if (currentOfficer == null) {
        escalateComplaint(complaint, "ZONE",2);
        continue;
    }

    String currentLevel = currentOfficer.getDesignation();

    if ("WARD".equals(currentLevel)) {
        escalateComplaint(complaint, "ZONE",2);
        System.out.println("Escalaed to Zone" + complaint.getComplaintId());
        

    } else if ("ZONE".equals(currentLevel)) {
        escalateComplaint(complaint, "CORPORATION",2);
        System.out.println("Escalaed to Zone" + complaint.getComplaintId());

    } else if ("CORPORATION".equals(currentLevel)) {
        System.out.println("Already at highest level: " + complaint.getComplaintId());
    }
}
    }

private void escalateComplaint(Complaint complaint, String nextLevel,int logedInUser) {

    LocalDateTime now = LocalDateTime.now();

    // Find the officer
    Officers nextOfficer = getOfficerByLevel(complaint, nextLevel);
    if (nextOfficer == null) {
        System.out.println("No officer found for level: " + nextLevel);
        return;
    }

    // create new sla rule
    SlaRules nextSla = em.createQuery(
            "SELECT s FROM SlaRules s WHERE s.categoryId.categoryId = :catId "
            + "AND s.level = :level",
            SlaRules.class)
            .setParameter("catId", complaint.getCategoryId().getCategoryId())
            .setParameter("level", nextLevel)
            .setMaxResults(1)
            .getResultList()
            .stream()
            .findFirst()
            .orElse(null);

    if (nextSla == null) {
        System.out.println("No SLA found for level: " + nextLevel);
        return;
    }

    // store old and new status for complaint status history
    String oldStatus = complaint.getStatus();
    String newStatus = "ESCALATED_TO_" + nextLevel;
    Users user = em.find(Users.class, logedInUser);

   
    LocalDateTime newDueDate = now.plusDays(nextSla.getMaxResolutionDays());

    complaint.setAssignedOfficerId(nextOfficer);
    complaint.setDueDate(newDueDate);
    complaint.setStatus(newStatus);

    em.merge(complaint);
    
    complaintBean.createComplaintStatusHistory(complaint, oldStatus, newStatus, user);

    ComplaintEscalation escalation = new ComplaintEscalation();
    escalation.setComplaintId(complaint);
    escalation.setEscalatedTo(nextOfficer);
    escalation.setReason("SLA Breached - Escalated to " + nextLevel);
    escalation.setEscalatedAt(now);

    em.persist(escalation);

    System.out.println("Complaint ID "
            + complaint.getComplaintId()
            + " escalated to "
            + nextLevel);
}

    private Officers getOfficerByLevel(Complaint complaint, String level) {

        if ("ZONE".equals(level)) {
            return em.createQuery(
                    "SELECT o FROM Officers o WHERE o.zoneId = :zone "
                    + "AND o.designation = 'ZONE'",
                    Officers.class)
                    .setParameter("zone", complaint.getZoneId())
                    .setMaxResults(1)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
        }

        if ("CORPORATION".equals(level)) {
            return em.createQuery(
                    "SELECT o FROM Officers o WHERE o.designation = 'CORPORATION'",
                    Officers.class)
                    .setMaxResults(1)
                    .getResultList()
                    .stream()
                    .findFirst()
                    .orElse(null);
        }

        return null;
    }

    private Officers getEscalationOfficer(Complaint complaint) {
        return em.createQuery("SELECT o FROM Officers o WHERE o.zoneId=:zone AND o.designation='ZONE'", Officers.class)
                .setParameter("zone", complaint.getZoneId())
                .setMaxResults(1)
                .getSingleResult();
    }
}

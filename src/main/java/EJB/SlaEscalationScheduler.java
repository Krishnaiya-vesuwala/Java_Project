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

    @EJB
    NotificationBeanLocal notifyBean;

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
                escalateComplaint(complaint, "ZONE_OFFICER", 2);
                continue;
            }

            String currentLevel = currentOfficer.getDesignation();

            if ("WARD_OFFICER".equals(currentLevel)) {
                escalateComplaint(complaint, "ZONE_OFFICER", 2);
                System.out.println("Escalaed to Zone" + complaint.getComplaintId());

            } else if ("ZONE_OFFICER".equals(currentLevel)) {
                escalateComplaint(complaint, "CORPORATE_ADMIN", 2);
                System.out.println("Escalaed to Zone" + complaint.getComplaintId());

            } else if ("CORPORATE_ADMIN".equals(currentLevel)) {
                System.out.println("Already at highest level: " + complaint.getComplaintId());
            }
        }
    }

    private void escalateComplaint(Complaint complaint, String nextLevel, int logedInUser) {

        LocalDateTime now = LocalDateTime.now();

        // Find the officer
        Officers nextOfficer = selectOfficer(complaint, nextLevel);
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

//        LocalDateTime newDueDate = now.plusDays(nextSla.getMaxResolutionDays());
        LocalDateTime newDueDate = now.plusMinutes(nextSla.getMaxResolutionDays());

        complaint.setAssignedOfficerId(nextOfficer);
        nextOfficer.getComplaintCollection().add(complaint);
        complaint.setDueDate(newDueDate);
        complaint.setStatus(newStatus);

        em.merge(complaint);

        complaintBean.createComplaintStatusHistory(complaint.getComplaintId(), oldStatus, newStatus, user);
        

        ComplaintEscalation escalation = new ComplaintEscalation();
        escalation.setComplaintId(complaint);
        escalation.setEscalatedTo(nextOfficer);
        escalation.setReason("SLA Breached - Escalated to " + nextLevel);
        escalation.setEscalatedAt(now);
        complaint.getComplaintEscalationCollection().add(escalation);
        
        em.persist(escalation);

        notifyBean.sendSMS(
                user.getMobile(),
                "Your Complaint ID: " + complaint.getComplaintId()
                + " has been escalated to " + nextLevel + " for further action."
        );

        // Send notification to newly assigned officer
        notifyBean.sendSMS(
                nextOfficer.getUserId().getMobile(),
                "New Escalated Complaint Assigned. ID: "
                + complaint.getComplaintId()
        );

        System.out.println("Complaint ID "
                + complaint.getComplaintId()
                + " escalated to "
                + nextLevel);
    }

    private Officers selectOfficer(Complaint complaint, String level) {

        List<Object[]> results;

        if ("ZONE_OFFICER".equals(level)) {

            results = em.createQuery(
                    "SELECT o, COUNT(c) FROM Officers o "
                    + "LEFT JOIN Complaint c ON c.assignedOfficerId = o "
                    + "AND c.status NOT IN ('RESOLVED','CLOSED') "
                    + "WHERE o.zoneId = :zone "
                    + "AND o.designation = 'ZONE_OFFICER' "
                    + "GROUP BY o "
                    + "ORDER BY COUNT(c) ASC",
                    Object[].class)
                    .setParameter("zone", complaint.getZoneId())
                    .getResultList();

        } else if ("CORPORATE_ADMIN".equals(level)) {

            results = em.createQuery(
                    "SELECT o, COUNT(c) FROM Officers o "
                    + "LEFT JOIN Complaint c ON c.assignedOfficerId = o "
                    + "AND c.status NOT IN ('RESOLVED','CLOSED') "
                    + "WHERE o.designation = 'CORPORATE_ADMIN' "
                    + "GROUP BY o "
                    + "ORDER BY COUNT(c) ASC",
                    Object[].class)
                    .getResultList();

        } else {
            return null;
        }

        if (results.isEmpty()) {
            return null;
        }

        long minLoad = (Long) results.get(0)[1];

        List<Officers> leastLoaded = new java.util.ArrayList<>();

        for (Object[] r : results) {

            if ((Long) r[1] == minLoad) {
                leastLoaded.add((Officers) r[0]);
            }

        }
        if (leastLoaded.size() == 1) {
            return leastLoaded.get(0);
        }

        return roundRobinSelect(leastLoaded);
    }
    private static int lastIndex = -1;

    private Officers roundRobinSelect(List<Officers> officers) {

        if (officers.isEmpty()) {
            return null;
        }

        lastIndex = (lastIndex + 1) % officers.size();

        return officers.get(lastIndex);
    }
//    private Officers getOfficerByLevel(Complaint complaint, String level) {
//
//        if ("ZONE".equals(level)) {
//            return em.createQuery(
//                    "SELECT o FROM Officers o WHERE o.zoneId = :zone "
//                    + "AND o.designation = 'ZONE'",
//                    Officers.class)
//                    .setParameter("zone", complaint.getZoneId())
//                    .setMaxResults(1)
//                    .getResultList()
//                    .stream()
//                    .findFirst()
//                    .orElse(null);
//        }
//
//        if ("CORPORATION".equals(level)) {
//            return em.createQuery(
//                    "SELECT o FROM Officers o WHERE o.designation = 'CORPORATION'",
//                    Officers.class)
//                    .setMaxResults(1)
//                    .getResultList()
//                    .stream()
//                    .findFirst()
//                    .orElse(null);
//        }
//
//        return null;
//    }
//    private Officers getEscalationOfficer(Complaint complaint) {
//        return em.createQuery("SELECT o FROM Officers o WHERE o.zoneId=:zone AND o.designation='ZONE'", Officers.class)
//                .setParameter("zone", complaint.getZoneId())
//                .setMaxResults(1)
//                .getSingleResult();
//    }
}

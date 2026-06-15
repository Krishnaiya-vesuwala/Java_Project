package EJB;

import Entity.Complaint;
import Entity.ComplaintReply;
import Entity.ComplaintStatusHistory;
import Entity.Officers;
import Entity.Society;
import Entity.Users;
import Entity.Ward;
import jakarta.ejb.Local;
import java.util.Collection;
import java.util.List;

@Local
public interface ComplaintBeanLocal {

    // =========================
    // Complaint Creation
    // =========================

    List<Society> decodeQRCode(Integer wardID);

    void createComplaint(
            Integer userId,
            Integer categoryId,
            Integer societyId,
            Integer wardId,
            String title,
            String description,
            String priority);

    Officers assignToWardOfficer(Integer complaintId);

    // =========================
    // Complaint Retrieval
    // =========================

    List<Object[]> getComplaintByUserId(Integer userId);

    List<Complaint> getAllComplaints();

    List<Complaint> getPendingComplaints();

    List<Complaint> getRecentComplaintsByUser(Integer userId);

    // =========================
    // Complaint Status History
    // =========================

    void createComplaintStatusHistory(
            int complaint,
            String old_status,
            String new_status,
            Users changed_by);

    List<ComplaintStatusHistory> getComplaintStatusHistory(
            int complaintId);

    // =========================
    // Complaint Replies
    // =========================

    void createComplaintReply(
            int complaint_id,
            int replied_by,
            String message);

    List<ComplaintReply> getComplaintReplies(
            int complaintId);

    // =========================
    // Citizen Notifications
    // =========================

    List<Object[]> getCitizenNotifications(
            Integer userId);

    // =========================
    // User Dashboard Statistics
    // =========================

    Long getTotalComplaintsByUser(
            Integer userId);

    Long getAssignedComplaintsByUser(
            Integer userId);

    Long getResolvedComplaintsByUser(
            Integer userId);

    Long getRejectedComplaintsByUser(
            Integer userId);

    // =========================
    // Ward Admin Dashboard
    // =========================

    Collection<Complaint> getComplaintsByWard(
            Integer wardId);

    Long totalComplaints(
            Integer wardId);

    Long pendingComplaints(
            Integer wardId);

    Long resolvedComplaints(
            Integer wardId);

    Long rejectedComplaints(
            Integer wardId);

    Collection<Society> getSocietiesByWard(
            Integer wardId);

    Collection<Users> getCitizensByWard(
            Integer wardId);

    Collection<Officers> getOfficersByWard(
            Integer wardId);

    Collection<Officers> getWardOfficers(
            Integer wardId);

    // =========================
    // Zone Admin Dashboard
    // =========================

    Long totalComplaintsByZone(
            Integer zoneId);

    Long pendingComplaintsByZone(
            Integer zoneId);

    Long resolvedComplaintsByZone(
            Integer zoneId);

    Long rejectedComplaintsByZone(
            Integer zoneId);

    Collection<Complaint> getComplaintsByZone(
            Integer zoneId);

    Collection<Ward> getWardsByZone(
            Integer zoneId);

    Collection<Society> getSocietiesByZone(
            Integer zoneId);

    Collection<Users> getCitizensByZone(
            Integer zoneId);

    Collection<Officers> getOfficersByZone(
            Integer zoneId);
}
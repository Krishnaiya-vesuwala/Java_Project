/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entity;

import com.mycompany.grievancesystem.*;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author riya vesuwala
 */
@Entity
@Table(name = "complaint")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Complaint.findAll", query = "SELECT c FROM Complaint c"),
    @NamedQuery(name = "Complaint.findByComplaintId", query = "SELECT c FROM Complaint c WHERE c.complaintId = :complaintId"),
    @NamedQuery(name = "Complaint.findByTitle", query = "SELECT c FROM Complaint c WHERE c.title = :title"),
    @NamedQuery(name = "Complaint.findByDescription", query = "SELECT c FROM Complaint c WHERE c.description = :description"),
    @NamedQuery(name = "Complaint.findByPriority", query = "SELECT c FROM Complaint c WHERE c.priority = :priority"),
    @NamedQuery(name = "Complaint.findByStatus", query = "SELECT c FROM Complaint c WHERE c.status = :status")})
public class Complaint implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "complaint_id")
    private Integer complaintId;
    @Size(max = 100)
    @Column(name = "title")
    private String title;
    @Size(max = 500)
    @Column(name = "description")
    private String description;
    @Size(max = 100)
    @Column(name = "priority")
    private String priority;
    @Size(max = 100)
    @Column(name = "status")
    private String status;
    @OneToMany(mappedBy = "complaintId")
    private Collection<ComplaintStatusHistory> complaintStatusHistoryCollection;
    @OneToMany(mappedBy = "complaintId")
    private Collection<Feedback> feedbackCollection;
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @ManyToOne
    private ComplaintCategory categoryId;
    @JoinColumn(name = "assigned_officer_id", referencedColumnName = "officer_id")
    @ManyToOne
    private Officers assignedOfficerId;
    @JoinColumn(name = "society_id", referencedColumnName = "society_id")
    @ManyToOne
    private Society societyId;
    @JoinColumn(name = "citizen_id", referencedColumnName = "user_id")
    @ManyToOne
    private Users citizenId;
    @JoinColumn(name = "ward_id", referencedColumnName = "ward_id")
    @ManyToOne
    private Ward wardId;
    @JoinColumn(name = "zone_id", referencedColumnName = "zone_id")
    @ManyToOne
    private Zone zoneId;
    @OneToMany(mappedBy = "complaintId")
    private Collection<ComplaintReply> complaintReplyCollection;
    @OneToMany(mappedBy = "complaintId")
    private Collection<ComplaintEscalation> complaintEscalationCollection;

    public Complaint() {
    }

    public Complaint(Integer complaintId) {
        this.complaintId = complaintId;
    }

    public Integer getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Integer complaintId) {
        this.complaintId = complaintId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<ComplaintStatusHistory> getComplaintStatusHistoryCollection() {
        return complaintStatusHistoryCollection;
    }

    public void setComplaintStatusHistoryCollection(Collection<ComplaintStatusHistory> complaintStatusHistoryCollection) {
        this.complaintStatusHistoryCollection = complaintStatusHistoryCollection;
    }

    @XmlTransient
    public Collection<Feedback> getFeedbackCollection() {
        return feedbackCollection;
    }

    public void setFeedbackCollection(Collection<Feedback> feedbackCollection) {
        this.feedbackCollection = feedbackCollection;
    }

    public ComplaintCategory getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(ComplaintCategory categoryId) {
        this.categoryId = categoryId;
    }

    public Officers getAssignedOfficerId() {
        return assignedOfficerId;
    }

    public void setAssignedOfficerId(Officers assignedOfficerId) {
        this.assignedOfficerId = assignedOfficerId;
    }

    public Society getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Society societyId) {
        this.societyId = societyId;
    }

    public Users getCitizenId() {
        return citizenId;
    }

    public void setCitizenId(Users citizenId) {
        this.citizenId = citizenId;
    }

    public Ward getWardId() {
        return wardId;
    }

    public void setWardId(Ward wardId) {
        this.wardId = wardId;
    }

    public Zone getZoneId() {
        return zoneId;
    }

    public void setZoneId(Zone zoneId) {
        this.zoneId = zoneId;
    }

    @XmlTransient
    public Collection<ComplaintReply> getComplaintReplyCollection() {
        return complaintReplyCollection;
    }

    public void setComplaintReplyCollection(Collection<ComplaintReply> complaintReplyCollection) {
        this.complaintReplyCollection = complaintReplyCollection;
    }

    @XmlTransient
    public Collection<ComplaintEscalation> getComplaintEscalationCollection() {
        return complaintEscalationCollection;
    }

    public void setComplaintEscalationCollection(Collection<ComplaintEscalation> complaintEscalationCollection) {
        this.complaintEscalationCollection = complaintEscalationCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (complaintId != null ? complaintId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Complaint)) {
            return false;
        }
        Complaint other = (Complaint) object;
        if ((this.complaintId == null && other.complaintId != null) || (this.complaintId != null && !this.complaintId.equals(other.complaintId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.grievancesystem.Complaint[ complaintId=" + complaintId + " ]";
    }
    
}

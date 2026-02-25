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
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author riya vesuwala
 */
@Entity
@Table(name = "complaint_status_history")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComplaintStatusHistory.findAll", query = "SELECT c FROM ComplaintStatusHistory c"),
    @NamedQuery(name = "ComplaintStatusHistory.findByHistoryId", query = "SELECT c FROM ComplaintStatusHistory c WHERE c.historyId = :historyId"),
    @NamedQuery(name = "ComplaintStatusHistory.findByOldStatus", query = "SELECT c FROM ComplaintStatusHistory c WHERE c.oldStatus = :oldStatus"),
    @NamedQuery(name = "ComplaintStatusHistory.findByNewStatus", query = "SELECT c FROM ComplaintStatusHistory c WHERE c.newStatus = :newStatus"),
    @NamedQuery(name = "ComplaintStatusHistory.findByChangedAt", query = "SELECT c FROM ComplaintStatusHistory c WHERE c.changedAt = :changedAt")})
public class ComplaintStatusHistory implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "history_id")
    private Integer historyId;
    @Size(max = 100)
    @Column(name = "old_status")
    private String oldStatus;
    @Size(max = 100)
    @Column(name = "new_status")
    private String newStatus;
    @Column(name = "changed_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date changedAt;
    @JoinColumn(name = "complaint_id", referencedColumnName = "complaint_id")
    @ManyToOne
    private Complaint complaintId;
    @JoinColumn(name = "changed_by", referencedColumnName = "user_id")
    @ManyToOne
    private Users changedBy;

    public ComplaintStatusHistory() {
    }

    public ComplaintStatusHistory(Integer historyId) {
        this.historyId = historyId;
    }

    public Integer getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Integer historyId) {
        this.historyId = historyId;
    }

    public String getOldStatus() {
        return oldStatus;
    }

    public void setOldStatus(String oldStatus) {
        this.oldStatus = oldStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public Date getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Date changedAt) {
        this.changedAt = changedAt;
    }

    public Complaint getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Complaint complaintId) {
        this.complaintId = complaintId;
    }

    public Users getChangedBy() {
        return changedBy;
    }

    public void setChangedBy(Users changedBy) {
        this.changedBy = changedBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (historyId != null ? historyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComplaintStatusHistory)) {
            return false;
        }
        ComplaintStatusHistory other = (ComplaintStatusHistory) object;
        if ((this.historyId == null && other.historyId != null) || (this.historyId != null && !this.historyId.equals(other.historyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.grievancesystem.ComplaintStatusHistory[ historyId=" + historyId + " ]";
    }
    
}

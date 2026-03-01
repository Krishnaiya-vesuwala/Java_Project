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
import java.time.LocalDateTime;


/**
 *
 * @author riya vesuwala
 */
@Entity
@Table(name = "complaint_escalation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComplaintEscalation.findAll", query = "SELECT c FROM ComplaintEscalation c"),
    @NamedQuery(name = "ComplaintEscalation.findByEscalationId", query = "SELECT c FROM ComplaintEscalation c WHERE c.escalationId = :escalationId"),
    @NamedQuery(name = "ComplaintEscalation.findByReason", query = "SELECT c FROM ComplaintEscalation c WHERE c.reason = :reason"),
    @NamedQuery(name = "ComplaintEscalation.findByEscalatedAt", query = "SELECT c FROM ComplaintEscalation c WHERE c.escalatedAt = :escalatedAt")})
public class ComplaintEscalation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "escalation_id")
    private Integer escalationId;
    @Size(max = 200)
    @Column(name = "reason")
    private String reason;
    @Column(name = "escalated_at")
    private LocalDateTime  escalatedAt;
    @JoinColumn(name = "complaint_id", referencedColumnName = "complaint_id")
    @ManyToOne
    private Complaint complaintId;
    @JoinColumn(name = "escalated_to", referencedColumnName = "officer_id")
    @ManyToOne
    private Officers escalatedTo;

    public ComplaintEscalation() {
    }

    public ComplaintEscalation(Integer escalationId) {
        this.escalationId = escalationId;
    }

    public Integer getEscalationId() {
        return escalationId;
    }

    public void setEscalationId(Integer escalationId) {
        this.escalationId = escalationId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public LocalDateTime getEscalatedAt() {
        return escalatedAt;
    }

    public void setEscalatedAt(LocalDateTime escalatedAt) {
        this.escalatedAt = escalatedAt;
    }

    public Complaint getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Complaint complaintId) {
        this.complaintId = complaintId;
    }

    public Officers getEscalatedTo() {
        return escalatedTo;
    }

    public void setEscalatedTo(Officers escalatedTo) {
        this.escalatedTo = escalatedTo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (escalationId != null ? escalationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComplaintEscalation)) {
            return false;
        }
        ComplaintEscalation other = (ComplaintEscalation) object;
        if ((this.escalationId == null && other.escalationId != null) || (this.escalationId != null && !this.escalationId.equals(other.escalationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.grievancesystem.ComplaintEscalation[ escalationId=" + escalationId + " ]";
    }
    
}

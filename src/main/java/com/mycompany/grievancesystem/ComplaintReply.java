/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.grievancesystem;

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
@Table(name = "complaint_reply")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ComplaintReply.findAll", query = "SELECT c FROM ComplaintReply c"),
    @NamedQuery(name = "ComplaintReply.findByReplyId", query = "SELECT c FROM ComplaintReply c WHERE c.replyId = :replyId"),
    @NamedQuery(name = "ComplaintReply.findByMessage", query = "SELECT c FROM ComplaintReply c WHERE c.message = :message"),
    @NamedQuery(name = "ComplaintReply.findByRepliedAt", query = "SELECT c FROM ComplaintReply c WHERE c.repliedAt = :repliedAt")})
public class ComplaintReply implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "reply_id")
    private Integer replyId;
    @Size(max = 200)
    @Column(name = "message")
    private String message;
    @Column(name = "replied_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date repliedAt;
    @JoinColumn(name = "complaint_id", referencedColumnName = "complaint_id")
    @ManyToOne
    private Complaint complaintId;
    @JoinColumn(name = "replied_by", referencedColumnName = "user_id")
    @ManyToOne
    private Users repliedBy;

    public ComplaintReply() {
    }

    public ComplaintReply(Integer replyId) {
        this.replyId = replyId;
    }

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getRepliedAt() {
        return repliedAt;
    }

    public void setRepliedAt(Date repliedAt) {
        this.repliedAt = repliedAt;
    }

    public Complaint getComplaintId() {
        return complaintId;
    }

    public void setComplaintId(Complaint complaintId) {
        this.complaintId = complaintId;
    }

    public Users getRepliedBy() {
        return repliedBy;
    }

    public void setRepliedBy(Users repliedBy) {
        this.repliedBy = repliedBy;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (replyId != null ? replyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ComplaintReply)) {
            return false;
        }
        ComplaintReply other = (ComplaintReply) object;
        if ((this.replyId == null && other.replyId != null) || (this.replyId != null && !this.replyId.equals(other.replyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.grievancesystem.ComplaintReply[ replyId=" + replyId + " ]";
    }
    
}

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
import jakarta.persistence.Lob;
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
@Table(name = "society")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Society.findAll", query = "SELECT s FROM Society s"),
    @NamedQuery(name = "Society.findBySocietyId", query = "SELECT s FROM Society s WHERE s.societyId = :societyId"),
    @NamedQuery(name = "Society.findBySocietyName", query = "SELECT s FROM Society s WHERE s.societyName = :societyName"),
    @NamedQuery(name = "Society.findByStatus", query = "SELECT s FROM Society s WHERE s.status = :status")})
public class Society implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "society_id")
    private Integer societyId;
    @Size(max = 100)
    @Column(name = "society_name")
    private String societyName;
    @Lob
    @Size(max = 65535)
    @Column(name = "address")
    private String address;
    @Size(max = 100)
    @Column(name = "status")
    private String status;
    @OneToMany(mappedBy = "societyId")
    private Collection<Users> usersCollection;
    @JoinColumn(name = "ward_id", referencedColumnName = "ward_id")
    @ManyToOne
    private Ward wardId;
    @OneToMany(mappedBy = "societyId")
    private Collection<Complaint> complaintCollection;

    public Society() {
    }

    public Society(Integer societyId) {
        this.societyId = societyId;
    }

    public Integer getSocietyId() {
        return societyId;
    }

    public void setSocietyId(Integer societyId) {
        this.societyId = societyId;
    }

    public String getSocietyName() {
        return societyName;
    }

    public void setSocietyName(String societyName) {
        this.societyName = societyName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<Users> getUsersCollection() {
        return usersCollection;
    }

    public void setUsersCollection(Collection<Users> usersCollection) {
        this.usersCollection = usersCollection;
    }

    public Ward getWardId() {
        return wardId;
    }

    public void setWardId(Ward wardId) {
        this.wardId = wardId;
    }

    @XmlTransient
    public Collection<Complaint> getComplaintCollection() {
        return complaintCollection;
    }

    public void setComplaintCollection(Collection<Complaint> complaintCollection) {
        this.complaintCollection = complaintCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (societyId != null ? societyId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Society)) {
            return false;
        }
        Society other = (Society) object;
        if ((this.societyId == null && other.societyId != null) || (this.societyId != null && !this.societyId.equals(other.societyId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.grievancesystem.Society[ societyId=" + societyId + " ]";
    }
    
}

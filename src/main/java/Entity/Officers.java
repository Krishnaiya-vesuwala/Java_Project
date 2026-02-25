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
@Table(name = "officers")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Officers.findAll", query = "SELECT o FROM Officers o"),
    @NamedQuery(name = "Officers.findByOfficerId", query = "SELECT o FROM Officers o WHERE o.officerId = :officerId"),
    @NamedQuery(name = "Officers.findByDesignation", query = "SELECT o FROM Officers o WHERE o.designation = :designation")})
public class Officers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "officer_id")
    private Integer officerId;
    @Size(max = 100)
    @Column(name = "designation")
    private String designation;
    @OneToMany(mappedBy = "assignedOfficerId")
    private Collection<Complaint> complaintCollection;
    @OneToMany(mappedBy = "escalatedTo")
    private Collection<ComplaintEscalation> complaintEscalationCollection;
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    @ManyToOne
    private Departments departmentId;
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    @ManyToOne
    private Users userId;
    @JoinColumn(name = "ward_id", referencedColumnName = "ward_id")
    @ManyToOne
    private Ward wardId;
    @JoinColumn(name = "zone_id", referencedColumnName = "zone_id")
    @ManyToOne
    private Zone zoneId;

    public Officers() {
    }

    public Officers(Integer officerId) {
        this.officerId = officerId;
    }

    public Integer getOfficerId() {
        return officerId;
    }

    public void setOfficerId(Integer officerId) {
        this.officerId = officerId;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    @XmlTransient
    public Collection<Complaint> getComplaintCollection() {
        return complaintCollection;
    }

    public void setComplaintCollection(Collection<Complaint> complaintCollection) {
        this.complaintCollection = complaintCollection;
    }

    @XmlTransient
    public Collection<ComplaintEscalation> getComplaintEscalationCollection() {
        return complaintEscalationCollection;
    }

    public void setComplaintEscalationCollection(Collection<ComplaintEscalation> complaintEscalationCollection) {
        this.complaintEscalationCollection = complaintEscalationCollection;
    }

    public Departments getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Departments departmentId) {
        this.departmentId = departmentId;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (officerId != null ? officerId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Officers)) {
            return false;
        }
        Officers other = (Officers) object;
        if ((this.officerId == null && other.officerId != null) || (this.officerId != null && !this.officerId.equals(other.officerId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.grievancesystem.Officers[ officerId=" + officerId + " ]";
    }
    
}

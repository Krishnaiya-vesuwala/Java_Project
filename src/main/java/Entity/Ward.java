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
@Table(name = "ward")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Ward.findAll", query = "SELECT w FROM Ward w"),
    @NamedQuery(name = "Ward.findByWardId", query = "SELECT w FROM Ward w WHERE w.wardId = :wardId"),
    @NamedQuery(name = "Ward.findByWardName", query = "SELECT w FROM Ward w WHERE w.wardName = :wardName"),
    @NamedQuery(name = "Ward.findByStatus", query = "SELECT w FROM Ward w WHERE w.status = :status")})
public class Ward implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ward_id")
    private Integer wardId;
    @Size(max = 200)
    @Column(name = "ward_name")
    private String wardName;
    @Size(max = 100)
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "zone_id", referencedColumnName = "zone_id")
    @ManyToOne
    private Zone zoneId;
    @OneToMany(mappedBy = "wardId")
    private Collection<Society> societyCollection;
    @OneToMany(mappedBy = "wardId")
    private Collection<Complaint> complaintCollection;
    @OneToMany(mappedBy = "wardId")
    private Collection<Officers> officersCollection;

    public Ward() {
    }

    public Ward(Integer wardId) {
        this.wardId = wardId;
    }

    public Integer getWardId() {
        return wardId;
    }

    public void setWardId(Integer wardId) {
        this.wardId = wardId;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Zone getZoneId() {
        return zoneId;
    }

    public void setZoneId(Zone zoneId) {
        this.zoneId = zoneId;
    }

    @XmlTransient
    public Collection<Society> getSocietyCollection() {
        return societyCollection;
    }

    public void setSocietyCollection(Collection<Society> societyCollection) {
        this.societyCollection = societyCollection;
    }

    @XmlTransient
    public Collection<Complaint> getComplaintCollection() {
        return complaintCollection;
    }

    public void setComplaintCollection(Collection<Complaint> complaintCollection) {
        this.complaintCollection = complaintCollection;
    }

    @XmlTransient
    public Collection<Officers> getOfficersCollection() {
        return officersCollection;
    }

    public void setOfficersCollection(Collection<Officers> officersCollection) {
        this.officersCollection = officersCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (wardId != null ? wardId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ward)) {
            return false;
        }
        Ward other = (Ward) object;
        if ((this.wardId == null && other.wardId != null) || (this.wardId != null && !this.wardId.equals(other.wardId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.grievancesystem.Ward[ wardId=" + wardId + " ]";
    }
    
}

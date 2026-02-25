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
@Table(name = "zone")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Zone.findAll", query = "SELECT z FROM Zone z"),
    @NamedQuery(name = "Zone.findByZoneId", query = "SELECT z FROM Zone z WHERE z.zoneId = :zoneId"),
    @NamedQuery(name = "Zone.findByZoneName", query = "SELECT z FROM Zone z WHERE z.zoneName = :zoneName"),
    @NamedQuery(name = "Zone.findByStatus", query = "SELECT z FROM Zone z WHERE z.status = :status")})
public class Zone implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "zone_id")
    private Integer zoneId;
    @Size(max = 200)
    @Column(name = "zone_name")
    private String zoneName;
    @Size(max = 100)
    @Column(name = "status")
    private String status;
    @OneToMany(mappedBy = "zoneId")
    private Collection<Ward> wardCollection;
    @OneToMany(mappedBy = "zoneId")
    private Collection<Complaint> complaintCollection;
    @JoinColumn(name = "corporation_id", referencedColumnName = "corporation_id")
    @ManyToOne
    private Corporation corporationId;
    @OneToMany(mappedBy = "zoneId")
    private Collection<Officers> officersCollection;

    public Zone() {
    }

    public Zone(Integer zoneId) {
        this.zoneId = zoneId;
    }

    public Integer getZoneId() {
        return zoneId;
    }

    public void setZoneId(Integer zoneId) {
        this.zoneId = zoneId;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<Ward> getWardCollection() {
        return wardCollection;
    }

    public void setWardCollection(Collection<Ward> wardCollection) {
        this.wardCollection = wardCollection;
    }

    @XmlTransient
    public Collection<Complaint> getComplaintCollection() {
        return complaintCollection;
    }

    public void setComplaintCollection(Collection<Complaint> complaintCollection) {
        this.complaintCollection = complaintCollection;
    }

    public Corporation getCorporationId() {
        return corporationId;
    }

    public void setCorporationId(Corporation corporationId) {
        this.corporationId = corporationId;
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
        hash += (zoneId != null ? zoneId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Zone)) {
            return false;
        }
        Zone other = (Zone) object;
        if ((this.zoneId == null && other.zoneId != null) || (this.zoneId != null && !this.zoneId.equals(other.zoneId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.grievancesystem.Zone[ zoneId=" + zoneId + " ]";
    }
    
}

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
import jakarta.persistence.Lob;
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
@Table(name = "corporation")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Corporation.findAll", query = "SELECT c FROM Corporation c"),
    @NamedQuery(name = "Corporation.findByCorporationId", query = "SELECT c FROM Corporation c WHERE c.corporationId = :corporationId"),
    @NamedQuery(name = "Corporation.findByCorporationName", query = "SELECT c FROM Corporation c WHERE c.corporationName = :corporationName"),
    @NamedQuery(name = "Corporation.findByStatus", query = "SELECT c FROM Corporation c WHERE c.status = :status")})
public class Corporation implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "corporation_id")
    private Integer corporationId;
    @Size(max = 200)
    @Column(name = "corporation_name")
    private String corporationName;
    @Lob
    @Size(max = 65535)
    @Column(name = "address")
    private String address;
    @Size(max = 50)
    @Column(name = "status")
    private String status;
    @OneToMany(mappedBy = "corporationId")
    private Collection<Zone> zoneCollection;

    public Corporation() {
    }

    public Corporation(Integer corporationId) {
        this.corporationId = corporationId;
    }

    public Integer getCorporationId() {
        return corporationId;
    }

    public void setCorporationId(Integer corporationId) {
        this.corporationId = corporationId;
    }

    public String getCorporationName() {
        return corporationName;
    }

    public void setCorporationName(String corporationName) {
        this.corporationName = corporationName;
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
    public Collection<Zone> getZoneCollection() {
        return zoneCollection;
    }

    public void setZoneCollection(Collection<Zone> zoneCollection) {
        this.zoneCollection = zoneCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (corporationId != null ? corporationId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Corporation)) {
            return false;
        }
        Corporation other = (Corporation) object;
        if ((this.corporationId == null && other.corporationId != null) || (this.corporationId != null && !this.corporationId.equals(other.corporationId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.grievancesystem.Corporation[ corporationId=" + corporationId + " ]";
    }
    
}

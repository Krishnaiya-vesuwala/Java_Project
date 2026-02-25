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
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 *
 * @author riya vesuwala
 */
@Entity
@Table(name = "sla_rules")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SlaRules.findAll", query = "SELECT s FROM SlaRules s"),
    @NamedQuery(name = "SlaRules.findBySlaId", query = "SELECT s FROM SlaRules s WHERE s.slaId = :slaId"),
    @NamedQuery(name = "SlaRules.findByMaxResolutionDays", query = "SELECT s FROM SlaRules s WHERE s.maxResolutionDays = :maxResolutionDays")})
public class SlaRules implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sla_id")
    private Integer slaId;
    @Size(max = 100)
    @Column(name = "max_resolution_days")
    private String maxResolutionDays;
    @JoinColumn(name = "department_id", referencedColumnName = "department_id")
    @ManyToOne
    private Departments departmentId;

    public SlaRules() {
    }

    public SlaRules(Integer slaId) {
        this.slaId = slaId;
    }

    public Integer getSlaId() {
        return slaId;
    }

    public void setSlaId(Integer slaId) {
        this.slaId = slaId;
    }

    public String getMaxResolutionDays() {
        return maxResolutionDays;
    }

    public void setMaxResolutionDays(String maxResolutionDays) {
        this.maxResolutionDays = maxResolutionDays;
    }

    public Departments getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Departments departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (slaId != null ? slaId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SlaRules)) {
            return false;
        }
        SlaRules other = (SlaRules) object;
        if ((this.slaId == null && other.slaId != null) || (this.slaId != null && !this.slaId.equals(other.slaId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.grievancesystem.SlaRules[ slaId=" + slaId + " ]";
    }
    
}

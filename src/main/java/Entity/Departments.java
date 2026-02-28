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
@Table(name = "departments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Departments.findAll", query = "SELECT d FROM Departments d"),
    @NamedQuery(name = "Departments.findByDepartmentId", query = "SELECT d FROM Departments d WHERE d.departmentId = :departmentId"),
    @NamedQuery(name = "Departments.findByDepartmentName", query = "SELECT d FROM Departments d WHERE d.departmentName = :departmentName"),
    @NamedQuery(name = "Departments.findByDescription", query = "SELECT d FROM Departments d WHERE d.description = :description"),
    @NamedQuery(name = "Departments.findByStatus", query = "SELECT d FROM Departments d WHERE d.status = :status")})
public class Departments implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "department_id")
    private Integer departmentId;
    @Size(max = 200)
    @Column(name = "department_name")
    private String departmentName;
    @Size(max = 200)
    @Column(name = "description")
    private String description;
    @Size(max = 50)
    @Column(name = "status")
    private String status;
    @OneToMany(mappedBy = "departmentId")
    private Collection<ComplaintCategory> complaintCategoryCollection;
//    @OneToMany(mappedBy = "departmentId")
//    private Collection<SlaRules> slaRulesCollection;
    @OneToMany(mappedBy = "departmentId")
    private Collection<Officers> officersCollection;

    public Departments() {
    }

    public Departments(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @XmlTransient
    public Collection<ComplaintCategory> getComplaintCategoryCollection() {
        return complaintCategoryCollection;
    }

    public void setComplaintCategoryCollection(Collection<ComplaintCategory> complaintCategoryCollection) {
        this.complaintCategoryCollection = complaintCategoryCollection;
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
        hash += (departmentId != null ? departmentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Departments)) {
            return false;
        }
        Departments other = (Departments) object;
        if ((this.departmentId == null && other.departmentId != null) || (this.departmentId != null && !this.departmentId.equals(other.departmentId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.grievancesystem.Departments[ departmentId=" + departmentId + " ]";
    }
    
}

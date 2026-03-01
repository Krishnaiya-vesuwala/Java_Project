package Entity;

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
import jakarta.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@Table(name = "sla_rules")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "SlaRules.findAll", query = "SELECT s FROM SlaRules s"),
    @NamedQuery(name = "SlaRules.findBySlaId", query = "SELECT s FROM SlaRules s WHERE s.slaId = :slaId")
})
public class SlaRules implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "sla_id")
    private Integer slaId;

    @Column(name = "max_resolution_days")
    private Integer maxResolutionDays;

    @Column(name = "level")
    private String level;

    //  Correct Foreign Key Mapping
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @ManyToOne
    private ComplaintCategory categoryId;

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

    public Integer getMaxResolutionDays() {
        return maxResolutionDays;
    }

    public void setMaxResolutionDays(Integer maxResolutionDays) {
        this.maxResolutionDays = maxResolutionDays;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ComplaintCategory getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(ComplaintCategory categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public int hashCode() {
        return (slaId != null ? slaId.hashCode() : 0);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof SlaRules)) {
            return false;
        }
        SlaRules other = (SlaRules) object;
        return !((this.slaId == null && other.slaId != null)
                || (this.slaId != null && !this.slaId.equals(other.slaId)));
    }

    @Override
    public String toString() {
        return "SlaRules[ slaId=" + slaId + " ]";
    }
}

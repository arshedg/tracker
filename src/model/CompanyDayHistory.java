/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mubarak
 */
@Entity
@Table(name = "companyDayHistory")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CompanyDayHistory.findAll", query = "SELECT c FROM CompanyDayHistory c"),
    @NamedQuery(name = "CompanyDayHistory.findById", query = "SELECT c FROM CompanyDayHistory c WHERE c.id = :id"),
    @NamedQuery(name = "CompanyDayHistory.findByCompanyValue", query = "SELECT c FROM CompanyDayHistory c WHERE c.companyValue = :companyValue"),
    @NamedQuery(name = "CompanyDayHistory.findByUpdatedTime", query = "SELECT c FROM CompanyDayHistory c WHERE c.updatedTime = :updatedTime")})
public class CompanyDayHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "company_value")
    private String companyValue;
    @Basic(optional = false)
    @Column(name = "updated_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedTime;
    @JoinColumn(name = "company_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ListedCompany companyId;

    public CompanyDayHistory() {
    }

    public CompanyDayHistory(Integer id) {
        this.id = id;
    }

    public CompanyDayHistory(Integer id, String companyValue, Date updatedTime) {
        this.id = id;
        this.companyValue = companyValue;
        this.updatedTime = updatedTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyValue() {
        return companyValue;
    }

    public void setCompanyValue(String companyValue) {
        this.companyValue = companyValue;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public ListedCompany getCompanyId() {
        return companyId;
    }

    public void setCompanyId(ListedCompany companyId) {
        this.companyId = companyId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompanyDayHistory)) {
            return false;
        }
        CompanyDayHistory other = (CompanyDayHistory) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.CompanyDayHistory[ id=" + id + " ]";
    }
    
}

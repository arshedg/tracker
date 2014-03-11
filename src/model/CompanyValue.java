/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author mubarak
 */
@Entity
@Table(name = "companyValue")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "CompanyValue.findAll", query = "SELECT c FROM CompanyValue c"),
    @NamedQuery(name = "CompanyValue.findById", query = "SELECT c FROM CompanyValue c WHERE c.id = :id"),
    @NamedQuery(name = "CompanyValue.findByCurrentRate", query = "SELECT c FROM CompanyValue c WHERE c.currentRate = :currentRate"),
    @NamedQuery(name = "CompanyValue.findByDayOpenValue", query = "SELECT c FROM CompanyValue c WHERE c.dayOpenValue = :dayOpenValue"),
    @NamedQuery(name = "CompanyValue.findByListedCompany", query = "SELECT c FROM CompanyValue c WHERE c.listedCompanyId = :listedCompanyId")})
public class CompanyValue implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "current_rate")
    private String currentRate;
    @Basic(optional = false)
    @Column(name = "day_open_value")
    private String dayOpenValue;
    @JoinColumn(name = "listed_company_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private ListedCompany listedCompanyId;

    public CompanyValue() {
    }

    public CompanyValue(Integer id) {
        this.id = id;
    }

    public CompanyValue(Integer id, String currentRate, String dayOpenValue) {
        this.id = id;
        this.currentRate = currentRate;
        this.dayOpenValue = dayOpenValue;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrentRate() {
        return currentRate;
    }

    public void setCurrentRate(String currentRate) {
        this.currentRate = currentRate;
    }

    public String getDayOpenValue() {
        return dayOpenValue;
    }

    public void setDayOpenValue(String dayOpenValue) {
        this.dayOpenValue = dayOpenValue;
    }

    public ListedCompany getListedCompanyId() {
        return listedCompanyId;
    }

    public void setListedCompanyId(ListedCompany listedCompanyId) {
        this.listedCompanyId = listedCompanyId;
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
        if (!(object instanceof CompanyValue)) {
            return false;
        }
        CompanyValue other = (CompanyValue) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.CompanyValue[ id=" + id + " ]";
    }
    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author mubarak
 */
@Entity
@Table(name = "listedCompany")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "ListedCompany.findAll", query = "SELECT l FROM ListedCompany l"),
    @NamedQuery(name = "ListedCompany.findById", query = "SELECT l FROM ListedCompany l WHERE l.id = :id"),
    @NamedQuery(name = "ListedCompany.findByScripId", query = "SELECT l FROM ListedCompany l WHERE l.scripId = :scripId"),
    @NamedQuery(name = "ListedCompany.findByScripName", query = "SELECT l FROM ListedCompany l WHERE l.scripName = :scripName"),
    @NamedQuery(name = "ListedCompany.findByScripGroup", query = "SELECT l FROM ListedCompany l WHERE l.scripGroup = :scripGroup")})
public class ListedCompany implements Serializable {
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "companyId")
    private Collection<CompanyDayHistory> companyDayHistoryCollection;
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "scrip_id")
    private String scripId;
    @Basic(optional = false)
    @Column(name = "scrip_name")
    private String scripName;
    @Basic(optional = false)
    @Column(name = "scrip_group")
    private String scripGroup;

    public ListedCompany() {
    }

    public ListedCompany(Integer id) {
        this.id = id;
    }

    public ListedCompany(Integer id, String scripId, String scripName, String scripGroup) {
        this.id = id;
        this.scripId = scripId;
        this.scripName = scripName;
        this.scripGroup = scripGroup;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getScripId() {
        return scripId;
    }

    public void setScripId(String scripId) {
        this.scripId = scripId;
    }

    public String getScripName() {
        return scripName;
    }

    public void setScripName(String scripName) {
        this.scripName = scripName;
    }

    public String getScripGroup() {
        return scripGroup;
    }

    public void setScripGroup(String scripGroup) {
        this.scripGroup = scripGroup;
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
        if (!(object instanceof ListedCompany)) {
            return false;
        }
        ListedCompany other = (ListedCompany) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.ListedCompany[ id=" + id + " ]";
    }

    @XmlTransient
    public Collection<CompanyDayHistory> getCompanyDayHistoryCollection() {
        return companyDayHistoryCollection;
    }

    public void setCompanyDayHistoryCollection(Collection<CompanyDayHistory> companyDayHistoryCollection) {
        this.companyDayHistoryCollection = companyDayHistoryCollection;
    }
    
}

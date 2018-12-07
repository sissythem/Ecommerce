package gr.di.ecommerce.airbnb.entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "searches")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Searches.findAll", query = "SELECT s FROM Searches s"),
    @NamedQuery(name = "Searches.findById", query = "SELECT s FROM Searches s WHERE s.id = :id"),
    @NamedQuery(name = "Searches.findByCity", query = "SELECT s FROM Searches s WHERE s.city = :city"),
    
    /* Custom */
    @NamedQuery(name = "Searches.findByUserId", query = "SELECT s FROM Searches s WHERE s.userId.id = :userId")
})
public class Searches implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Integer id;
    @Size(max = 45)
    @Column(name = "city")
    private String city;
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @ManyToOne
    private Users userId;

    public Searches() {}

    public Searches(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Users getUserId() {
        return userId;
    }

    public void setUserId(Users userId) {
        this.userId = userId;
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
        if (!(object instanceof Searches)) {
            return false;
        }
        Searches other = (Searches) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.Searches[ id=" + id + " ]";
    }
}

package fromRESTful;

import java.io.Serializable;

/** Images class to map the object from RESTful services **/
public class Images implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer id;
    private String name;
    private Residences residenceId;

    public Images() {
    }

    public Images(Integer id) {
        this.id = id;
    }

    public Images(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Residences getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(Residences residenceId) {
        this.residenceId = residenceId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        /** WARNING: this method won't work in the case the id fields are not set **/
        if (!(object instanceof Images)) {
            return false;
        }
        Images other = (Images) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.Images[ id=" + id + " ]";
    }
}
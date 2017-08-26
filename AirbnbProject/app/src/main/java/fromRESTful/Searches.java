package fromRESTful;

import java.io.Serializable;

/** Searches class to map the object from RESTful services **/
public class Searches implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String city;
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
    public boolean equals(Object object)
    {
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
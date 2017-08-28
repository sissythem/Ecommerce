package fromRESTful;

import java.io.Serializable;

/** Reviews class to map the object from RESTful services **/
public class Reviews implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String comment;
    private double rating;
    private Residences residenceId;
    private Users hostId;
    private Users tenantId;

    public Reviews() {}

    public Reviews(Residences residence, Users host, Users tenant, String comment, double rating)
    {
        this.rating = rating;
        this.comment=comment;
        this.residenceId=residence;
        this.hostId=host;
        this.tenantId=tenant;
    }

    public Reviews(Integer id, String comment, double rating) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Residences getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(Residences residenceId) {
        this.residenceId = residenceId;
    }

    public Users getHostId() {
        return hostId;
    }

    public void setHostId(Users hostId) {
        this.hostId = hostId;
    }

    public Users getTenantId() {
        return tenantId;
    }

    public void setTenantId(Users tenantId) {
        this.tenantId = tenantId;
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
        if (!(object instanceof Reviews)) {
            return false;
        }
        Reviews other = (Reviews) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "domain.Reviews[ id=" + id + " ]";
    }
}
package fromRESTful;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import util.Utils;

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

    public static Reviews fromJSON (JSONObject obj){
        Reviews r = new Reviews();
        try {
            r.id = (Integer) obj.get("id");
            r.comment="";
            if(Utils.isFieldOK(obj, "comment"))
                r.comment = (String) obj.get("comment");
            r.rating=0.0;
            if(Utils.isFieldOK(obj, "rating"))
                r.rating = (double) obj.get("rating");

            JSONObject residenceObject = (JSONObject)obj.get("residenceId");
            Residences ratedResidence = Residences.fromJSON(residenceObject);
            r.residenceId = ratedResidence;

            JSONObject userobject = (JSONObject)obj.get("hostId");
            Users hostUser = Users.fromJSON(userobject);
            r.hostId = hostUser;

            JSONObject tenantObject = (JSONObject)obj.get("tenantId");
            Users tenantUser = Users.fromJSON(tenantObject);
            r.tenantId = tenantUser;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return r;
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
        return "dbpackage.Reviews[ id=" + id + " ]";
    }

}
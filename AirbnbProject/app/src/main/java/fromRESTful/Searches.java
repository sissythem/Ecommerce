package fromRESTful;

/**
 * Created by sissy on 8/5/2017.
 */

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Searches implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private String city;
    private Users userId;

    public Searches() {
    }

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

    public static Searches fromJSON(JSONObject obj){
        Searches s = new Searches();

        try {
            s.id = (Integer) obj.get("id");
            s.city = (String) obj.get("city");
            JSONObject userobject = (JSONObject)obj.get("userId");
            Users user = Users.fromJSON(userobject);
            s.userId = user;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return s;
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
        return "dbpackage.Searches[ id=" + id + " ]";
    }

}

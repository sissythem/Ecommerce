package fromRESTful;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Date;

import util.Utils;

public class Reservations implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Date startDate;
    private Date endDate;
    private int guests;
    private Residences residenceId;
    private Users tenantId;

    public Reservations() {}

    public Reservations(Users user, Residences residence, Date date_start, Date date_end, int guests)
    {
        this.tenantId=user;
        this.residenceId=residence;
        this.startDate=date_start;
        this.endDate=date_end;
        this.guests=guests;
    }

    public Reservations(Integer id) {
        this.id = id;
    }

    public Reservations(Integer id, Date startDate, Date endDate, int guests) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guests = guests;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public Residences getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(Residences residenceId) {
        this.residenceId= residenceId;
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

    public static Reservations fromJSON (JSONObject obj){
        Reservations r = new Reservations();

        try {
            r.id = (Integer) obj.get("id");
            if(Utils.isFieldOK(obj, "startDate")) {
                String startDateString = (String) obj.get("startDate");
                r.startDate = Utils.ConvertStringToDate(startDateString, Utils.DATABASE_DATE_FORMAT);
            }
            if(Utils.isFieldOK(obj, "endDate")) {
                String endDateString = (String) obj.get("endDate");
                r.endDate = Utils.ConvertStringToDate(endDateString, Utils.DATABASE_DATE_FORMAT);
            }
            r.guests=0;
            if(Utils.isFieldOK(obj, "guests"))
                r.guests = (int) obj.get("guests");

            JSONObject tenantObject = (JSONObject)obj.get("tenantId");
            Users tenantUser = Users.fromJSON(tenantObject);
            r.tenantId = tenantUser;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return r;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reservations)) {
            return false;
        }
        Reservations other = (Reservations) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbpackage.Reservations[ id=" + id + " ]";
    }

}
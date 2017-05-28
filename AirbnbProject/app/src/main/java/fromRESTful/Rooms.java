package fromRESTful;

/**
 * Created by sissy on 8/5/2017.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import util.Utils;

public class Rooms implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer beds;
    private int bedType;
    private Integer bathrooms;
    private double spaceArea;
    private String view;
    private Residences residenceId;
    private Users hostId;
    private Collection<Reservations> reservationsCollection;

    public Rooms() {
    }

    public Rooms(Integer id) {
        this.id = id;
    }

    public Rooms(Integer id, int bedType, double spaceArea) {
        this.id = id;
        this.bedType = bedType;
        this.spaceArea = spaceArea;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public int getBedType() {
        return bedType;
    }

    public void setBedType(int bedType) {
        this.bedType = bedType;
    }

    public Integer getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(Integer bathrooms) {
        this.bathrooms = bathrooms;
    }

    public double getSpaceArea() {
        return spaceArea;
    }

    public void setSpaceArea(double spaceArea) {
        this.spaceArea = spaceArea;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
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


    public Collection<Reservations> getReservationsCollection() {
        return reservationsCollection;
    }

    public void setReservationsCollection(Collection<Reservations> reservationsCollection) {
        this.reservationsCollection = reservationsCollection;
    }

    public static Rooms fromJSON (JSONObject obj){
        Rooms r = new Rooms();
        try {
            r.id = (Integer) obj.get("id");
            r.beds = 0;
            if(Utils.isFieldOK(obj, "beds"))
                r.beds = (Integer) obj.get("beds");
            r.bedType=0;
            if(Utils.isFieldOK(obj, "bedType"))
                r.bedType = (int) obj.get("bedType");
            r.bathrooms = 0;
            if(Utils.isFieldOK(obj, "bathrooms"))
                r.bathrooms = (Integer) obj.get("bathrooms");
            r.spaceArea = 0.0;
            if(Utils.isFieldOK(obj, "spaceArea"))
                r.spaceArea = (double) obj.get("spaceArea");
            r.view="";
            if(Utils.isFieldOK(obj, "view"))
                r.view = (String) obj.get("view");

            JSONObject userobject = (JSONObject)obj.get("hostId");
            Users hostUser = Users.fromJSON(userobject);
            r.hostId = hostUser;

            JSONObject residenceObject = (JSONObject)obj.get("residenceId");
            Residences residence = Residences.fromJSON(residenceObject);
            r.residenceId = residence;

            ArrayList<Reservations> reservationsList = new ArrayList<>();
            JSONArray jsonArrayReservations = (JSONArray)obj.get("Reservations");

            for(int i=0;i<jsonArrayReservations.length();i++){
                JSONObject object = (JSONObject)jsonArrayReservations.get(i);
                Reservations reservation = Reservations.fromJSON(object);
                reservationsList.add(reservation);
            }
            r.reservationsCollection = reservationsList;

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
        if (!(object instanceof Rooms)) {
            return false;
        }
        Rooms other = (Rooms) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbpackage.Rooms[ id=" + id + " ]";
    }

}

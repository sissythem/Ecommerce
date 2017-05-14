package fromRESTful;

/**
 * Created by sissy on 8/5/2017.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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
    private Rooms roomId;
    private Users tenantId;

    public Reservations() {
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

    public Rooms getRoomId() {
        return roomId;
    }

    public void setRoomId(Rooms roomId) {
        this.roomId = roomId;
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
            String startDateString = (String)obj.get("startDate");
            r.startDate = Utils.ConvertStringToDate(startDateString, "yyyy-MM-dd'T'hh:mm:ssz");
            String endDateString = (String)obj.get("endDate");
            r.endDate = Utils.ConvertStringToDate(endDateString, "yyyy-MM-dd'T'hh:mm:ssz");
            r.guests = (int) obj.get("guests");
            JSONObject roomObject = (JSONObject)obj.get("roomId");
            r.roomId = Rooms.fromJSON(roomObject);
            JSONObject tenantObject = (JSONObject)obj.get("tenantId");
            r.tenantId = Users.fromJSON(tenantObject);
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

package fromRESTful;

import java.io.Serializable;

/** Reservations class to map the object from RESTful services **/
public class Reservations implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private long startDate;
    private long endDate;
    private int guests;
    private Residences residenceId;
    private Users tenantId;

    public Reservations() {}

    public Reservations(Users user, Residences residence, long date_start, long date_end, int guests) {
        this.tenantId       = user;
        this.residenceId    = residence;
        this.startDate      = date_start;
        this.endDate        = date_end;
        this.guests         = guests;
    }

    public Reservations(Integer id) {
        this.id = id;
    }

    public Reservations(Integer id, long startDate, long endDate, int guests) {
        this.id = id;
        this.startDate  = startDate;
        this.endDate    = endDate;
        this.guests     = guests;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getStartDate() {
        return startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) { this.endDate = endDate; }

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

    @Override
    public boolean equals(Object object)
    {
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
        return "domain.Reservations[ id=" + id + " ]";
    }
}
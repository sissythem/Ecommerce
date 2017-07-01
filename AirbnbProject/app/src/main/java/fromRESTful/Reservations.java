package fromRESTful;

import java.io.Serializable;
import java.util.Date;

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
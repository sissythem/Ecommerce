/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecommerce_rest;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author vasso
 */
@Entity
@Table(name = "reservations")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reservations.findAll", query = "SELECT r FROM Reservations r"),
    @NamedQuery(name = "Reservations.findById", query = "SELECT r FROM Reservations r WHERE r.id = :id"),
    @NamedQuery(name = "Reservations.findByStartDate", query = "SELECT r FROM Reservations r WHERE r.startDate = :startDate"),
    @NamedQuery(name = "Reservations.findByEndDate", query = "SELECT r FROM Reservations r WHERE r.endDate = :endDate"),
    @NamedQuery(name = "Reservations.findByGuests", query = "SELECT r FROM Reservations r WHERE r.guests = :guests")})
public class Reservations implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "end_date")
    @Temporal(TemporalType.DATE)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "guests")
    private int guests;
    @JoinColumn(name = "host_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users hostId;
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Rooms roomId;
    @JoinColumn(name = "tenant_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
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

    public Users getHostId() {
        return hostId;
    }

    public void setHostId(Users hostId) {
        this.hostId = hostId;
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
        return "ecommerce_rest.Reservations[ id=" + id + " ]";
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecommerce_rest;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author vasso
 */
@Entity
@Table(name = "rooms")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rooms.findAll", query = "SELECT r FROM Rooms r"),
    @NamedQuery(name = "Rooms.findById", query = "SELECT r FROM Rooms r WHERE r.id = :id"),
    @NamedQuery(name = "Rooms.findByResidenceId", query = "SELECT r FROM Rooms r WHERE r.residenceId = :residenceId"),
    @NamedQuery(name = "Rooms.findByHostId", query = "SELECT r FROM Rooms r WHERE r.hostId = :hostId"),
    @NamedQuery(name = "Rooms.findByBeds", query = "SELECT r FROM Rooms r WHERE r.beds = :beds"),
    @NamedQuery(name = "Rooms.findByBedType", query = "SELECT r FROM Rooms r WHERE r.bedType = :bedType"),
    @NamedQuery(name = "Rooms.findByBathrooms", query = "SELECT r FROM Rooms r WHERE r.bathrooms = :bathrooms"),
    @NamedQuery(name = "Rooms.findBySpaceArea", query = "SELECT r FROM Rooms r WHERE r.spaceArea = :spaceArea"),
    @NamedQuery(name = "Rooms.findByView", query = "SELECT r FROM Rooms r WHERE r.view = :view")})
public class Rooms implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "residence_id")
    private int residenceId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "host_id")
    private int hostId;
    @Column(name = "beds")
    private Integer beds;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bed_type")
    private int bedType;
    @Column(name = "bathrooms")
    private Integer bathrooms;
    @Basic(optional = false)
    @NotNull
    @Column(name = "space_area")
    private double spaceArea;
    @Size(max = 50)
    @Column(name = "view")
    private String view;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roomId")
    private Collection<Reservations> reservationsCollection;

    public Rooms() {
    }

    public Rooms(Integer id) {
        this.id = id;
    }

    public Rooms(Integer id, int residenceId, int hostId, int bedType, double spaceArea) {
        this.id = id;
        this.residenceId = residenceId;
        this.hostId = hostId;
        this.bedType = bedType;
        this.spaceArea = spaceArea;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getResidenceId() {
        return residenceId;
    }

    public void setResidenceId(int residenceId) {
        this.residenceId = residenceId;
    }

    public int getHostId() {
        return hostId;
    }

    public void setHostId(int hostId) {
        this.hostId = hostId;
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

    @XmlTransient
    public Collection<Reservations> getReservationsCollection() {
        return reservationsCollection;
    }

    public void setReservationsCollection(Collection<Reservations> reservationsCollection) {
        this.reservationsCollection = reservationsCollection;
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
        return "ecommerce_rest.Rooms[ id=" + id + " ]";
    }
    
}

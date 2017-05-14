/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbpackage;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author sissy
 */
@Entity
@Table(name = "residences")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Residences.findAll", query = "SELECT r FROM Residences r"),
    @NamedQuery(name = "Residences.findById", query = "SELECT r FROM Residences r WHERE r.id = :id"),
    @NamedQuery(name = "Residences.findByHostId", query = "SELECT r FROM Residences r WHERE r.hostId.id = :hostId"),
    @NamedQuery(name = "Residences.findByType", query = "SELECT r FROM Residences r WHERE r.type = :type"),
    @NamedQuery(name = "Residences.findByAbout", query = "SELECT r FROM Residences r WHERE r.about = :about"),
    @NamedQuery(name = "Residences.findByCancellationPolicy", query = "SELECT r FROM Residences r WHERE r.cancellationPolicy = :cancellationPolicy"),
    @NamedQuery(name = "Residences.findByCountry", query = "SELECT r FROM Residences r WHERE r.country = :country"),
    @NamedQuery(name = "Residences.findByCity", query = "SELECT r FROM Residences r WHERE r.city = :city"),
    @NamedQuery(name = "Residences.findByAddress", query = "SELECT r FROM Residences r WHERE r.address = :address"),
    @NamedQuery(name = "Residences.findByRules", query = "SELECT r FROM Residences r WHERE r.rules = :rules"),
    @NamedQuery(name = "Residences.findByAmenities", query = "SELECT r FROM Residences r WHERE r.amenities = :amenities"),
    @NamedQuery(name = "Residences.findByFloor", query = "SELECT r FROM Residences r WHERE r.floor = :floor"),
    @NamedQuery(name = "Residences.findByRooms", query = "SELECT r FROM Residences r WHERE r.rooms = :rooms"),
    @NamedQuery(name = "Residences.findByBaths", query = "SELECT r FROM Residences r WHERE r.baths = :baths"),
    @NamedQuery(name = "Residences.findByKitchen", query = "SELECT r FROM Residences r WHERE r.kitchen = :kitchen"),
    @NamedQuery(name = "Residences.findByLivingRoom", query = "SELECT r FROM Residences r WHERE r.livingRoom = :livingRoom"),
    @NamedQuery(name = "Residences.findByView", query = "SELECT r FROM Residences r WHERE r.view = :view"),
    @NamedQuery(name = "Residences.findBySpaceArea", query = "SELECT r FROM Residences r WHERE r.spaceArea = :spaceArea"),
    @NamedQuery(name = "Residences.findByPhotos", query = "SELECT r FROM Residences r WHERE r.photos = :photos"),
    @NamedQuery(name = "Residences.findByGuests", query = "SELECT r FROM Residences r WHERE r.guests = :guests"),
    @NamedQuery(name = "Residences.findByAvailableDateStart", query = "SELECT r FROM Residences r WHERE r.availableDateStart = :availableDateStart"),
    @NamedQuery(name = "Residences.findByAvailableDateEnd", query = "SELECT r FROM Residences r WHERE r.availableDateEnd = :availableDateEnd"),
    @NamedQuery(name = "Residences.findByMinPrice", query = "SELECT r FROM Residences r WHERE r.minPrice = :minPrice"),
    @NamedQuery(name = "Residences.findByAdditionalCostPerPerson", query = "SELECT r FROM Residences r WHERE r.additionalCostPerPerson = :additionalCostPerPerson")})
public class Residences implements Serializable {

    @Column(name = "floor")
    private Integer floor;
    @Column(name = "rooms")
    private Integer rooms;
    @Column(name = "baths")
    private Integer baths;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "space_area")
    private Double spaceArea;
    @Column(name = "guests")
    private Integer guests;
    @Column(name = "min_price")
    private Double minPrice;
    @Column(name = "additional_cost_per_person")
    private Double additionalCostPerPerson;
    @JoinColumn(name = "host_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users hostId;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    
    @Size(min = 1, max = 45)
    @Column(name = "type")
    private String type;

    @Size(min = 1, max = 45)
    @Column(name = "about")
    private String about;
   
    @Size(min = 1, max = 45)
    @Column(name = "cancellation_policy")
    private String cancellationPolicy;
  
    @Size(min = 1, max = 45)
    @Column(name = "country")
    private String country;
 
    @Size(min = 1, max = 45)
    @Column(name = "city")
    private String city;
  
    @Size(min = 1, max = 45)
    @Column(name = "address")
    private String address;
  
    @Size(min = 1, max = 255)
    @Column(name = "rules")
    private String rules;
    
    @Size(min = 1, max = 255)
    @Column(name = "amenities")
    private String amenities;
    
    
    @Column(name = "kitchen")
    private Boolean kitchen;
    
    @Column(name = "living_room")
    private Boolean livingRoom;
    
    @Size(max = 50)
    @Column(name = "view")
    private String view;
    
    
    @Size(min = 1, max = 45)
    @Column(name = "photos")
    private String photos;
    
    
    @Column(name = "available_date_start")
    @Temporal(TemporalType.DATE)
    private Date availableDateStart;
    
    @Column(name = "available_date_end")
    @Temporal(TemporalType.DATE)
    private Date availableDateEnd;
    
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "residenceId")
    private Collection<Rooms> roomsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "residenceId")
    private Collection<Reviews> reviewsCollection;

    public Residences() {
    }

    public Residences(Integer id) {
        this.id = id;
    }

    public Residences(Integer id, Users hostId, String type, String about, String cancellationPolicy, String country, String city, String address, String rules, String amenities, int floor, int rooms, int baths, double spaceArea, String photos, int guests, Date availableDateStart, Date availableDateEnd, double minPrice, double additionalCostPerPerson) {
        this.id = id;
        this.hostId = hostId;
        this.type = type;
        this.about = about;
        this.cancellationPolicy = cancellationPolicy;
        this.country = country;
        this.city = city;
        this.address = address;
        this.rules = rules;
        this.amenities = amenities;
        this.floor = floor;
        this.rooms = rooms;
        this.baths = baths;
        this.spaceArea = spaceArea;
        this.photos = photos;
        this.guests = guests;
        this.availableDateStart = availableDateStart;
        this.availableDateEnd = availableDateEnd;
        this.minPrice = minPrice;
        this.additionalCostPerPerson = additionalCostPerPerson;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getHostId() {
        return hostId;
    }

    public void setHostId(Users hostId) {
        this.hostId = hostId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getCancellationPolicy() {
        return cancellationPolicy;
    }

    public void setCancellationPolicy(String cancellationPolicy) {
        this.cancellationPolicy = cancellationPolicy;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRules() {
        return rules;
    }

    public void setRules(String rules) {
        this.rules = rules;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }


    public Boolean getKitchen() {
        return kitchen;
    }

    public void setKitchen(Boolean kitchen) {
        this.kitchen = kitchen;
    }

    public Boolean getLivingRoom() {
        return livingRoom;
    }

    public void setLivingRoom(Boolean livingRoom) {
        this.livingRoom = livingRoom;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public double getSpaceArea() {
        return spaceArea;
    }

    public void setSpaceArea(double spaceArea) {
        this.spaceArea = spaceArea;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }


    public Date getAvailableDateStart() {
        return availableDateStart;
    }

    public void setAvailableDateStart(Date availableDateStart) {
        this.availableDateStart = availableDateStart;
    }

    public Date getAvailableDateEnd() {
        return availableDateEnd;
    }

    public void setAvailableDateEnd(Date availableDateEnd) {
        this.availableDateEnd = availableDateEnd;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getAdditionalCostPerPerson() {
        return additionalCostPerPerson;
    }

    public void setAdditionalCostPerPerson(double additionalCostPerPerson) {
        this.additionalCostPerPerson = additionalCostPerPerson;
    }

    @XmlTransient
    public Collection<Rooms> getRoomsCollection() {
        return roomsCollection;
    }

    public void setRoomsCollection(Collection<Rooms> roomsCollection) {
        this.roomsCollection = roomsCollection;
    }

    @XmlTransient
    public Collection<Reviews> getReviewsCollection() {
        return reviewsCollection;
    }

    public void setReviewsCollection(Collection<Reviews> reviewsCollection) {
        this.reviewsCollection = reviewsCollection;
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
        if (!(object instanceof Residences)) {
            return false;
        }
        Residences other = (Residences) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "dbpackage.Residences[ id=" + id + " ]";
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getRooms() {
        return rooms;
    }

    public void setRooms(Integer rooms) {
        this.rooms = rooms;
    }

    public Integer getBaths() {
        return baths;
    }

    public void setBaths(Integer baths) {
        this.baths = baths;
    }

    public void setSpaceArea(Double spaceArea) {
        this.spaceArea = spaceArea;
    }

    public Integer getGuests() {
        return guests;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
    }


    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public void setAdditionalCostPerPerson(Double additionalCostPerPerson) {
        this.additionalCostPerPerson = additionalCostPerPerson;
    }
    
}

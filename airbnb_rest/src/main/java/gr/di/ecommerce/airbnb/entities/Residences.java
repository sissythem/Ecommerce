package gr.di.ecommerce.airbnb.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@Entity
@Table(name = "residences")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Residences.findAll", query = "SELECT r FROM Residences r"),
    @NamedQuery(name = "Residences.findById", query = "SELECT r FROM Residences r WHERE r.id = :id"),
    @NamedQuery(name = "Residences.findByType", query = "SELECT r FROM Residences r WHERE r.type = :type"),
    @NamedQuery(name = "Residences.findByAbout", query = "SELECT r FROM Residences r WHERE r.about = :about"),
    @NamedQuery(name = "Residences.findByCancellationPolicy", query = "SELECT r FROM Residences r WHERE r.cancellationPolicy = :cancellationPolicy"),
    @NamedQuery(name = "Residences.findByCountry", query = "SELECT r FROM Residences r WHERE r.country = :country"),
    @NamedQuery(name = "Residences.findByAddress", query = "SELECT r FROM Residences r WHERE r.address = :address"),
    @NamedQuery(name = "Residences.findByCity", query = "SELECT r FROM Residences r WHERE r.city = :city"),
    @NamedQuery(name = "Residences.findByRules", query = "SELECT r FROM Residences r WHERE r.rules = :rules"),
    @NamedQuery(name = "Residences.findByAmenities", query = "SELECT r FROM Residences r WHERE r.amenities = :amenities"),
    @NamedQuery(name = "Residences.findByFloor", query = "SELECT r FROM Residences r WHERE r.floor = :floor"),
    @NamedQuery(name = "Residences.findByRooms", query = "SELECT r FROM Residences r WHERE r.rooms = :rooms"),
    @NamedQuery(name = "Residences.findByBaths", query = "SELECT r FROM Residences r WHERE r.baths = :baths"),
    @NamedQuery(name = "Residences.findByKitchen", query = "SELECT r FROM Residences r WHERE r.kitchen = :kitchen"),
    @NamedQuery(name = "Residences.findByLivingRoom", query = "SELECT r FROM Residences r WHERE r.livingRoom = :livingRoom"),
    @NamedQuery(name = "Residences.findByView", query = "SELECT r FROM Residences r WHERE r.view = :view"),
    @NamedQuery(name = "Residences.findByPhotos", query = "SELECT r FROM Residences r WHERE r.photos = :photos"),
    @NamedQuery(name = "Residences.findByGuests", query = "SELECT r FROM Residences r WHERE r.guests = :guests"),
    @NamedQuery(name = "Residences.findByAvailableDateStart", query = "SELECT r FROM Residences r WHERE r.availableDateStart = :availableDateStart"),
    @NamedQuery(name = "Residences.findByAvailableDateEnd", query = "SELECT r FROM Residences r WHERE r.availableDateEnd = :availableDateEnd"),
    @NamedQuery(name = "Residences.findByMinPrice", query = "SELECT r FROM Residences r WHERE r.minPrice = :minPrice"),
    @NamedQuery(name = "Residences.findByAdditionalCostPerPerson", query = "SELECT r FROM Residences r WHERE r.additionalCostPerPerson = :additionalCostPerPerson"),
    
    /* Custom */
    @NamedQuery(name = "findByHost", query = "SELECT r FROM Residences r WHERE r.hostId.id = :hostId")
    
})
public class Residences implements Serializable {
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 65535)
    @Column(name = "title")
    private String title;
    @Size(max = 45)
    @Column(name = "type")
    private String type;
    @Size(max = 255)
    @Column(name = "about")
    private String about;
    @Size(max = 255)
    @Column(name = "cancellation_policy")
    private String cancellationPolicy;
    @Size(max = 45)
    @Column(name = "country")
    private String country;
    @Size(max = 45)
    @Column(name = "address")
    private String address;
    @Size(max = 45)
    @Column(name = "city")
    private String city;
    @Size(max = 255)
    @Column(name = "rules")
    private String rules;
    @Size(max = 255)
    @Column(name = "amenities")
    private String amenities;
    @Column(name = "floor")
    private Integer floor;
    @Column(name = "rooms")
    private Integer rooms;
    @Column(name = "baths")
    private Integer baths;
    @Column(name = "kitchen")
    private short kitchen;
    @Column(name = "living_room")
    private short livingRoom;
    @Size(max = 50)
    @Column(name = "view")
    private String view;
    @Size(max = 45)
    @Column(name = "photos")
    private String photos;
    @Column(name = "guests")
    private Integer guests;
    @Column(name = "available_date_start")
    private LocalDate availableDateStart;
    @Column(name = "available_date_end")
    private LocalDate availableDateEnd;
    @Column(name = "min_price")
    private Double minPrice;
    @Column(name = "additional_cost_per_person")
    private Double additionalCostPerPerson;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "residenceId")
    private Collection<Reservations> reservationsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "residenceId")
    private Collection<Reviews> reviewsCollection;
    @JoinColumn(name = "host_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users hostId;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "residenceId")
    private Collection<Conversations> conversationsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "residenceId")
    private Collection<Images> imagesCollection;
    
    public Residences() {
    }

    public Residences(Integer id) {
        this.id = id;
    }

    public Residences(Integer id, String title) {
        this.id = id;
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public short getKitchen() {
        return kitchen;
    }

    public void setKitchen(short kitchen) {
        this.kitchen = kitchen;
    }

    public short getLivingRoom() {
        return livingRoom;
    }

    public void setLivingRoom(short livingRoom) {
        this.livingRoom = livingRoom;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getPhotos() {
        return photos;
    }

    public void setPhotos(String photos) {
        this.photos = photos;
    }

    public Integer getGuests() {
        return guests;
    }

    public void setGuests(Integer guests) {
        this.guests = guests;
    }

    public LocalDate getAvailableDateStart() {
        return availableDateStart;
    }

    public void setAvailableDateStart(LocalDate availableDateStart) {
        this.availableDateStart = availableDateStart;
    }

    public LocalDate getAvailableDateEnd() {
        return availableDateEnd;
    }

    public void setAvailableDateEnd(LocalDate availableDateEnd) {
        this.availableDateEnd = availableDateEnd;
    }

    public Double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Double minPrice) {
        this.minPrice = minPrice;
    }

    public Double getAdditionalCostPerPerson() {
        return additionalCostPerPerson;
    }

    public void setAdditionalCostPerPerson(Double additionalCostPerPerson) {
        this.additionalCostPerPerson = additionalCostPerPerson;
    }

    @XmlTransient
    public Collection<Reservations> getReservationsCollection() {
        return reservationsCollection;
    }

    public void setReservationsCollection(Collection<Reservations> reservationsCollection) {
        this.reservationsCollection = reservationsCollection;
    }

    @XmlTransient
    public Collection<Reviews> getReviewsCollection() {
        return reviewsCollection;
    }

    public void setReviewsCollection(Collection<Reviews> reviewsCollection) {
        this.reviewsCollection = reviewsCollection;
    }

    public Users getHostId() {
        return hostId;
    }

    public void setHostId(Users hostId) {
        this.hostId = hostId;
    }

    @XmlTransient
    public Collection<Conversations> getConversationsCollection() {
        return conversationsCollection;
    }

    public void setConversationsCollection(Collection<Conversations> conversationsCollection) {
        this.conversationsCollection = conversationsCollection;
    }
    
    @XmlTransient
    public Collection<Images> getImagesCollection() {
        return imagesCollection;
    }
    
    public void setImagesCollection(Collection<Images> imagesCollection) {
        this.imagesCollection = imagesCollection;
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
        return "domain.Residences[ id=" + id + " ]";
    }
}
package fromRESTful;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/** Residences class to map the object from RESTful services **/
public class Residences implements Serializable, Comparable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Users hostId;
    private String title;
    private String type;
    private String about;
    private String cancellationPolicy;
    private String country;
    private String city;
    private String address;
    private String rules;
    private String amenities;
    private int floor;
    private int rooms;
    private int baths;
    private short kitchen;
    private short livingRoom;
    private String view;
    private String photos;
    private int guests;
    private long availableDateStart;
    private long availableDateEnd;
    private double minPrice;
    private double additionalCostPerPerson;
    private Collection<Reviews> reviewsCollection;
    private Collection<Conversations> conversationsCollection;

    public Residences(){}

    /** Add new Residence **/
    public Residences(Users host,
                      String title,
                      String type,
                      String about,
                      String address,
                      String city,
                      String country,
                      String amenities,
                      int floor,
                      int rooms,
                      int baths,
                      String view,
                      int guests,
                      double minPrice,
                      double additionalCostPerPerson,
                      String cancellationPolicy,
                      String rules,
                      short kitchen,
                      short livingRoom,
                      long startDate,
                      long endDate)
    {
        this.hostId                     = host;
        this.title                      = title;
        this.type                       = type;
        this.about                      = about;
        this.address                    = address;
        this.city                       = city;
        this.country                    = country;
        this.amenities                  = amenities;
        this.floor                      = floor;
        this.rooms                      = rooms;
        this.baths                      = baths;
        this.view                       = view;
        this.guests                     = guests;
        this.minPrice                   = minPrice;
        this.additionalCostPerPerson    = additionalCostPerPerson;
        this.cancellationPolicy         = cancellationPolicy;
        this.rules                      = rules;
        this.kitchen                    = kitchen;
        this.livingRoom                 = livingRoom;
        this.availableDateStart         = startDate;
        this.availableDateEnd           = endDate;
    }

    /** Edit Residence **/
    public Residences(Integer residenceId,
                      Users host,
                      String title,
                      String type,
                      String about,
                      String address,
                      String city,
                      String country,
                      String amenities,
                      int floor,
                      int rooms,
                      int baths,
                      String view,
                      int guests,
                      double minPrice,
                      double additionalCostPerPerson,
                      String cancellationPolicy,
                      String rules,
                      short kitchen,
                      short livingRoom,
                      long startDate,
                      long endDate,
                      String photo)
    {
        this.id                         = residenceId;
        this.hostId                     = host;
        this.title                      = title;
        this.type                       = type;
        this.about                      = about;
        this.address                    = address;
        this.city                       = city;
        this.country                    = country;
        this.amenities                  = amenities;
        this.floor                      = floor;
        this.rooms                      = rooms;
        this.baths                      = baths;
        this.view                       = view;
        this.guests                     = guests;
        this.minPrice                   = minPrice;
        this.additionalCostPerPerson    = additionalCostPerPerson;
        this.cancellationPolicy         = cancellationPolicy;
        this.rules                      = rules;
        this.kitchen                    = kitchen;
        this.livingRoom                 = livingRoom;
        this.availableDateStart         = startDate;
        this.availableDateEnd           = endDate;
        this.photos                     = photo;
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

    public void setCancellationPolicy(String cancellationPolicy) { this.cancellationPolicy = cancellationPolicy; }

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

    public String getAmenities() { return amenities; }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public int getBaths() {
        return baths;
    }

    public void setBaths(int baths) {
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

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }

    public long getAvailableDateStart() {
        return availableDateStart;
    }

    public void setAvailableDateStart(long availableDateStart) { this.availableDateStart = availableDateStart; }

    public long getAvailableDateEnd() {
        return availableDateEnd;
    }

    public void setAvailableDateEnd(long availableDateEnd) { this.availableDateEnd = availableDateEnd; }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getAdditionalCostPerPerson() {
        return additionalCostPerPerson;
    }

    public void setAdditionalCostPerPerson(double additionalCostPerPerson) { this.additionalCostPerPerson = additionalCostPerPerson; }

    public Collection<Reviews> getReviewsCollection() {
        return reviewsCollection;
    }

    public void setReviewsCollection(Collection<Reviews> reviewsCollection) { this.reviewsCollection = reviewsCollection; }

    public Collection<Conversations> getConversationsCollection() { return conversationsCollection; }

    public void setConversationsCollection(Collection<Conversations> conversationsCollection) { this.conversationsCollection = conversationsCollection; }

    public double getAverageRating(){
        double rating = 0.0;
        double averageRating;
        if(reviewsCollection == null){
            averageRating=0.0;
        } else {
            for(int i=0;i<reviewsCollection.size();i++){
                rating = rating + ((ArrayList<Reviews>)reviewsCollection).get(i).getRating();
            }
            averageRating = rating/reviewsCollection.size();
        }
        return averageRating;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /** We use this method to find if the Residence is the same with another in HomeActivity
     * If they are the same they are excluded from the HashSet**/
    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
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

    /** We use the compareTo method in order to sort the residences in HomeActivity**/
    @Override
    public int compareTo(@NonNull Object o) {
        Double averageRating = this.getAverageRating();
        Double otherAverageRating = ((Residences) o).getAverageRating();
        return  -averageRating.compareTo(otherAverageRating);
    }
}
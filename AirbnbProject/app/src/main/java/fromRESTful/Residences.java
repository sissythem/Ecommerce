package fromRESTful;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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
    private Boolean kitchen;
    private Boolean livingRoom;
    private String view;
    private double spaceArea;
    private String photos;
    private int guests;
    private Date availableDateStart;
    private Date availableDateEnd;
    private double minPrice;
    private double additionalCostPerPerson;
    private Collection<Reviews> reviewsCollection;
    private Collection<Conversations> conversationsCollection;
    private short active;

    public Residences(Users host, String title, String type, String about, String address, String city, String country, String amenities, int floor, int rooms,
                      int baths, String view, double spaceArea, int guests, double minPrice, double additionalCostPerPerson, String cancellationPolicy,
                      String rules, boolean kitchen, boolean b, boolean livingRoom, Date startDate, Date endDate, String photo)
    {
        this.hostId=host;
        this.title = title;
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
        this.photos = photo;
        this.guests = guests;
        this.availableDateStart = startDate;
        this.availableDateEnd = endDate;
        this.minPrice = minPrice;
        this.additionalCostPerPerson = additionalCostPerPerson;
        this.view=view;
        this.kitchen=kitchen;
        this.livingRoom=livingRoom;
    }

    public Residences(Users hostId, String title, String type, String about, String cancellationPolicy, String country, String city, String address, String rules, String amenities,
        int floor, int rooms, int baths, double spaceArea, String photos, int guests, Date availableDateStart, Date availableDateEnd, double minPrice,
        double additionalCostPerPerson) {

        this.hostId = hostId;
        this.title = title;
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

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
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

    public short getActive() {return active;}

    public void setActive(short active) {this.active = active;}

    public Collection<Reviews> getReviewsCollection() {
        return reviewsCollection;
    }

    public void setReviewsCollection(Collection<Reviews> reviewsCollection) {
        this.reviewsCollection = reviewsCollection;
    }

    public Collection<Conversations> getConversationsCollection() {
        return conversationsCollection;
    }

    public void setConversationsCollection(Collection<Conversations> conversationsCollection) {
        this.conversationsCollection = conversationsCollection;
    }

//    public static Residences fromJSON (JSONObject obj){
//        Residences r = new Residences(host, title, type, about, address, city, country, amenities, floor, rooms, baths, view, spaceArea, guests, minPrice, additionalCostPerPerson, cancellationPolicy, rules, kitchen, kitchen, livingRoom, startDate, endDate, photo);
//        try {
//            r.id = (Integer) obj.get("id");
//
//            JSONObject userobject = (JSONObject)obj.get("hostId");
//            Users hostUser = Users.fromJSON(userobject);
//            r.hostId = hostUser;
//
//            r.title="";
//            if(Utils.isFieldOK(obj, "title")) r.title = (String) obj.get("title");
//
//            r.type="";
//            if(Utils.isFieldOK(obj, "type")) r.type = (String) obj.get("type");
//            r.about="";
//            if(Utils.isFieldOK(obj, "about")) r.about = (String) obj.get("about");
//            r.cancellationPolicy="";
//            if(Utils.isFieldOK(obj, "cancellationPolicy")) r.cancellationPolicy = (String) obj.get("cancellationPolicy");
//            r.country="";
//            if(Utils.isFieldOK(obj, "country")) r.country = (String) obj.get("country");
//            r.city="";
//            if(Utils.isFieldOK(obj, "city")) r.city = (String) obj.get("city");
//            r.address="";
//            if(Utils.isFieldOK(obj, "address")) r.address = (String) obj.get("address");
//            r.rules="";
//            if(Utils.isFieldOK(obj, "rules")) r.rules = (String) obj.get("rules");
//            r.amenities="";
//            if(Utils.isFieldOK(obj, "amenities")) r.amenities = (String) obj.get("amenities");
//            r.floor=0;
//            if(Utils.isFieldOK(obj, "floor")) r.floor = (int) obj.get("floor");
//            r.rooms=0;
//            if(Utils.isFieldOK(obj, "rooms")) r.rooms = (int) obj.get("rooms");
//            r.baths=0;
//            if(Utils.isFieldOK(obj, "baths")) r.baths = (int) obj.get("baths");
//            r.kitchen=false;
//            if(Utils.isFieldOK(obj, "kitchen")) r.kitchen = (Boolean) obj.get("kitchen");
//            r.livingRoom=false;
//            if(Utils.isFieldOK(obj, "livingRoom")) r.livingRoom = (Boolean) obj.get("livingRoom");
//            r.view="";
//            if(Utils.isFieldOK(obj, "view")) r.view = (String) obj.get("view");
//            r.spaceArea=0.0;
//            if(Utils.isFieldOK(obj, "spaceArea")) r.spaceArea = (double) obj.get("spaceArea");
//            r.photos="";
//            if(Utils.isFieldOK(obj, "photos")) r.photos = (String) obj.get("photos");
//            r.guests=0;
//            if(Utils.isFieldOK(obj, "guests")) r.guests = (int) obj.get("guests");
//
//            if(Utils.isFieldOK(obj, "availableDateStart")) {
//                String availableDateStartString = (String) obj.get("availableDateStart");
//                r.availableDateStart = Utils.ConvertStringToDate(availableDateStartString, DATABASE_DATE_FORMAT);
//            }
//            if(Utils.isFieldOK(obj, "availableDateEnd")) {
//                String availableDateEndString = (String) obj.get("availableDateEnd");
//                r.availableDateEnd = Utils.ConvertStringToDate(availableDateEndString, DATABASE_DATE_FORMAT);
//            }
//            r.minPrice=0.0;
//            if(Utils.isFieldOK(obj, "minPrice")) r.minPrice = (double) obj.get("minPrice");
//            r.additionalCostPerPerson=0.0;
//            if(Utils.isFieldOK(obj, "additionalCostPerPerson"))
//                r.additionalCostPerPerson = (double) obj.get("additionalCostPerPerson");
//
//            if(Utils.isFieldOK(obj, "Reviews"))
//            {
//                ArrayList<Reviews> reviewsList = new ArrayList<>();
//                JSONArray jsonArrayReviews = (JSONArray) obj.get("Reviews");
//                for (int i = 0; i < jsonArrayReviews.length(); i++) {
//                    JSONObject object = (JSONObject) jsonArrayReviews.get(i);
//                    Reviews review = Reviews.fromJSON(object);
//                    reviewsList.add(review);
//                }
//                r.reviewsCollection = reviewsList;
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        catch(Exception e){
//            e.printStackTrace();
//        }
//        return r;
//    }
//
//    public JSONObject toJSON (){
//        JSONObject jsonResidence = new JSONObject();
//        try {
//            jsonResidence.put("id", id.toString());
//
//            JSONObject jsonUser = hostId.toJSON();
//            jsonResidence.put("hostId", jsonUser);
//
//            jsonResidence.put("title", title);
//            jsonResidence.put("type", type);
//            jsonResidence.put("about", about);
//            jsonResidence.put("cancellationPolicy", cancellationPolicy);
//            jsonResidence.put("address", address);
//            jsonResidence.put("rules", rules);
//            jsonResidence.put("country", country);
//            jsonResidence.put("city", city);
//            jsonResidence.put("amenities", amenities);
//            jsonResidence.put("floor", floor);
//            jsonResidence.put("rooms", rooms);
//            jsonResidence.put("baths", baths);
//            jsonResidence.put("spaceArea", spaceArea);
//            jsonResidence.put("photos", photos);
//            jsonResidence.put("guests", guests);
//            jsonResidence.put("minPrice", minPrice);
//            jsonResidence.put("additionalCostPerPerson", additionalCostPerPerson);
//
//            String startdate = Utils.ConvertDateToString(availableDateStart, DATABASE_DATE_FORMAT);
//            jsonResidence.put("availableDateStart", startdate);
//
//            String enddate = Utils.ConvertDateToString(availableDateEnd, DATABASE_DATE_FORMAT);
//            jsonResidence.put("availableDateEnd", enddate);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return jsonResidence;
//    }
//
    public double getAverageRating(){
        double rating = 0.0;
        double averageRating;
        if(reviewsCollection == null){
            averageRating=0.0;
        }
        else{
            for(int i=0;i<reviewsCollection.size();i++){
                rating = rating + ((ArrayList<Reviews>)reviewsCollection).get(i).getRating();
            }
            averageRating = rating/reviewsCollection.size();
        }
        return averageRating;
    }

    /*
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    } */

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
        return "dbpackage.Residences[ id=" + id + " ]";
    }

    @Override
    public int compareTo(@NonNull Object o) {
        Double averageRating = this.getAverageRating();
        Double otherAverageRating = ((Residences) o).getAverageRating();
        return  -averageRating.compareTo(otherAverageRating);
    }
}
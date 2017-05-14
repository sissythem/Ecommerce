package dbpackage;

import dbpackage.Reviews;
import dbpackage.Rooms;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-05T17:43:03")
@StaticMetamodel(Residences.class)
public class Residences_ { 

    public static volatile SingularAttribute<Residences, String> country;
    public static volatile SingularAttribute<Residences, String> amenities;
    public static volatile SingularAttribute<Residences, Integer> rooms;
    public static volatile SingularAttribute<Residences, String> city;
    public static volatile SingularAttribute<Residences, Double> additionalCostPerPerson;
    public static volatile SingularAttribute<Residences, String> about;
    public static volatile SingularAttribute<Residences, String> rules;
    public static volatile SingularAttribute<Residences, String> type;
    public static volatile CollectionAttribute<Residences, Reviews> reviewsCollection;
    public static volatile SingularAttribute<Residences, String> photos;
    public static volatile SingularAttribute<Residences, String> view;
    public static volatile SingularAttribute<Residences, Integer> id;
    public static volatile SingularAttribute<Residences, Integer> floor;
    public static volatile SingularAttribute<Residences, Boolean> kitchen;
    public static volatile SingularAttribute<Residences, Date> availableDateEnd;
    public static volatile CollectionAttribute<Residences, Rooms> roomsCollection;
    public static volatile SingularAttribute<Residences, String> address;
    public static volatile SingularAttribute<Residences, Integer> hostId;
    public static volatile SingularAttribute<Residences, Boolean> livingRoom;
    public static volatile SingularAttribute<Residences, Double> spaceArea;
    public static volatile SingularAttribute<Residences, Integer> baths;
    public static volatile SingularAttribute<Residences, Double> minPrice;
    public static volatile SingularAttribute<Residences, Integer> guests;
    public static volatile SingularAttribute<Residences, Date> availableDateStart;
    public static volatile SingularAttribute<Residences, String> cancellationPolicy;

}
package dbpackage;

import dbpackage.Conversations;
import dbpackage.Reservations;
import dbpackage.Reviews;
import dbpackage.Rooms;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-05T17:43:03")
@StaticMetamodel(Users.class)
public class Users_ { 

    public static volatile CollectionAttribute<Users, Rooms> roomsCollection;
    public static volatile SingularAttribute<Users, String> lastName;
    public static volatile SingularAttribute<Users, String> country;
    public static volatile CollectionAttribute<Users, Conversations> conversationsCollection;
    public static volatile CollectionAttribute<Users, Reservations> reservationsCollection;
    public static volatile SingularAttribute<Users, String> city;
    public static volatile SingularAttribute<Users, String> about;
    public static volatile SingularAttribute<Users, String> photo;
    public static volatile CollectionAttribute<Users, Reviews> reviewsCollection;
    public static volatile SingularAttribute<Users, Date> birthDate;
    public static volatile CollectionAttribute<Users, Reviews> reviewsCollection1;
    public static volatile SingularAttribute<Users, String> firstName;
    public static volatile SingularAttribute<Users, String> password;
    public static volatile SingularAttribute<Users, String> phoneNumber;
    public static volatile CollectionAttribute<Users, Conversations> conversationsCollection1;
    public static volatile SingularAttribute<Users, Date> registrationDate;
    public static volatile SingularAttribute<Users, String> host;
    public static volatile SingularAttribute<Users, Integer> id;
    public static volatile SingularAttribute<Users, String> email;
    public static volatile SingularAttribute<Users, String> username;

}
package dbpackage;

import dbpackage.Reservations;
import dbpackage.Residences;
import dbpackage.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-05T17:43:03")
@StaticMetamodel(Rooms.class)
public class Rooms_ { 

    public static volatile SingularAttribute<Rooms, String> view;
    public static volatile CollectionAttribute<Rooms, Reservations> reservationsCollection;
    public static volatile SingularAttribute<Rooms, Users> hostId;
    public static volatile SingularAttribute<Rooms, Integer> id;
    public static volatile SingularAttribute<Rooms, Integer> beds;
    public static volatile SingularAttribute<Rooms, Integer> bathrooms;
    public static volatile SingularAttribute<Rooms, Integer> bedType;
    public static volatile SingularAttribute<Rooms, Residences> residenceId;
    public static volatile SingularAttribute<Rooms, Double> spaceArea;

}
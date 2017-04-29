package ecommerce_rest;

import ecommerce_rest.Rooms;
import ecommerce_rest.Users;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T19:31:39")
@StaticMetamodel(Reservations.class)
public class Reservations_ { 

    public static volatile SingularAttribute<Reservations, Date> endDate;
    public static volatile SingularAttribute<Reservations, Integer> guests;
    public static volatile SingularAttribute<Reservations, Users> tenantId;
    public static volatile SingularAttribute<Reservations, Users> hostId;
    public static volatile SingularAttribute<Reservations, Integer> id;
    public static volatile SingularAttribute<Reservations, Date> startDate;
    public static volatile SingularAttribute<Reservations, Rooms> roomId;

}
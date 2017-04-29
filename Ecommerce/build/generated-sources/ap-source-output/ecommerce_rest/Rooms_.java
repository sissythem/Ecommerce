package ecommerce_rest;

import ecommerce_rest.Reservations;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-04-28T19:31:39")
@StaticMetamodel(Rooms.class)
public class Rooms_ { 

    public static volatile SingularAttribute<Rooms, String> view;
    public static volatile CollectionAttribute<Rooms, Reservations> reservationsCollection;
    public static volatile SingularAttribute<Rooms, Integer> hostId;
    public static volatile SingularAttribute<Rooms, Integer> id;
    public static volatile SingularAttribute<Rooms, Integer> beds;
    public static volatile SingularAttribute<Rooms, Integer> bathrooms;
    public static volatile SingularAttribute<Rooms, Integer> residenceId;
    public static volatile SingularAttribute<Rooms, Integer> bedType;
    public static volatile SingularAttribute<Rooms, Double> spaceArea;

}
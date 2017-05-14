package dbpackage;

import dbpackage.Residences;
import dbpackage.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-05T17:43:03")
@StaticMetamodel(Reviews.class)
public class Reviews_ { 

    public static volatile SingularAttribute<Reviews, Double> rating;
    public static volatile SingularAttribute<Reviews, Users> tenantId;
    public static volatile SingularAttribute<Reviews, Users> hostId;
    public static volatile SingularAttribute<Reviews, String> comment;
    public static volatile SingularAttribute<Reviews, Integer> id;
    public static volatile SingularAttribute<Reviews, Residences> residenceId;

}
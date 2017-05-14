package dbpackage;

import dbpackage.Messages;
import dbpackage.Users;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2017-05-05T17:43:03")
@StaticMetamodel(Conversations.class)
public class Conversations_ { 

    public static volatile SingularAttribute<Conversations, Users> senderId;
    public static volatile SingularAttribute<Conversations, Users> receiverId;
    public static volatile SingularAttribute<Conversations, String> subject;
    public static volatile SingularAttribute<Conversations, Integer> id;
    public static volatile CollectionAttribute<Conversations, Messages> messagesCollection;

}
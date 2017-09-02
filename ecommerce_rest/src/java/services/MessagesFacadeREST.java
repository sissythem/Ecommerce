package services;

import domain.Messages;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import utils.KeyHolder;

@Stateless
@Path("messages")
public class MessagesFacadeREST extends AbstractFacade<Messages> {

    @PersistenceContext(unitName = "ecommerce_restPU")
    private EntityManager em;

    public MessagesFacadeREST() {
        super(Messages.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Messages entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Messages entity) {
        super.edit(entity);
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Messages> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces(MediaType.TEXT_PLAIN)
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    /*** CUSTOM METHODS ***/
    
    private static String className = MessagesFacadeREST.class.getName();
    
    @DELETE
    @Path("delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String remove(@HeaderParam("Authorization") String token, @PathParam("id")Integer id) {
        if (KeyHolder.checkToken(token, className)) {
            super.remove(super.find(id));
        } else {
            token = KeyHolder.issueToken(null);
        }
        return token;
    }
    
    @POST
    @Path("deletemsg/{id}/{user}/{type}")
    @Produces({MediaType.APPLICATION_JSON})
    public String removeMSG(@HeaderParam("Authorization") String token, @PathParam("id")Integer id, @PathParam("user")Integer user, @PathParam("type")String type) {
        if (KeyHolder.checkToken(token, className)) {
            String userType = "";
            String cUser = "";
            if (type.equals("sender")) {
                userType = "deleted_from_sender";
                cUser = "sender_id";
            } else if (type.equals("receiver")) {
                userType = "deleted_from_receiver";
                cUser = "receiver_id";
            }
            
            Query query = em.createNativeQuery("UPDATE messages AS m SET m."+userType+" = 1 "
                    + "WHERE m.id =? AND "
                    + "(SELECT COUNT(c.id) FROM conversations AS c WHERE c.id = m.conversation_id AND c."+cUser+" =?) > 0");
            query.setParameter(1, id);
            query.setParameter(2, user);
            query.executeUpdate();
        } else {
            token = KeyHolder.issueToken(null);
        }
        return token;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Messages find(@HeaderParam("Authorization")String token, @PathParam("id") Integer id) {
        if (KeyHolder.checkToken(token, className)) {
            return super.find(id);
        }
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Messages> findAll(@HeaderParam("Authorization") String token) {
        List<Messages> data = new ArrayList<Messages>();
        if (KeyHolder.checkToken(token, className)) {
            data = super.findAll();
        }
        return data;
    }
    
    @GET
    @Path("conversation/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Messages> findByConversation(@HeaderParam("Authorization") String token, @PathParam("id") Integer id) {
        List<Messages> data = new ArrayList<Messages>();
        if (KeyHolder.checkToken(token, className)) {
            Query query = em.createNamedQuery("findByConversation");
            query.setParameter("conversationId", id);
            data = query.getResultList();
        }
        return data;
    }
    
    private boolean addNewMessage(Integer senderId, Integer conversationId, String msgBody) {
        Query query = em.createNativeQuery("INSERT IGNORE INTO messages (user_id, conversation_id, body) VALUES (?, ?, ?)");
        query.setParameter(1, senderId);
        query.setParameter(2, conversationId);
        query.setParameter(3, msgBody);

        int result = query.executeUpdate();
        if (result > 0) return true;
        return false;
    }
    
    @POST
    @Path("addmessage")
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateConversation(@HeaderParam("Authorization") String token, Messages entity) {
        if (KeyHolder.checkToken(token, className)) {
            int senderId        = entity.getUserId().getId();
            int conversationId  = entity.getConversationId().getId();
            String msgBody      = entity.getBody();
            addNewMessage(senderId, conversationId, msgBody);
            return token;
        }
        return "";
    }
    
    @GET
    @Path("count/{user}")
    @Produces(MediaType.TEXT_PLAIN)
    public String countNewMessages(@HeaderParam("Authorization") String token, @PathParam("user") Integer user) {
        String countMsg = "0";
        if (KeyHolder.checkToken(token, className)) {
            Query query = em.createNativeQuery("SELECT count(*) FROM conversations WHERE "
                    + " (CASE WHEN sender_id =? THEN read_from_sender END) != 1 OR "
                    + " (CASE WHEN receiver_id =? THEN read_from_receiver END) != 1 ");
            query.setParameter(1, user);
            query.setParameter(2, user);

            countMsg = query.getSingleResult().toString();
        }
        return countMsg;
    }
}

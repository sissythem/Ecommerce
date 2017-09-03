package services;

import domain.Conversations;
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
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import utils.KeyHolder;

@Stateless
@Path("conversations")
public class ConversationsFacadeREST extends AbstractFacade<Conversations> {

    @PersistenceContext(unitName = "ecommerce_restPU")
    private EntityManager em;

    public ConversationsFacadeREST() {
        super(Conversations.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Conversations entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Conversations entity) {
        super.edit(entity);
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Conversations> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    private static String className = ConversationsFacadeREST.class.getName();
    
    @DELETE
    @Path("delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String remove(@HeaderParam("Authorization") String token, @PathParam("id")String id) {
        if (KeyHolder.checkToken(token, className)) {
            super.remove(super.find(Integer.parseInt(id)));
            token = KeyHolder.issueToken(null);
        }
        return token;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Conversations find(@HeaderParam("Authorization") String token, @PathParam("id") Integer id) {
        Conversations data = new Conversations();
        if (KeyHolder.checkToken(token, className)) {
            data = super.find(id);
        }
        return data;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Conversations> findAll(@HeaderParam("Authorization") String token)  {
        List<Conversations> data = new ArrayList<Conversations>();
        if (KeyHolder.checkToken(token, className)) {
            data = super.findAll();
        }
        return data;
    }
    
    @POST
    @Path("add")
    @Consumes({MediaType.APPLICATION_JSON})
    public String createConversation(@HeaderParam("Authorization") String token, Conversations entity) {
        if (KeyHolder.checkToken(token, className)) {
            super.create(entity);
            return token;
        }
        return "";
    }
    
    @GET
    @Path("user")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Conversations> conversationByUserId(@HeaderParam("Authorization") String token, @QueryParam("userId") String userId) {
        List<Conversations> data = new ArrayList<Conversations>();
        if (KeyHolder.checkToken(token, className)) {
            Query query = em.createNativeQuery("SELECT * FROM conversations WHERE sender_id=?userId OR receiver_id=?userId", Conversations.class);
            query.setParameter("userId", userId);
            data = query.getResultList();
        }
        return data;
    }
    
    @GET
    @Path("last")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Conversations> lastConversationEntry(@HeaderParam("Authorization") String token, 
            @QueryParam("senderId") Integer senderId, 
            @QueryParam("receiverId") Integer receiverId) {
        
        List<Conversations> data = new ArrayList<Conversations>();
        if (KeyHolder.checkToken(token, className)) {
            Query query = em.createNativeQuery("SELECT * FROM conversations WHERE sender_id=?senderId AND receiver_id=?receiverId ORDER BY id DESC LIMIT 1", Conversations.class);
            query.setParameter("senderId", senderId);
            query.setParameter("receiverId", receiverId);
            data = query.getResultList();
        }
        return data;
    }
    
    @GET
    @Path("residence/{id}/{user}")
    @Produces({MediaType.APPLICATION_JSON})
        public List<Conversations> conversationByResidence(@HeaderParam("Authorization") String token, @PathParam("id") Integer residenceId, @PathParam("user") Integer userId) {
        List<Conversations> data = new ArrayList<Conversations>();
        if (KeyHolder.checkToken(token, className)) {
            Query query = em.createNativeQuery("SELECT * FROM conversations WHERE residence_id =?residenceId AND (sender_id =?senderId OR receiver_id =?receiverId) LIMIT 1", Conversations.class);
            query.setParameter("residenceId", residenceId);
            query.setParameter("senderId", userId);
            query.setParameter("receiverId", userId);
            data = query.getResultList();
        }
        return data;
    }
    
    @POST
    @Path("update_conversation")
    @Produces({MediaType.TEXT_PLAIN})
    public String updateReadConversation(@HeaderParam("Authorization") String token, 
            @QueryParam("read") String isRead, 
            @QueryParam("type") String type, 
            @QueryParam("id") String id) {
        
        if (KeyHolder.checkToken(token, className)) {
            if (isRead != null && type != null && id != null) {
                String userType = "";
                if (type.equals("sender")) {
                    userType = "read_from_sender";
                } else if (type.equals("receiver")) {
                    userType = "read_from_receiver";
                }
            
                if (userType != "") {
                    Query query = em.createNativeQuery("UPDATE conversations SET "+userType+" =? WHERE id =?");
                    query.setParameter(1, isRead);
                    query.setParameter(2, id);
                    query.executeUpdate();
                }
            }
        } else {
            token = KeyHolder.issueToken(null);
        }
        return token;
    }
    
    @POST
    @Path("deletecnv/{id}/{user}/{type}")
    @Produces({MediaType.APPLICATION_JSON})
    public String removeCNV(@HeaderParam("Authorization") String token, @PathParam("id")Integer id, @PathParam("user")Integer user, @PathParam("type")String type) {
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
            
            Query query = em.createNativeQuery("UPDATE conversations SET "+userType+" = 1 WHERE id =? AND "+cUser+" =? ");
            query.setParameter(1, id);
            query.setParameter(2, user);
            query.executeUpdate();
        } else {
            token = KeyHolder.issueToken(null);
        }
        return token;
    }
    
    @POST
    @Path("restore/{id}/{user}/{type}")
    @Produces({MediaType.APPLICATION_JSON})
    public String restoreCNV(@HeaderParam("Authorization") String token, @PathParam("id")Integer id, @PathParam("user")Integer user, @PathParam("type")String type) {
        System.out.println("to restore");
        System.out.println(id);
        System.out.println(user);
        System.out.println(type);
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
            
            Query query = em.createNativeQuery("UPDATE conversations SET "+userType+" = 0 WHERE id =? AND "+cUser+" =? ");
            query.setParameter(1, id);
            query.setParameter(2, user);
            query.executeUpdate();
        } else {
            token = KeyHolder.issueToken(null);
        }
        return token;
    }
}

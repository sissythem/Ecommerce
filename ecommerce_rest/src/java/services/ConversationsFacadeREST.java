/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import domain.Conversations;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import utils.AuthenticationFilter;


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

    @DELETE
    @Path("{id}")
    public void remove(@HeaderParam("Authorization") String token, @PathParam("id") Integer id) {
        try
        {
            AuthenticationFilter.filter(token);
            super.remove(super.find(id));
        }
        catch(Exception ex) 
         {
            Logger.getLogger(UsersFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
         }
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Conversations find(@HeaderParam("Authorization") String token, @PathParam("id") Integer id) {
        try
        {
            AuthenticationFilter.filter(token);
            return super.find(id);
        }
        catch(Exception ex) 
         {
            Logger.getLogger(UsersFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
         }
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Conversations> findAll(@HeaderParam("Authorization") String token) {
        try
        {
            AuthenticationFilter.filter(token);
            List<Conversations> data = super.findAll();
            System.out.println(data);
            return data;
        }
        catch(Exception ex) 
         {
            Logger.getLogger(UsersFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
         }
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
    
    @POST
    @Path("add")
    @Consumes({MediaType.APPLICATION_JSON})
    public String createConversation(@HeaderParam("Authorization") String token, Conversations entity) 
    {
        try
        {
            AuthenticationFilter.filter(token);
            super.create(entity);
            return token;
        }
        catch(Exception ex) 
         {
            Logger.getLogger(UsersFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
         }
    }
    
    @GET
    @Path("user")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Conversations> conversationByUserId(@HeaderParam("Authorization") String token, @QueryParam("userId")String userId) {
         try
        {
            AuthenticationFilter.filter(token);
            Query query = em.createNativeQuery("SELECT * FROM conversations WHERE sender_id=?userId OR receiver_id=?userId", Conversations.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        }
         catch(Exception ex) 
         {
            Logger.getLogger(UsersFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
         }
    }
    
    @GET
    @Path("last")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Conversations> lastConversationEntry(@HeaderParam("Authorization") String token, @QueryParam("senderId")String senderId, @QueryParam("receiverId")String receiverId) {
         try
        {
            AuthenticationFilter.filter(token);
            Query query = em.createNativeQuery("SELECT * FROM conversations WHERE sender_id=?senderId AND receiver_id=?receiverId ORDER BY id DESC LIMIT 1", Conversations.class);
            query.setParameter("senderId", senderId);
            query.setParameter("receiverId", receiverId);
            return query.getResultList();
        }
        catch(Exception ex) 
         {
            Logger.getLogger(UsersFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
         } 
    }
    
    @GET
    @Path("residence/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Conversations> conversationByResidence(@HeaderParam("Authorization") String token, @PathParam("id") String residenceId) {
         try
        {
            AuthenticationFilter.filter(token);
            Query query = em.createNativeQuery("SELECT * FROM conversations WHERE residence_id =?residenceId LIMIT 1", Conversations.class);
            query.setParameter("residenceId", residenceId);
            List<Conversations> result = query.getResultList();
            return result;
        }
        catch(Exception ex) 
         {
            Logger.getLogger(UsersFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
         }  
    }
    
    @GET
    @Path("update_conversation")
    @Produces({MediaType.APPLICATION_JSON})
    public Conversations updateReadConversation(@HeaderParam("Authorization") String token, @QueryParam("read") String isRead, @QueryParam("type") String type, @QueryParam("id") String id) {
        try
        {
            AuthenticationFilter.filter(token);
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
                    System.out.println(query);
                }
            }
            Conversations conv = super.find(Integer.parseInt(id));
            System.out.println(conv);
            return conv;
        }
        catch(Exception ex) 
         {
            Logger.getLogger(UsersFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
         }  
    }
    
    private String issueToken(String username) {
            Key key = utils.KeyHolder.key;
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            long expMillis = nowMillis + 300000L;
            Date exp = new Date(expMillis);
            String jws = Jwts.builder()
                        .setSubject(username)
                        .setIssuedAt(now)
                        .signWith(SignatureAlgorithm.HS512, key)
                        .setExpiration(exp)
                        .compact();
            return jws;
    }
}

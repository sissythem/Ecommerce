/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import domain.Messages;
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

    @DELETE
    @Path("{id}")
    public void remove(@HeaderParam("Authorization")String token, @PathParam("id") Integer id) {
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
    public Messages find(@HeaderParam("Authorization")String token, @PathParam("id") Integer id) {
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
    public List<Messages> findAll(@HeaderParam("Authorization") String token) {
        try
        {
            AuthenticationFilter.filter(token);
            return super.findAll();
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
    @GET
    @Path("conversation")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Messages> findByConversation(@HeaderParam("Authorization") String token, @QueryParam("convId")Integer convId) {
        try
        {
            AuthenticationFilter.filter(token);
            Query query = em.createNamedQuery("findByConversation");
            query.setParameter("convId", convId);
            return query.getResultList();
        }
        catch(Exception ex) 
         {
            Logger.getLogger(UsersFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
         }
    }
    
    public boolean addNewMessage(Integer senderId, Integer conversationId, String msgBody) {
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
        try
        {
            AuthenticationFilter.filter(token);
            int senderId        = entity.getUserId().getId();
            int conversationId  = entity.getConversationId().getId();
            String msgBody      = entity.getBody();
        
            addNewMessage(senderId, conversationId, msgBody);
            return token;
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

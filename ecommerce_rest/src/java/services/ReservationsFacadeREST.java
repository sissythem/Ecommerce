/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import domain.Reservations;
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

/**
 *
 * @author sissy
 */
@Stateless
@Path("reservations")
public class ReservationsFacadeREST extends AbstractFacade<Reservations> {

    @PersistenceContext(unitName = "ecommerce_restPU")
    private EntityManager em;

    public ReservationsFacadeREST() {
        super(Reservations.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Reservations entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Reservations entity) {
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
    public Reservations find(@HeaderParam("Authorization") String token, @PathParam("id") Integer id) {
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
    public List<Reservations> findAll(@HeaderParam("Authorization") String token) {
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
    public List<Reservations> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    
    /* Custom */
    @GET
    @Path("tenant")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reservations> findbyTenants(@HeaderParam("Authorization") String token, @QueryParam("tenantId")Integer tenantId) {
        try
        {
            AuthenticationFilter.filter(token);
            Query query = em.createNamedQuery("Reservations.findbyTenants");
            query.setParameter("tenantId", tenantId);
            return query.getResultList();
        }
        catch(Exception ex) 
         {
            Logger.getLogger(UsersFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
         }
    }
    
    @GET
    @Path("residence")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reservations> findbyResidence(@HeaderParam("Authorization") String token, @QueryParam("residenceId")Integer residenceId) {
        try
        {
            AuthenticationFilter.filter(token);
            Query query = em.createNamedQuery("Reservations.findbyResidence");
            query.setParameter("residenceId", residenceId);
            return query.getResultList();
        }
        catch(Exception ex) 
         {
            Logger.getLogger(UsersFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
         }
    }
    
    @GET
    @Path("comment")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reservations> review(@HeaderParam("Authorization") String token, @QueryParam("tenantId")Integer tenantId, @QueryParam("residenceId")Integer residenceId) {
         try
        {
            AuthenticationFilter.filter(token);
            Query query = em.createNamedQuery("Reservations.comment");
            query.setParameter("tenantId", tenantId);
            query.setParameter("residenceId", residenceId);
            return query.getResultList();
        }
         catch(Exception ex) 
         {
            Logger.getLogger(UsersFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
         }
    }
    
    @POST
    @Path("makereservation")
    @Consumes({MediaType.APPLICATION_JSON})
    public String createReservation(@HeaderParam("Authorization") String token, Reservations entity) {
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

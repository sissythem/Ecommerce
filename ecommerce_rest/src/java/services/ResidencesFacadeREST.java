/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import domain.Residences;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import static javax.faces.component.UIInput.isEmpty;
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
import utils.General;

/**
 *
 * @author sissy
 */
@Stateless
@Path("residences")
public class ResidencesFacadeREST extends AbstractFacade<Residences> {

    @PersistenceContext(unitName = "ecommerce_restPU")
    private EntityManager em;

    public ResidencesFacadeREST() {
        super(Residences.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Residences entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Residences entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("delete/{id}")
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
    public Residences find(@HeaderParam("Authorization") String token, @PathParam("id") Integer id) {
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
    public List<Residences> findAll(@HeaderParam("Authorization") String token) {
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
    public List<Residences> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    
    @GET
    @Path("city")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Residences> findbyCity(@HeaderParam("Authorization") String token, @QueryParam("city")String city) {
        try
        {
            AuthenticationFilter.filter(token);
            Query query = em.createNamedQuery("Residences.findByCity");
            query.setParameter("city", city);
            return query.getResultList();
        }
        catch(Exception ex) 
         {
            Logger.getLogger(UsersFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
         }
    }
    
    @GET
    @Path("host")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Residences> findByHost(@HeaderParam("Authorization") String token, @QueryParam("hostId")Integer hostId) {
        try
        {
            AuthenticationFilter.filter(token);
            Query query = em.createNamedQuery("findByHost");
            query.setParameter("hostId", hostId);
            return query.getResultList();
        }
        catch(Exception ex) 
         {
            Logger.getLogger(UsersFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
         }
    }
    
    public void addUserSearch(Integer userId, String city) {
        Query query = em.createNativeQuery("INSERT IGNORE INTO searches (user_id, city) VALUES (?, ?)");
        query.setParameter(1, userId);
        query.setParameter(2, city);

        query.executeUpdate();
    }
    
    @GET
    @Path("search")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Residences> findRecommendations(
        @HeaderParam("Authorization") String token,
        @QueryParam("userId")String userId, 
        @QueryParam("city")String city,
        @QueryParam("startDate")String startDate,
        @QueryParam("endDate")String endDate,
        @QueryParam("guests") Integer guests
    ) {
        try
        {
            AuthenticationFilter.filter(token);
            Query query;
            List<Residences> results = new ArrayList<>();
        
            /* Date validator from utils.General */
            General dateValidator = new General();

            if (isEmpty(startDate) || !dateValidator.isThisDateValid(startDate, "yyyy-MM-dd")) {
                String currentdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                startDate = currentdate;
            }
            if (isEmpty(endDate) || !dateValidator.isThisDateValid(endDate, "yyyy-MM-dd")) {
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DATE, +7);
                endDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
            }
        
            String querystring = "SELECT * FROM residences as res"
            + " WHERE '"+startDate+"' >= res.available_date_start AND '"+endDate+"' <= res.available_date_end AND res.rooms > 0"
            + " AND res.guests > 0";

         if (isEmpty(guests)) guests = 1;
         querystring += " AND (res.guests - (SELECT count(guests) FROM reservations as r "
                + "WHERE r.residence_id = res.id AND r.start_date < '"+endDate+"' AND r.end_date > '"+startDate+"'"
                + ")) >= " + guests;
        
            if (!isEmpty(userId)) {            
            querystring += " AND res.host_id != " + userId;
            
                if (!isEmpty(city)) {
                    city = city.toLowerCase();
                    addUserSearch(Integer.parseInt(userId), city);
                    querystring += " AND res.city = '" + city + "'";
                }
            }
        
            querystring += " order by res.min_price ASC";
        
            System.out.println(querystring);
            query = em.createNativeQuery(querystring, Residences.class);

            return query.getResultList();
        }
        catch(Exception ex) 
         {
            Logger.getLogger(UsersFacadeREST.class.getName()).log(Level.SEVERE, null, ex);
            return null;
         }
    }
    
    @POST
    @Path("add")
    @Consumes({MediaType.APPLICATION_JSON})
    public String createResidence(Residences entity) 
    {
        super.create(entity);
        String token = issueToken(entity.getHostId().getUsername());
        return token;
    }
    
    @PUT
    @Path("put")
    @Consumes({MediaType.APPLICATION_JSON})
    public String editResidence(@HeaderParam("Authorization") String token, @PathParam("id") Integer id, Residences entity) {
        try
        {
            AuthenticationFilter.filter(token);
            super.edit(entity);
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

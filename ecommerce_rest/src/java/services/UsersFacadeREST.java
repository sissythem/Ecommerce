/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import domain.Users;
import java.security.Key;
import java.util.Date;
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

/**
 *
 * @author sissy
 */
@Stateless
@Path("users")
public class UsersFacadeREST extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "ecommerce_restPU")
    private EntityManager em;

    public UsersFacadeREST() {
        super(Users.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Users entity) {
        super.create(entity);
        
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Users entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("delete")
    public void remove(Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Users find(@HeaderParam("Authorization") String token, @PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Users> findAll(@HeaderParam("Authorization") String token) {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Users> findRange(@HeaderParam("Authorization") String token, @PathParam("from") Integer from, @PathParam("to") Integer to) {
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
   
    
    /**
     *
     * @param username
     * @param password
     * @return
     */
    
    @POST
    @Path("register")
    @Consumes({MediaType.APPLICATION_JSON})
    public String createUser(Users entity) {
        super.create(entity);
        String token = issueToken(entity.getUsername());
        return token;
    }
    
    @PUT
    @Path("put")
    @Consumes({MediaType.APPLICATION_JSON})
    public String editUser(@PathParam("id") Integer id, Users entity) {
        super.edit(entity);
        String token = issueToken(entity.getUsername());
        return token;
    }
    
    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public String login(@QueryParam("username")String username, @QueryParam("password")String password) {
        Query query = em.createNamedQuery("loginUser");
        query.setParameter("username", username);
        query.setParameter("password", password);
        List<Users> isUser = query.getResultList();
        if(isUser.isEmpty()){
            return "not";
        }
        else{
            String token = issueToken(isUser.get(0).getUsername());
            return token;
        }
        
    }
    
    @GET
    @Path("username")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findbyUsername(@HeaderParam("Authorization") String token, @QueryParam("username")String username) {
        Query query = em.createNamedQuery("Users.findByUsername");
        query.setParameter("username", username);
        return query.getResultList();
    }
    
    @GET
    @Path("email")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findbyEmail(@HeaderParam("Authorization") String token, @QueryParam("email")String email) {
        Query query = em.createNamedQuery("Users.findByEmail");
        query.setParameter("email", email);
        return query.getResultList();
    }

    private String issueToken(String username) 
    {
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

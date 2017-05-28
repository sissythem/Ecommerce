/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dbpackage.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
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

    @PersistenceContext(unitName = "ecommerce_restfulPU")
    private EntityManager em;

    public UsersFacadeREST() {
        super(Users.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void create(Users entity) {
        super.create(entity);
    }

    @PUT
    @Path("put")
    @Consumes({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public void edit(Users entity) 
    {
        super.edit(entity);
    }

    @DELETE
    @Path("delete/{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Users find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Users> findAll() {
        return super.findAll();
    }
    
    @GET
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> login(@QueryParam("username")String username, @QueryParam("password")String password) {
        //http://192.168.2.2:8080/ecommerce/webresources/domain.users/login?username=vasso&password=124ddd
        
        Query query = em.createNamedQuery("loginUser");
        query.setParameter("username", username);
        query.setParameter("password", password);
        return query.getResultList();
    }
    
    @GET
    @Path("username")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findbyUsername(@QueryParam("username")String username) {
        //http://192.168.2.2:8080/ecommerce/webresources/users/username?username=sissy
        
        Query query = em.createNamedQuery("Users.findByUsername");
        query.setParameter("username", username);
        return query.getResultList();
    }
    
    @GET
    @Path("email")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findbyEmail(@QueryParam("email")String email) {
        //http://192.168.2.2:8080/ecommerce/webresources/users/email?email=sissy@email.com
        
        Query query = em.createNamedQuery("Users.findByEmail");
        query.setParameter("email", email);
        return query.getResultList();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Users> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    
}

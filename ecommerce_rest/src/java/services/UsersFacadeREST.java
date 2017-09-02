package services;

import domain.Users;
import java.util.ArrayList;
import java.util.List;
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
import utils.KeyHolder;

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
    private static String className = UsersFacadeREST.class.getName();
    
    @PUT
    @Path("put/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public String editUser(@HeaderParam("Authorization") String token, @PathParam("id") Integer id, Users entity) {
        if (KeyHolder.checkToken(token, className)) {
            super.edit(entity);
            
            /** Update token and restart session **/
            token = KeyHolder.issueToken(entity.getUsername());
            return token;
        }
        return "not";
    }
    
    @DELETE
    @Path("delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String remove(@HeaderParam("Authorization") String token, @PathParam("id")String id) {
        if (KeyHolder.checkToken(token, className)) {
            System.out.println("deleteinner");
            super.remove(super.find(Integer.parseInt(id)));
            token = KeyHolder.issueToken(null);
        }
        System.out.println("Deleted user using token " + token);
        return token;
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Users find(@HeaderParam("Authorization") String token, @PathParam("id") Integer id) {
        if (KeyHolder.checkToken(token, className)) {
            return super.find(id);
        }
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Users> findAll(@HeaderParam("Authorization") String token) {
        List<Users> data = new ArrayList<Users>();
        if (KeyHolder.checkToken(token, className)) {
            data = super.findAll();
        }
        return data;
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Users> findRange(@HeaderParam("Authorization") String token, @PathParam("from") Integer from, @PathParam("to") Integer to) {
        List<Users> data = new ArrayList<Users>();
        if (KeyHolder.checkToken(token, className)) {
            data = super.findRange(new int[]{from, to});
        }
        return data;
    }
    
    @POST
    @Path("register")
    @Consumes({MediaType.APPLICATION_JSON})
    //@Produces(MediaType.TEXT_PLAIN)
    public String createUser(Users entity) 
    {
        System.out.println(entity);
        List<Users> UsernameList = new ArrayList<>();
        List<Users> EmailList = new ArrayList<>();
        Query query = em.createNamedQuery("Users.findByUsername");
        query.setParameter("username", entity.getUsername());
        UsernameList = query.getResultList();
        
        Query query2 = em.createNamedQuery("Users.findByEmail");
        query2.setParameter("email", entity.getEmail());
        EmailList = query2.getResultList();
        
        if(UsernameList.size()!=0) return "username exists";
        if(EmailList.size()!=0) return "email exists";
        
        if(UsernameList.size()==0 && EmailList.size()==0) {
            super.create(entity);
            String token = KeyHolder.issueToken(entity.getUsername());
            return token;
        }
        else 
            return "error";
    }
    
    @GET
    @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    public String login(@QueryParam("username")String username, @QueryParam("password")String password)
    {
        Query query = em.createNamedQuery("loginUser");
        query.setParameter("username", username);
        query.setParameter("password", password);
        List<Users> isUser = query.getResultList();
        if(!isUser.isEmpty()){
            String token = KeyHolder.issueToken(isUser.get(0).getUsername());
            System.out.println("Logged in user " + username + ", issued token " + token);
            return token;
        }
        return "not";
    }
    
    @GET
    @Path("username")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findbyUsername(@HeaderParam("Authorization") String token, @QueryParam("username")String username) {
        List<Users> data = new ArrayList<Users>();
        if (KeyHolder.checkToken(token, className)) {
            Query query = em.createNamedQuery("Users.findByUsername");
            query.setParameter("username", username);
            data = query.getResultList();
        }
        return data;
    }
    
    @GET
    @Path("email")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Users> findbyEmail(@HeaderParam("Authorization") String token, @QueryParam("email")String email) {
        List<Users> data = new ArrayList<Users>();
        if (KeyHolder.checkToken(token, className)) {
            Query query = em.createNamedQuery("Users.findByEmail");
            query.setParameter("email", email);
            data = query.getResultList();
        }
        return data;
    }
    
    @GET
    @Path("checktoken")
    @Produces(MediaType.TEXT_PLAIN)
    public boolean isTokenExpired(@HeaderParam("Authorization")String token)
    {
        Logger.getAnonymousLogger().info("users/checktoken token: " + token);

        if(!KeyHolder.checkToken(token, className))
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
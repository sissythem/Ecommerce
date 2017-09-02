package services;

import domain.Searches;
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
@Path("searches")
public class SearchesFacadeREST extends AbstractFacade<Searches> {

    @PersistenceContext(unitName = "ecommerce_restPU")
    private EntityManager em;

    public SearchesFacadeREST() {
        super(Searches.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Searches entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Searches entity) {
        super.edit(entity);
    }
    
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Searches> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    private static String className = SearchesFacadeREST.class.getName();
    
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
    public Searches find(@HeaderParam("Authorization") String token, @PathParam("id") Integer id) {
        if (KeyHolder.checkToken(token, className)) {
            return super.find(id);
        }
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Searches> findAll(@HeaderParam("Authorization") String token) {
        List<Searches> data = new ArrayList<Searches>();
        if (KeyHolder.checkToken(token, className)) {
            data = super.findAll();
        }
        return data;
    }
    
    @GET
    @Path("city")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Searches> findbyUserId(@HeaderParam("Authorization") String token, @QueryParam("userId")Integer userId) {
        
        List<Searches> data = new ArrayList<Searches>();
        if (KeyHolder.checkToken(token, className)) {
            Query query = em.createNamedQuery("Searches.findByUserId");
            query.setParameter("userId", userId);
            data = query.getResultList();
        }
        return data;
    }
}

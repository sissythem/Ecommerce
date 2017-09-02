package services;

import domain.Reviews;
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
@Path("reviews")
public class ReviewsFacadeREST extends AbstractFacade<Reviews> {

    @PersistenceContext(unitName = "ecommerce_restPU")
    private EntityManager em;

    public ReviewsFacadeREST() {
        super(Reviews.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Reviews entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Reviews entity) {
        super.edit(entity);
    }
    
    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Reviews> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    private static String className = ReviewsFacadeREST.class.getName();

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

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Reviews find(@HeaderParam("Authorization") String token, @PathParam("id") Integer id) {
        if (KeyHolder.checkToken(token, className)) {
            return super.find(id);
        }
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Reviews> findAll(@HeaderParam("Authorization") String token) {
        List<Reviews> data = new ArrayList<Reviews>();
        if (KeyHolder.checkToken(token, className)) {
            data = super.findAll();
        }
        return data;
    }
    
    @POST
    @Path("postreview")
    @Consumes({MediaType.APPLICATION_JSON})
    public String createReview(@HeaderParam("Authorization") String token, Reviews entity) {
        if (KeyHolder.checkToken(token, className)) {
            super.create(entity);
            return token;
        }
        return "not";
    }
    
    @GET
    @Path("residence")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reviews> findbyResidence(@HeaderParam("Authorization") String token, @QueryParam("residenceId")Integer residenceId) {
        List<Reviews> data = new ArrayList<Reviews>();
        if (KeyHolder.checkToken(token, className)) {
            Query query = em.createNamedQuery("Reviews.findbyResidence");
            query.setParameter("residenceId", residenceId);
            data = query.getResultList();
        }
        return data;
    }
    
    @GET
    @Path("tenant")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reviews> findbyTenant(@HeaderParam("Authorization") String token, @QueryParam("tenantId")Integer tenantId) {
        List<Reviews> data = new ArrayList<Reviews>();
        if (KeyHolder.checkToken(token, className)) {
            Query query = em.createNamedQuery("Reviews.findbyTenant");
            query.setParameter("tenantId", tenantId);
            data = query.getResultList();
        }
        return data;
    }
}
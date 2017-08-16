package services;

import domain.Reservations;
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
    
    /*** CUSTOM METHODS ***/
    
    private static String className = ReservationsFacadeREST.class.getName();
    
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
    public Reservations find(@HeaderParam("Authorization") String token, @PathParam("id") Integer id) {
        if (KeyHolder.checkToken(token, className)) {
            return super.find(id);
        }
        return null;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Reservations> findAll(@HeaderParam("Authorization") String token) {
        List<Reservations> data = new ArrayList<Reservations>();
        if (KeyHolder.checkToken(token, className)) {
            data = super.findAll();
        }
        return data;
    }
    
    @GET
    @Path("tenant")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reservations> findbyTenants(@HeaderParam("Authorization") String token, @QueryParam("tenantId")Integer tenantId) {
        List<Reservations> data = new ArrayList<Reservations>();
        if (KeyHolder.checkToken(token, className)) {
            Query query = em.createNamedQuery("Reservations.findbyTenants");
            query.setParameter("tenantId", tenantId);
            data = query.getResultList();
        }
        return data;
    }
    
    @GET
    @Path("residence")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reservations> findbyResidence(@HeaderParam("Authorization") String token, @QueryParam("residenceId")Integer residenceId) {
        List<Reservations> data = new ArrayList<Reservations>();
        if (KeyHolder.checkToken(token, className)) {
            Query query = em.createNamedQuery("Reservations.findbyResidence");
            query.setParameter("residenceId", residenceId);
            data = query.getResultList();
        }
        return data;
    }
    
    @GET
    @Path("comment")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reservations> review(@HeaderParam("Authorization") String token, @QueryParam("tenantId")Integer tenantId, @QueryParam("residenceId")Integer residenceId) {
        List<Reservations> data = new ArrayList<Reservations>();
        if (KeyHolder.checkToken(token, className)) {
            Query query = em.createNamedQuery("Reservations.comment");
            query.setParameter("tenantId", tenantId);
            query.setParameter("residenceId", residenceId);
            data = query.getResultList();
        } 
        return data;
    }
    
    @POST
    @Path("makereservation")
    @Consumes({MediaType.APPLICATION_JSON})
    public String createReservation(@HeaderParam("Authorization") String token, Reservations entity) {
        if (KeyHolder.checkToken(token, className)) {
            super.create(entity);
            return token;
        }
        return "";
    }
}
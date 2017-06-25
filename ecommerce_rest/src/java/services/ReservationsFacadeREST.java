/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import domain.Reservations;
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
 * @author vasso
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
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Reservations find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Reservations> findAll() {
        List<Reservations>  rrr = super.findAll();
        return rrr;
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
    public List<Reservations> findbyTenants(@QueryParam("tenantId")Integer tenantId) {
        Query query = em.createNamedQuery("Reservations.findbyTenants");
        query.setParameter("tenantId", tenantId);
        return query.getResultList();
    }
    
    @GET
    @Path("residence")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reservations> findbyResidence(@QueryParam("residenceId")Integer residenceId) {
        Query query = em.createNamedQuery("Reservations.findbyResidence");
        query.setParameter("residenceId", residenceId);
        return query.getResultList();
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dbpackage.Residences;
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
import javax.ws.rs.core.Response;

/**
 *
 * @author sissy
 */
@Stateless
@Path("residences")
public class ResidencesFacadeREST extends AbstractFacade<Residences> {

    @PersistenceContext(unitName = "ecommerce_restfulPU")
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
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Residences find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Residences> findAll() {
        return super.findAll();
    }
    
    @GET
    @Path("maxId/{hostId}")
    @Produces({MediaType.APPLICATION_JSON})
    public Residences maxResidenceId(@PathParam("hostId") Integer hostId){
        List<Residences> residences = findByHostId(hostId);
        int maxId=0;
        for(int i=0; i<residences.size();i++){
            if(residences.get(i).getId()> maxId){
                maxId=residences.get(i).getId();
            }
        }
        
        return super.find(maxId);
    }
    
    @GET
    @Path("city")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Residences> findbyCity(@QueryParam("city")String city) {
        //http://192.168.1.6:8080/ecommerce_restful/webresources/residences/city?city="Athens"
        
        Query query = em.createNamedQuery("Residences.findByCity");
        query.setParameter("city", city);
        return query.getResultList();
    }
    
    @GET
    @Path("hostId")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Residences> findByHostId(@QueryParam("hostId")Integer hostId) {
        //http://192.168.1.6:8080/ecommerce_restful/webresources/residences/city?city="Athens"
        
        Query query = em.createNamedQuery("Residences.findByHostId");
        query.setParameter("hostId", hostId);
        return query.getResultList();
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
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import domain.Reviews;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
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

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Reviews find(@HeaderParam("Authorization") String token, @PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Reviews> findAll(@HeaderParam("Authorization") String token) {
        return super.findAll();
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
    
    /* Custom */
    @POST
    @Path("postreview")
    @Consumes({MediaType.APPLICATION_JSON})
    public String createReview(@HeaderParam("Authorization") String token, Reviews entity) {
        super.create(entity);
        String newToken=issueToken(entity.getTenantId().getUsername());
        return token;
    }
    
    @GET
    @Path("residence")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reviews> findbyResidence(@HeaderParam("Authorization") String token, @QueryParam("residenceId")Integer residenceId) {
        Query query = em.createNamedQuery("Reviews.findbyResidence");
        query.setParameter("residenceId", residenceId);
        return query.getResultList();
    }
    
    @GET
    @Path("tenant")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Reviews> findbyTenant(@HeaderParam("Authorization") String token, @QueryParam("tenantId")Integer tenantId) {
        Query query = em.createNamedQuery("Reviews.findbyTenant");
        query.setParameter("tenantId", tenantId);
        return query.getResultList();
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

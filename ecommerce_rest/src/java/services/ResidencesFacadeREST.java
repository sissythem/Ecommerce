package services;

import domain.Residences;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
import utils.KeyHolder;

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
    
    /*** CUSTOM METHODS ***/
    private static String className = ResidencesFacadeREST.class.getName();
    
    @PUT
    @Path("put/{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public String editResidence(@HeaderParam("Authorization") String token, @PathParam("id") Integer id, Residences residence) {
        if (KeyHolder.checkToken(token, className)) {
            super.edit(residence);
            return token;
        }
        return "not";
    }  
    
    @DELETE
    @Path("delete/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public String remove(@HeaderParam("Authorization") String token, @PathParam("id")String id) {
        if (KeyHolder.checkToken(token, className)) {
            Residences res = new Residences();
            Query query = em.createNativeQuery("SELECT * FROM residences WHERE id =?id", Residences.class);
            query.setParameter("id", id);
            List<Residences> residence = query.getResultList();
            String imgName = residence.get(0).getPhotos();
            
            try {
                File file = new File("C:\\Users\\vasso\\Documents\\ecommerce\\images\\" + imgName);
                if (file.exists()) {
                    file.delete();
                }
            } catch (Exception ex) {
                System.out.println("Could not delete residence image");
            }
            
            super.remove(super.find(Integer.parseInt(id)));
            token = KeyHolder.issueToken(null);
        }
        return token;
    }
    
    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Residences find(@HeaderParam("Authorization") String token, @PathParam("id") Integer id) {
        if (KeyHolder.checkToken(token, className)) {
            return super.find(id);
        }
        return null;
    }
    
    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Residences> findAll(@HeaderParam("Authorization") String token) {
        List<Residences> data = new ArrayList<Residences>();
        if (KeyHolder.checkToken(token, className)) {
            data = super.findAll();
        }
        return data;
    }
    
    @GET
    @Path("city")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Residences> findbyCity(@HeaderParam("Authorization") String token, @QueryParam("city")String city) {
        List<Residences> data = new ArrayList<Residences>();
        if (KeyHolder.checkToken(token, className)) {
            Query query = em.createNamedQuery("Residences.findByCity");
            query.setParameter("city", city);
            data = query.getResultList();
        }
        return data;
    }
    
    @GET
    @Path("host")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Residences> findByHost(@HeaderParam("Authorization") String token, @QueryParam("hostId")Integer hostId) {
        List<Residences> data = new ArrayList<Residences>();
        if (KeyHolder.checkToken(token, className)) {
            Query query = em.createNamedQuery("findByHost");
            query.setParameter("hostId", hostId);
            data = query.getResultList();
        }
        return data;
    }
    
    private void addUserSearch(String username, String city) {
        Query query = em.createNativeQuery("INSERT IGNORE INTO searches (user_id, city) VALUES ((SELECT u.id from users as u where u.username = ?), ?)");
        query.setParameter(1, username);
        query.setParameter(2, city);
        query.executeUpdate();
    }
    
    @GET
    @Path("search")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Residences> findRecommendations(
        @HeaderParam("Authorization") String token,
        @QueryParam("username")String username, 
        @QueryParam("city")String city,
        @QueryParam("startDate")long startDate,
        @QueryParam("endDate")long endDate,
        @QueryParam("guests") Integer guests
    ) { 
        List<Residences> data = new ArrayList<Residences>();
        if (KeyHolder.checkToken(token, className)) {
            Query query;
            List<Residences> results = new ArrayList<>();
            
            /* Date validator from utils.General */
            if (isEmpty(startDate) || startDate <= 0) {
                startDate = getCurrentTimestamp(null);
            }
            if (isEmpty(endDate) || endDate <= 0) {
                endDate = getCurrentTimestamp(7);
            }

            String querystring = "SELECT * FROM residences as res"
            + " WHERE '"+startDate+"' >= res.available_date_start AND '"+endDate+"' <= res.available_date_end AND res.rooms > 0"
            + " AND res.guests > 0";

            if (isEmpty(guests)) guests = 1;
            querystring += " AND (res.guests - (SELECT count(guests) FROM reservations as r "
                + "WHERE r.residence_id = res.id AND r.start_date < '"+endDate+"' AND r.end_date > '"+startDate+"'"
                + ")) >= " + guests;
        
            if (!isEmpty(username)) {
                querystring += " AND res.host_id != (SELECT u.id from users as u where u.username = '"+username+"')";
                
                if (!isEmpty(city)) {
                    city = city.toLowerCase();
                    addUserSearch(username, city);
                    querystring += " AND res.city = '" + city + "'";
                }
            }
        
            querystring += " order by res.min_price ASC";
            query = em.createNativeQuery(querystring, Residences.class);
            data = query.getResultList();
        }
        return data;
    }
    
    @POST
    @Path("add")
    @Consumes({MediaType.APPLICATION_JSON})
    public String createResidence(@HeaderParam("Authorization") String token, Residences entity) {
        if (KeyHolder.checkToken(token, className)) {
            super.create(entity);
            return token;
        }
        return "not";
    }
    
    private long getCurrentTimestamp(Integer plus) {
        String DateFormat = "yyyy-MM-dd";
        long date = 0;
        String currentdate = "";
        
        if (plus != null) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, + plus);
            date = cal.getTimeInMillis();
//            currentdate = new SimpleDateFormat(DateFormat).format(cal.getTime());
        } else {
            date = new Date().getTime();
//            currentdate = new SimpleDateFormat(DateFormat).format(new Date());
        }
        System.out.println(date);
        return date;
    }
    
    @PUT
    @Path("main/{id}/{name}")
    @Produces(MediaType.TEXT_PLAIN)
    public String setMainResidencePhoto(@HeaderParam("Authorization") String token, @PathParam("id")Integer id, @PathParam("name")String name) {
        if (KeyHolder.checkToken(token, className)) {
            try {                
                Query query = em.createNativeQuery("UPDATE residences SET photos ='"+name+"' WHERE id ="+id);
                query.executeUpdate();
                return token;
            } catch (Exception e) {
                return "not";
            }
        }
        return "not";
    }
    
}
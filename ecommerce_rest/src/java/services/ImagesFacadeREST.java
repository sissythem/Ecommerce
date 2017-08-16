package services;

import domain.Images;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import javax.activation.MimetypesFileTypeMap;
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
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import utils.KeyHolder;

@Stateless
@Path("images")
public class ImagesFacadeREST extends AbstractFacade<Images> {

    @PersistenceContext(unitName = "ecommerce_restPU")
    private EntityManager em;

    public ImagesFacadeREST() {
        super(Images.class);
    }

    @POST
    @Override
    @Consumes({MediaType.APPLICATION_JSON})
    public void create(Images entity) {
        super.create(entity);
    }

    @PUT
    @Path("{id}")
    @Consumes({MediaType.APPLICATION_JSON})
    public void edit(@PathParam("id") Integer id, Images entity) {
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
    public Images find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Override
    @Produces({MediaType.APPLICATION_JSON})
    public List<Images> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({MediaType.APPLICATION_JSON})
    public List<Images> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
    private static String className = ImagesFacadeREST.class.getName();
    
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
    
    @POST
    @Path("profilepic/{id}")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces(MediaType.TEXT_PLAIN)
    public String uploadUserImage(@HeaderParam("Authorization")String token,
            @PathParam("id")Integer id,
            @FormDataParam("picture") InputStream uploadedInputStream, @FormDataParam("picture") FormDataContentDisposition fileDetail) {
        
        if (KeyHolder.checkToken(token, className)) {
            try {
                File directory = new File("/home/sissy/Documents/Professional/University/UOA/graduate/Semester2/Ecommerce/Project/ecommerce_rest/images/");
                File newFile = File.createTempFile("img", ".jpg", directory);
                saveToFile(uploadedInputStream, newFile);
                Query query = em.createNativeQuery("UPDATE users SET photo ='"+newFile.getName()+"' WHERE id ="+id);
                query.executeUpdate();
                return token;
            } catch (Exception e) {
                return "not";
            }
        }
        return "not";
    }
    
    @POST
    @Path("residence/{id}")
    @Consumes({MediaType.MULTIPART_FORM_DATA})
    @Produces(MediaType.TEXT_PLAIN)
    public String uploadResidenceImage(@HeaderParam("Authorization")String token,
            @PathParam("id")Integer id,
            @FormDataParam("picture") InputStream uploadedInputStream, @FormDataParam("picture") FormDataContentDisposition fileDetail) {
        
        if (KeyHolder.checkToken(token, className)) {
            try {
                File directory = new File("/home/sissy/Documents/Professional/University/UOA/graduate/Semester2/Ecommerce/Project/ecommerce_rest/images/");
                File newFile = File.createTempFile("img", ".jpg", directory);
                saveToFile(uploadedInputStream, newFile);
                Query query = em.createNativeQuery("UPDATE residences SET photos ='"+newFile.getName()+"' WHERE id ="+id);
                query.executeUpdate();
                return token;
            } catch (Exception e) {
                return "not";
            }
        }
        return "not";
    }

    /**
    * Utility method to save InputStream data to target location/file
    * @param inStream - InputStream to be saved
    * @param target - full path to destination file
    */
    private void saveToFile(InputStream inStream, File target) throws IOException {
        java.nio.file.Path path = target.toPath();
        System.out.println(path);
        Files.copy(inStream, path, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
    }
    
    @GET
    @Path("img/{name}")
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces("image/*")
    public Response getUserImage(@HeaderParam("Authorization")String token, @PathParam("name")String name) {
        File f = new File("/home/sissy/Documents/Professional/University/UOA/graduate/Semester2/Ecommerce/Project/ecommerce_rest/images/" + name);
        if (!f.exists()) {
            throw new WebApplicationException(404);
        }
        String mt = new MimetypesFileTypeMap().getContentType(f);
        System.out.println(Response.ok(f, mt).build());
        return Response.ok(f, mt).build();
    }
}
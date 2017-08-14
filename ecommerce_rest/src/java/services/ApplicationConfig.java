package services;

import java.util.Set;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(MultiPartFeature.class);
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(services.ConversationsFacadeREST.class);
        resources.add(services.ImagesFacadeREST.class);
        resources.add(services.MessagesFacadeREST.class);
        resources.add(services.ReservationsFacadeREST.class);
        resources.add(services.ResidencesFacadeREST.class);
        resources.add(services.ReviewsFacadeREST.class);
        resources.add(services.SearchesFacadeREST.class);
        resources.add(services.UsersFacadeREST.class);
    }
}

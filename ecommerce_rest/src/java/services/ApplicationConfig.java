/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author vasso
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
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
        resources.add(services.MessagesFacadeREST.class);
        resources.add(services.ReservationsFacadeREST.class);
        resources.add(services.ResidencesFacadeREST.class);
        resources.add(services.ReviewsFacadeREST.class);
        resources.add(services.SearchesFacadeREST.class);
        resources.add(services.UsersFacadeREST.class);
    }
    
}

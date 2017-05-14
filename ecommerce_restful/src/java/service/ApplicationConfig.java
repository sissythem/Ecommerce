/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author sissy
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
        resources.add(service.ConversationsFacadeREST.class);
        resources.add(service.MessagesFacadeREST.class);
        resources.add(service.ReservationsFacadeREST.class);
        resources.add(service.ResidencesFacadeREST.class);
        resources.add(service.ReviewsFacadeREST.class);
        resources.add(service.RoomsFacadeREST.class);
        resources.add(service.SearchesFacadeREST.class);
        resources.add(service.UsersFacadeREST.class);
    }
    
}

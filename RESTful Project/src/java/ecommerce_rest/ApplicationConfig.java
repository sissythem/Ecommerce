/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ecommerce_rest;

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
        resources.add(ecommerce_rest.ConversationsFacadeREST.class);
        resources.add(ecommerce_rest.MessagesFacadeREST.class);
        resources.add(ecommerce_rest.ReservationsFacadeREST.class);
        resources.add(ecommerce_rest.ResidencesFacadeREST.class);
        resources.add(ecommerce_rest.ReviewsFacadeREST.class);
        resources.add(ecommerce_rest.RoomsFacadeREST.class);
        resources.add(ecommerce_rest.UsersFacadeREST.class);
    }
    
}

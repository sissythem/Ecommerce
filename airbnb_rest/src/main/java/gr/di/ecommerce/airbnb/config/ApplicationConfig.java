package gr.di.ecommerce.airbnb.config;

import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.ws.rs.core.Application;
import java.util.Set;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "gr.di.ecommerce.airbnb")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(MultiPartFeature.class);
        return resources;
    }

}

package gr.di.ecommerce.airbnb.controllers;

import gr.di.ecommerce.airbnb.entities.Conversations;
import gr.di.ecommerce.airbnb.services.ConversationsService;
import gr.di.ecommerce.airbnb.utils.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/conversations/")
public class ConversationsController {

    private static String className = ConversationsController.class.getSimpleName();
    @Autowired
    private ConversationsService conversationsService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @Consumes({MediaType.APPLICATION_JSON})
    public String createConversation(@HeaderParam("Authorization") String token, Conversations entity) {
        if (KeyHolder.checkToken(token, className)) {
            conversationsService.createConversation(entity);
            return token;
        }
        return "";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @Produces({MediaType.APPLICATION_JSON})
    public String remove(@HeaderParam("Authorization") String token, @PathParam("id") String id) {
        if (KeyHolder.checkToken(token, className)) {
            conversationsService.removeConversation(Integer.parseInt(id));
            token = KeyHolder.issueToken(null);
        }
        return token;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @Produces({MediaType.APPLICATION_JSON})
    public Conversations find(@HeaderParam("Authorization") String token, @PathParam("id") Integer id) {
        Conversations data = new Conversations();
        if (KeyHolder.checkToken(token, className)) {
            data = conversationsService.getConversation(id);
        }
        return data;
    }

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public List<Conversations> findAll(@HeaderParam("Authorization") String token)  {
        List<Conversations> data = new ArrayList<>();
        if (KeyHolder.checkToken(token, className)) {
            data = conversationsService.getAllConversations();
        }
        return data;
    }
}

package gr.di.ecommerce.airbnb.controllers;

import gr.di.ecommerce.airbnb.entities.Conversations;
import gr.di.ecommerce.airbnb.services.ConversationsService;
import gr.di.ecommerce.airbnb.utils.Constants;
import gr.di.ecommerce.airbnb.utils.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/conversations/")
public class ConversationsController {

    private static String className = ConversationsController.class.getSimpleName();

    @Autowired
    private ConversationsService conversationsService;

    @RequestMapping(value = "add", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String createConversation(@RequestHeader(Constants.AUTHORIZATION) String token, Conversations entity) {
        if (KeyHolder.checkToken(token, className)) {
            conversationsService.createConversation(entity);
            return token;
        }
        return "";
    }

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String remove(@RequestHeader(Constants.AUTHORIZATION) String token, @PathVariable("id") String id) {
        if (KeyHolder.checkToken(token, className)) {
            conversationsService.removeConversation(Integer.parseInt(id));
            token = KeyHolder.issueToken(null);
        }
        return token;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Conversations find(@RequestHeader(Constants.AUTHORIZATION) String token, @PathVariable("id") Integer id) {
        if (KeyHolder.checkToken(token, className)) {
            return conversationsService.getConversation(id);
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Conversations> findAll(@RequestHeader(Constants.AUTHORIZATION) String token)  {
        if (KeyHolder.checkToken(token, className)) {
            return conversationsService.getAllConversations();
        }
        return null;
    }

    @RequestMapping(value = "user", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Conversations> getConversationsByUser(@RequestHeader(Constants.AUTHORIZATION) String token,
                                                                    @RequestParam("userId") String userId) {
        if (KeyHolder.checkToken(token, className)) {
            return conversationsService.getConversationsByUserId(Integer.parseInt(userId));
        }
        return null;
    }

    @RequestMapping(value = "last", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Conversations lastConversationEntry(@RequestHeader(Constants.AUTHORIZATION) String token,
                                                             @RequestParam("senderId") Integer senderId,
                                                             @RequestParam("receiverId") Integer receiverId) {
        if (KeyHolder.checkToken(token, className)) {
            return conversationsService.getLastConversation(senderId, receiverId);
        }
        return null;
    }

    @RequestMapping(value = "residence/{id}/{user}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Conversations> getConversationsByResidence(@RequestHeader(Constants.AUTHORIZATION) String token,
                                                                         @PathVariable("residenceId") Integer residenceId,
                                                                         @PathVariable("usedId") Integer userId) {
        if (KeyHolder.checkToken(token, className)) {
            return conversationsService.getConversationsByResidence(residenceId, userId);
        }
        return null;
    }

    @RequestMapping(value = "update_conversation", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String updateReadConversation(@RequestHeader(Constants.AUTHORIZATION) String token, @RequestParam("read") String isRead,
                                                       @RequestParam("type") String type, @RequestParam("id") String id) {
        if (KeyHolder.checkToken(token, className)) {
            conversationsService.updateReadConversation(isRead, type, Integer.parseInt(id));
        } else {
            token = KeyHolder.issueToken(null);
        }
        return token;
    }

    @RequestMapping(value = "deletecnv/{id}/{user}/{type}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String removeConversation(@RequestHeader(Constants.AUTHORIZATION) String token, @PathVariable("id") Integer id,
                                                   @PathVariable("user") Integer userId, @PathVariable("type") String type) {
        if (KeyHolder.checkToken(token, className)) {
            conversationsService.deleteConversation(id, userId, type);
        } else {
            token = KeyHolder.issueToken(null);
        }
        return token;
    }

    @RequestMapping(value = "restore/{id}/{user}/{type}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String restoreConversation(@RequestHeader(Constants.AUTHORIZATION) String token, @PathVariable("id") Integer id,
                                                    @PathVariable("user") Integer userId, @PathVariable("type") String type) {
        if (KeyHolder.checkToken(token, className)) {
            conversationsService.restoreConversation(id, userId, type);
        } else {
            token = KeyHolder.issueToken(null);
        }
        return token;
    }
}

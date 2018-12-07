package gr.di.ecommerce.airbnb.controllers;

import gr.di.ecommerce.airbnb.entities.Messages;
import gr.di.ecommerce.airbnb.services.MessagesService;
import gr.di.ecommerce.airbnb.utils.Constants;
import gr.di.ecommerce.airbnb.utils.KeyHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages/")
public class MessagesController {

    private static String className = MessagesController.class.getSimpleName();

    @Autowired
    private MessagesService messagesService;

    @RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String remove(@RequestHeader(Constants.AUTHORIZATION) String token, @PathVariable("id") Integer id) {
        if (KeyHolder.checkToken(token, className)) {
            messagesService.deleteMessage(id);
        } else {
            token = KeyHolder.issueToken(null);
        }
        return token;
    }

    @RequestMapping(value = "deletemsg/{id}/{user}/{type}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String removeMessage(@RequestHeader(Constants.AUTHORIZATION) String token, @PathVariable("id") Integer id,
                                              @PathVariable("user") Integer userId, @PathVariable("type") String type) {
        if (KeyHolder.checkToken(token, className)) {
            messagesService.removeMessage(id, userId, type);
        } else {
            token = KeyHolder.issueToken(null);
        }
        return token;
    }

    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody Messages getMessage(@RequestHeader(Constants.AUTHORIZATION) String token, @PathVariable("id") Integer id) {
        if (KeyHolder.checkToken(token, className)) {
            return messagesService.getMessage(id);
        }
        return null;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Messages> getAllMessages(@RequestHeader(Constants.AUTHORIZATION) String token) {
        if (KeyHolder.checkToken(token, className)) {
            return messagesService.getAllMessages();
        }
        return null;
    }

    @RequestMapping(value = "conversation/{id}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody List<Messages> getMessagesByConversation(@RequestHeader(Constants.AUTHORIZATION) String token,
                                                                  @PathVariable("id") Integer id) {
        if (KeyHolder.checkToken(token, className)) {
            return messagesService.getMessagesByConversation(id);
        }
        return null;
    }

    @RequestMapping(value = "addmessage", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String updateConversation(@RequestHeader(Constants.AUTHORIZATION) String token, Messages entity) {
        if (KeyHolder.checkToken(token, className)) {
            messagesService.updateConversation(entity);
            return token;
        }
        return "";
    }

    @RequestMapping(value = "count/{user}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody String countNewMessages(@RequestHeader(Constants.AUTHORIZATION) String token, @PathVariable("user") Integer user) {
        String countMsg = "0";

        return countMsg;
    }
}

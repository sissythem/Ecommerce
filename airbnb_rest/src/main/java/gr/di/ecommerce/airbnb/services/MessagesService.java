package gr.di.ecommerce.airbnb.services;

import gr.di.ecommerce.airbnb.entities.Messages;
import gr.di.ecommerce.airbnb.repositories.ConversationsRepository;
import gr.di.ecommerce.airbnb.repositories.MessagesRepository;
import gr.di.ecommerce.airbnb.repositories.UsersRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessagesService {

    @Autowired
    private MessagesRepository messagesRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ConversationsService conversationsService;

    public void createMessage(Messages messages) {
        messagesRepository.save(messages);
    }

    public void updateMessage(Messages messages) {
        messagesRepository.save(messages);
    }

    public void deleteMessage(Messages messages) {
        messagesRepository.delete(messages);
    }

    public void deleteMessage(Integer id) {
        messagesRepository.delete(id.toString());
    }

    public Messages getMessage(Integer id) {
        return messagesRepository.getOne(id.toString());
    }

    public List<Messages> getAllMessages() {
        return messagesRepository.findAll();
    }

    public void removeMessage(Integer id, Integer userId, String type) {
        if(!StringUtils.isBlank(type)) {
            if(type.equalsIgnoreCase("sender")) {
                messagesRepository.deleteFromSender(id, userId);
            } else if(type.equalsIgnoreCase("receiver")) {
                messagesRepository.deleteFromReceiver(id, userId);
            }
        }
    }

    public List<Messages> getMessagesByConversation(Integer conversationId) {
        return messagesRepository.findAllByConversationId(conversationId);
    }

    private void addNewMessage(Integer senderId, Integer conversationId, String msgBody) {
        Messages messages = new Messages();
        messages.setUserId(userService.getUser(senderId));
        messages.setBody(msgBody);
        messages.setConversationId(conversationsService.getConversation(conversationId));
        createMessage(messages);
    }

    public void updateConversation(Messages messages) {
        addNewMessage(messages.getUserId().getId(), messages.getConversationId().getId(), messages.getBody());
    }

    public String countNewMessages(Integer userId) {
        // TODO
        return null;
    }
}

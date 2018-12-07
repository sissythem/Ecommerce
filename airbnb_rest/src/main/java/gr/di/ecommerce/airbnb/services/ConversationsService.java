package gr.di.ecommerce.airbnb.services;

import gr.di.ecommerce.airbnb.entities.Conversations;
import gr.di.ecommerce.airbnb.repositories.ConversationsRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class ConversationsService {

    @Autowired
    private ConversationsRepository conversationsRepository;

    public void createConversation(Conversations conversations) {
        conversationsRepository.save(conversations);
    }

    public void updateConversation(Conversations conversations) {
        conversationsRepository.save(conversations);
    }

    public void removeConversation(Conversations conversations) {
        conversationsRepository.delete(conversations);
    }

    public void removeConversation(Integer id) {
        conversationsRepository.delete(id.toString());
    }

    public void deleteConversation(Integer id, Integer userId, String type) {
        if(!StringUtils.isBlank(type)) {
            if(type.equalsIgnoreCase("sender")) {
                conversationsRepository.deleteForSender(id, userId);
            } else if(type.equalsIgnoreCase("receiver")) {
                conversationsRepository.deleteForReceiver(id, userId);
            }
        }
    }

    public void restoreConversation(Integer id, Integer userId, String type) {
        if(!StringUtils.isBlank(type)) {
            if(type.equalsIgnoreCase("sender")) {
                conversationsRepository.restoreForSender(id, userId);
            } else if(type.equalsIgnoreCase("receiver")) {
                conversationsRepository.restoreForReceiver(id, userId);
            }
        }
    }

    public Conversations getConversation(Integer id) {
        return conversationsRepository.getOne(id.toString());
    }

    public List<Conversations> getAllConversations() {
        return conversationsRepository.findAll();
    }

    public Conversations getLastConversation(Integer senderId, Integer receiverId) {
        List<Conversations> conversations = conversationsRepository.findLastConversationEntry(senderId, receiverId);
        if (!CollectionUtils.isEmpty(conversations)) {
            return conversations.get(0);
        }
        return null;
    }

    public List<Conversations> getConversationsByResidence(Integer residenceId, Integer userId) {
        return conversationsRepository.findConversationByResidence(residenceId, userId);
    }

    public List<Conversations> getConversationsByUserId(Integer userId) {
        return conversationsRepository.findConversationByUserId(userId);
    }

    public void updateReadConversation(String isRead, String type, Integer id) {
        if(!StringUtils.isBlank(type)) {
            if(type.equalsIgnoreCase("sender")) {
                conversationsRepository.updateReadFromSender(isRead, id);
            } else if(type.equalsIgnoreCase("receiver")) {
                conversationsRepository.updateReadFromReceiver(isRead, id);
            }
        }
    }
}

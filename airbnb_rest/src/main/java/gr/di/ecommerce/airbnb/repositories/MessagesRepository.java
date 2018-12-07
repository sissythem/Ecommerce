package gr.di.ecommerce.airbnb.repositories;

import gr.di.ecommerce.airbnb.entities.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessagesRepository extends JpaRepository<Messages, String> {

    @Query("UPDATE Messages m SET m.deletedFromSender=1 WHERE m.id=(:id) AND (SELECT COUNT (c.id) FROM Conversations c WHERE c.id=m.conversationId AND c.senderId=(:senderId))>0")
    void deleteFromSender(@Param("id") Integer id, @Param("senderId") Integer senderId);

    @Query("UPDATE Messages m SET m.deletedFromReceiver=1 WHERE m.id=(:id) AND (SELECT COUNT (c.id) FROM Conversations c WHERE c.id=m.conversationId AND c.receiverId=(:receiverId))>0")
    void deleteFromReceiver(@Param("id") Integer id, @Param("receiverId") Integer receiverId);

    @Query("SELECT m FROM Messages m WHERE m.conversationId=(:conversationId)")
    List<Messages> findAllByConversationId(@Param("conversationId") Integer conversationId);
}

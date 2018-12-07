package gr.di.ecommerce.airbnb.repositories;

import gr.di.ecommerce.airbnb.entities.Conversations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversationsRepository extends JpaRepository<Conversations, String> {
    @Query("SELECT c FROM Conversations c WHERE c.senderId=(:userId) OR c.receiverId=(:userId)")
    List<Conversations> findConversationByUserId(@Param("userId") Integer userId);

    @Query("SELECT c FROM Conversations c WHERE c.senderId=(:senderId) AND c.receiverId=(:receiverId) ORDER BY id DESC")
    List<Conversations> findLastConversationEntry(@Param("senderId") Integer senderId, @Param("receiverId") Integer receiverId);

    @Query("SELECT c FROM Conversations c WHERE c.residenceId =(:residenceId) AND (c.senderId =(:senderId) OR c.receiverId =(:receiverId))")
    List<Conversations> findConversationByResidence(@Param("residenceId") Integer residenceId, @Param("userId") Integer userId);

    @Query("UPDATE Conversations c SET c.readFromSender=(:isRead) WHERE c.id=(:id)")
    void updateReadFromSender(@Param("isRead") String isRead, @Param("id") Integer id);

    @Query("UPDATE Conversations c SET c.readFromReceiver=(:isRead) WHERE c.id=(:id)")
    void updateReadFromReceiver(@Param("isRead") String isRead, @Param("id") Integer id);

    @Query("UPDATE Conversations c SET c.deletedFromSender=1 WHERE c.id=(:id) AND c.senderId=(:senderId)")
    void deleteForSender(@Param("id") Integer id, @Param("senderId") Integer senderId);

    @Query("UPDATE Conversations c SET c.deletedFromReceiver=1 WHERE c.id=(:id) AND c.receiverId=(:receiverId)")
    void deleteForReceiver(@Param("id") Integer id, @Param("receiverId") Integer receiverId);

    @Query("UPDATE Conversations c SET c.deletedFromSender=0 WHERE c.id=(:id) AND c.senderId=(:senderId)")
    void restoreForSender(@Param("id") Integer id, @Param("senderId") Integer senderId);

    @Query("UPDATE Conversations c SET c.deletedFromReceiver=0 WHERE c.id=(:id) AND c.receiverId=(:receiverId)")
    void restoreForReceiver(@Param("id") Integer id, @Param("receiverId") Integer receiverId);
}

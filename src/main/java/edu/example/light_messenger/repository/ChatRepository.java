package edu.example.light_messenger.repository;

import edu.example.light_messenger.model.ChatModel;
import edu.example.light_messenger.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface ChatRepository extends JpaRepository<ChatModel, Long> {

    @Query("""
            SELECT c
            FROM ChatModel c
            WHERE (c.user1=:user1 AND c.user2=:user2)
            OR (c.user1=:user2 AND c.user2=:user1)
            """)
    Optional<ChatModel> findChatForUsers(UserModel user1, UserModel user2);

    @Query("""
            SELECT c
            FROM ChatModel c
            WHERE c.user1=:user OR c.user2=:user
            """)
    Set<ChatModel> findChatsForUser(UserModel user);

}

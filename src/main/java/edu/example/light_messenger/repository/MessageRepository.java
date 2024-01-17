package edu.example.light_messenger.repository;

import edu.example.light_messenger.model.ChatModel;
import edu.example.light_messenger.model.MessageModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageModel, Long> {

    Page<MessageModel> findAllByChat(ChatModel chatModel, Pageable pageable);

}

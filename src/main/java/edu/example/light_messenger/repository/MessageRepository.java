package edu.example.light_messenger.repository;

import edu.example.light_messenger.model.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageModel, Long> {

}

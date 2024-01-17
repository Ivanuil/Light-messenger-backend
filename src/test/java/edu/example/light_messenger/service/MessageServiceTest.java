package edu.example.light_messenger.service;

import edu.example.light_messenger.TestContextConfig;
import edu.example.light_messenger.dto.RegisterRequestDto;
import edu.example.light_messenger.exception.EntityNotFoundException;
import edu.example.light_messenger.exception.UnprocessableEntityException;
import edu.example.light_messenger.repository.ChatRepository;
import edu.example.light_messenger.repository.MessageRepository;
import edu.example.light_messenger.repository.TokenRepository;
import edu.example.light_messenger.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MessageServiceTest extends TestContextConfig {

    @Autowired
    MessageService messageService;

    @Autowired
    AuthService authService;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    ChatRepository chatRepository;

    @BeforeEach
    @AfterEach
    public void clear() {
        messageRepository.deleteAll();
        chatRepository.deleteAll();
        tokenRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void sendMessageToUserNoSuchUser() throws UnprocessableEntityException {
        // given
        var user1 = new RegisterRequestDto();
        user1.setUsername("user1");
        user1.setPassword("pass1");
        authService.register(user1);

        var user2 = new RegisterRequestDto();
        user2.setUsername("user2");
        user2.setPassword("pass2");
        authService.register(user2);

        // when
        assertThrows(EntityNotFoundException.class, () ->
                messageService.sendMessageToWebSocket(user1.getUsername(),
                        user2.getUsername(), "message text"),
                "User offline");
    }

    @Test
    void sendMessage() throws UnprocessableEntityException {
        // given
        var sender = new RegisterRequestDto();
        sender.setUsername("user1");
        sender.setPassword("pass1");
        authService.register(sender);

        var recipient = new RegisterRequestDto();
        recipient.setUsername("user2");
        recipient.setPassword("pass2");
        authService.register(recipient);

        // when
        String messageText = "Message text!";
        messageService.sendMessage(sender.getUsername(), recipient.getUsername(), messageText);

        // then
        var messagePage = messageService.getMessages(
                userRepository.getReferenceById(recipient.getUsername()),
                sender.getUsername(),0, 5);
        assertEquals(1, messagePage.getTotalElements());
        assertEquals(1, messagePage.getTotalPages());
        var message = messagePage.get().findFirst().get();
        assertEquals(recipient.getUsername(), message.getTo().getUsername());
        assertEquals(sender.getUsername(), message.getFrom().getUsername());
        assertEquals(messageText, message.getText());
        assertEquals(Timestamp.valueOf(LocalDateTime.now()).getTime(),
                message.getTimestamp().getTime(),
                100);

    }

}

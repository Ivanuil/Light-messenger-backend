package edu.example.light_messenger.service;

import edu.example.light_messenger.TestContextConfig;
import edu.example.light_messenger.dto.RegisterRequestDto;
import edu.example.light_messenger.exception.EntityNotFoundException;
import edu.example.light_messenger.exception.UnprocessableEntityException;
import edu.example.light_messenger.repository.TokenRepository;
import edu.example.light_messenger.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MessageServiceTest extends TestContextConfig {

    @Autowired
    MessageService messageService;

    @Autowired
    AuthService authService;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    @AfterEach
    public void clear() {
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
        Assertions.assertThrows(EntityNotFoundException.class, () ->
                messageService.sendMessageToWebSocket(user1.getUsername(),
                        user2.getUsername(), "message text"),
                "User offline");
    }

}

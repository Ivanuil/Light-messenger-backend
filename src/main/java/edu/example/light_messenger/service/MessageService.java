package edu.example.light_messenger.service;

import edu.example.light_messenger.exception.EntityNotFoundException;
import edu.example.light_messenger.exception.WebSocketException;
import edu.example.light_messenger.model.UserModel;
import edu.example.light_messenger.repository.UserRepository;
import edu.example.light_messenger.web.socket.MessageWebSocketHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MessageService {

    private final MessageWebSocketHandler webSocketHandler;
    private final UserRepository userRepository;

    public MessageService(MessageWebSocketHandler webSocketHandler, UserRepository userRepository) {
        this.webSocketHandler = webSocketHandler;
        this.userRepository = userRepository;
    }

    /**
     * Checks if receiving user exists, then try to send message using WebSocketHandler
     * @param to receiving user's username
     * @param from sending user's username
     * @param text message text
     */
    public void sendMessageToWebSocket(String to, String from, String text) {
        UserModel receiver = userRepository.findById(to)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        try {
            webSocketHandler.sendMessage(receiver, from, text);
        } catch (IOException e) {
            throw new WebSocketException(e.getMessage());
        }
    }

}

package edu.example.light_messenger.web.socket;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.example.light_messenger.dto.MessageDto;
import edu.example.light_messenger.dto.MessageReceiveDto;
import edu.example.light_messenger.dto.MessageSendDto;
import edu.example.light_messenger.exception.EntityNotFoundException;
import edu.example.light_messenger.exception.WebSocketException;
import edu.example.light_messenger.model.UserModel;
import edu.example.light_messenger.repository.UserRepository;
import edu.example.light_messenger.service.TokenService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static edu.example.light_messenger.web.security.SecurityConstants.JWT_COOKIE_NAME;

@Component
public class MessageWebSocketHandler extends TextWebSocketHandler {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Logger logger = LoggerFactory.getLogger(MessageWebSocketHandler.class);

    @Value("${spring.kafka.topic-name}")
    private String topicName;

    public MessageWebSocketHandler(TokenService tokenService, UserRepository userRepository,
                                   KafkaTemplate<String, String> kafkaTemplate) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    private final Map<String, WebSocketSession> sessions = new HashMap<>();

    /**
     * Attempts to authorise user by token from session's attributes.
     * @param session
     * @throws WebSocketException if sending reply fails
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        try {
            session.sendMessage(new TextMessage("Connected!"));
        } catch (IOException e) {
            throw new WebSocketException(e.getMessage());
        }
        String token = (String) session.getAttributes().get(JWT_COOKIE_NAME);

        tokenService.findByTokenValue(token).ifPresentOrElse(tokenModel -> {
            sessions.put(tokenModel.getUser().getUsername(), session);
            logger.info("Opened WebSocket connection with user: " + tokenModel.getUser().getUsername());
            try {
                session.sendMessage(new TextMessage("Welcome " + tokenModel.getUser().getUsername() + "!"));
            } catch (IOException e) {
                throw new WebSocketException(e.getMessage());
            }
        }, () -> {
            try {
                session.sendMessage(new TextMessage("Unauthorised!"));
                session.close(CloseStatus.POLICY_VIOLATION);
            } catch (IOException e) {
                throw new WebSocketException(e.getMessage());
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String token = (String) session.getAttributes().get(JWT_COOKIE_NAME);
        if (!status.equalsCode(CloseStatus.POLICY_VIOLATION))
            sessions.remove(tokenService.findByTokenValue(token).get().getUser().getUsername());
    }

    /**
     * Handles receiving message from WebSocket by sending it to Kafka.
     * If receiving user doesn't exist, 'User not found' message will be sent to WebSocket.
     * If receiving user is offline, message will be lost.
     * @param session WebSocket session from which message was received
     * @param message text of message
     * @throws IOException if reply fails to send
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String token = (String) session.getAttributes().get(JWT_COOKIE_NAME);
        var messageSendDto = objectMapper.readValue(message.getPayload(), MessageSendDto.class);

        var userModelOptional =  userRepository.findById(messageSendDto.getTo());
        if (userModelOptional.isEmpty()) {
            session.sendMessage(new TextMessage("User not found!"));
            return;
        }

        MessageDto messageDto = new MessageDto(
                tokenService.findByTokenValue(token).get().getUser().getUsername(),
                messageSendDto.getTo(),
                messageSendDto.getText());
        String json = objectMapper.writeValueAsString(messageDto);
        kafkaTemplate.send(topicName, json);
    }

    /**
     * Sends message to WebSocket
     * @param to receiving user's model
     * @param from sender's username
     * @param text message text
     * @throws IOException if sending fails
     */
    public void sendMessage(UserModel to, String from, String text) throws IOException {
        var message = new MessageReceiveDto(from, text);
        String json = objectMapper.writeValueAsString(message);

        try {
            var session = sessions.get(to.getUsername());
            session.sendMessage(new TextMessage(json));
        } catch (NullPointerException e) {
            throw new EntityNotFoundException("User offline");
        }
    }

}

package edu.example.light_messenger.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.example.light_messenger.dto.MessageDto;
import edu.example.light_messenger.exception.EntityNotFoundException;
import edu.example.light_messenger.exception.WebSocketException;
import edu.example.light_messenger.model.UserModel;
import edu.example.light_messenger.repository.UserRepository;
import edu.example.light_messenger.web.socket.MessageWebSocketHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MessageService {

    private final MessageWebSocketHandler webSocketHandler;
    private final KafkaTemplate<String, String> kafkaTemplate;

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.kafka.topic-name}")
    private String topicName;

    public MessageService(MessageWebSocketHandler webSocketHandler,
                          KafkaTemplate<String, String> kafkaTemplate, UserRepository userRepository) {
        this.webSocketHandler = webSocketHandler;
        this.kafkaTemplate = kafkaTemplate;
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

    public void sendMessageToKafka(String from, String to, String text) throws JsonProcessingException {
        if (userRepository.findById(to).isEmpty())
            throw new EntityNotFoundException("No user with this username");

        MessageDto messageDto = new MessageDto(
                from,
                to,
                text);
        kafkaTemplate.send(topicName, to, objectMapper.writeValueAsString(messageDto));
    }

}

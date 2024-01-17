package edu.example.light_messenger.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.example.light_messenger.dto.MessageDto;
import edu.example.light_messenger.exception.EntityNotFoundException;
import edu.example.light_messenger.exception.WebSocketException;
import edu.example.light_messenger.model.MessageModel;
import edu.example.light_messenger.model.UserModel;
import edu.example.light_messenger.repository.ChatRepository;
import edu.example.light_messenger.repository.MessageRepository;
import edu.example.light_messenger.repository.UserRepository;
import edu.example.light_messenger.web.socket.MessageWebSocketHandler;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;

@Service
public class MessageService {

    private final MessageWebSocketHandler webSocketHandler;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ChatService chatService;

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;

    private final UserRepository userRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.kafka.topic-name}")
    private String topicName;

    public MessageService(MessageWebSocketHandler webSocketHandler, KafkaTemplate<String, String> kafkaTemplate,
                          MessageRepository messageRepository, ChatService chatService, ChatRepository chatRepository, UserRepository userRepository) {
        this.webSocketHandler = webSocketHandler;
        this.kafkaTemplate = kafkaTemplate;
        this.messageRepository = messageRepository;
        this.chatService = chatService;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    /**
     * Sends message to Kafka and then saves it to database
     * @param to receiving user's username
     * @param from sending user's username
     * @param text message text
     */
    public void sendMessage(String from, String to, String text) {
        var userFrom = userRepository.getReferenceById(from);
        var userTo = userRepository.getReferenceById(to);

        sendMessageToKafka(from, to, text);

        messageRepository.save(new MessageModel(0L,
                chatService.findOrCreateChat(userFrom, userTo),
                userFrom, userTo,
                new Timestamp(0), text));
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

    /**
     * Checks if user exists and then send message to kafka
     * @param from sending user's username
     * @param to receiving user's username
     * @param text message text
     */
    @SneakyThrows
    private void sendMessageToKafka(String from, String to, String text) {
        if (userRepository.findById(to).isEmpty())
            throw new EntityNotFoundException("No user with this username");

        MessageDto messageDto = new MessageDto(
                from,
                to,
                text);
        kafkaTemplate.send(topicName, to, objectMapper.writeValueAsString(messageDto));
    }

    public Page<MessageModel> getMessages(UserModel user1, String user2, int pageNumber, int pageSize) {
        var chatOpt = chatRepository.findChatForUsers(user1,
                userRepository.getReferenceById(user2));
        var pageable = PageRequest.of(pageNumber, pageSize, Sort.by("timestamp").descending());

        if (chatOpt.isEmpty())
            return Page.empty(pageable);
        return messageRepository.findAllByChat(chatOpt.get(), pageable);
    }

}

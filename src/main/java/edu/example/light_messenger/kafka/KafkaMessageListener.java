package edu.example.light_messenger.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.example.light_messenger.dto.MessageDto;
import edu.example.light_messenger.exception.EntityNotFoundException;
import edu.example.light_messenger.service.MessageService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageListener {

    private final MessageService messageService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaMessageListener(MessageService messageService) {
        this.messageService = messageService;
    }

    @KafkaListener(topics = "${spring.kafka.topic-name}",
            groupId = "${spring.kafka.group-id}", autoStartup = "true")
    public void listenGroupMessages(String message) throws JsonProcessingException {
        MessageDto messageDto = objectMapper.readValue(message, MessageDto.class);
        try {
            messageService.sendMessageToWebSocket(messageDto.getTo(),
                    messageDto.getFrom(),
                    messageDto.getText());
        } catch (EntityNotFoundException ignored) {}
    }

}

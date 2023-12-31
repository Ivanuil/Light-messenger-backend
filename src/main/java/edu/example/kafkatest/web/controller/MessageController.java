package edu.example.kafkatest.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.example.kafkatest.dto.MessageDto;
import edu.example.kafkatest.dto.MessageSendDto;
import edu.example.kafkatest.web.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${spring.kafka.topic-name}")
    private String topicName;

    public MessageController(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/send")
    @Operation(description = "Sends message to kafka (if receiving user doesn't exist " +
            "or is offline the message will be lost)")
    public void sendMessage(@RequestBody MessageSendDto message,
                            @AuthenticationPrincipal UserDetailsImpl userDetails) throws JsonProcessingException {
        MessageDto messageDto = new MessageDto(
                userDetails.getUsername(),
                message.getTo(),
                message.getText());
        kafkaTemplate.send(topicName, objectMapper.writeValueAsString(messageDto));
    }

}

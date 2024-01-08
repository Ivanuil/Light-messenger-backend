package edu.example.light_messenger.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.example.light_messenger.dto.MessageSendDto;
import edu.example.light_messenger.service.MessageService;
import edu.example.light_messenger.web.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    @Operation(description = "Sends message to kafka (if receiving user doesn't exist " +
            "or is offline the message will be lost)")
    public void sendMessage(@RequestBody MessageSendDto message,
                            @AuthenticationPrincipal UserDetailsImpl userDetails) throws JsonProcessingException {
        messageService.sendMessageToKafka(userDetails.getUsername(),
                message.getTo(),
                message.getText());
    }

}

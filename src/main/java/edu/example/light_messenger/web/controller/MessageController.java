package edu.example.light_messenger.web.controller;

import edu.example.light_messenger.dto.MessageResponseDto;
import edu.example.light_messenger.dto.MessageSendDto;
import edu.example.light_messenger.dto.PageResponse;
import edu.example.light_messenger.mapper.MessageMapper;
import edu.example.light_messenger.service.MessageService;
import edu.example.light_messenger.web.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class MessageController {

    private final MessageService messageService;
    private final MessageMapper messageMapper = MessageMapper.INSTANCE;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping("/send")
    @Operation(description = "Sends message to another user")
    public void sendMessage(@RequestBody MessageSendDto message,
                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        messageService.sendMessage(userDetails.getUsername(),
                message.getTo(),
                message.getText());
    }

    @GetMapping("/messages")
    @Operation(description = "Gets messages sent to user")
    public PageResponse<MessageResponseDto> getMessages(@RequestParam Integer pageNumber,
                                                        @RequestParam Integer pageSize,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return PageResponse.of(
                messageService.getMessages(userDetails.getUsername(), pageNumber, pageSize),
                messageMapper::toMessageResponseDto);
    }

}

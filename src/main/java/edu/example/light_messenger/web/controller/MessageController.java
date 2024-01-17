package edu.example.light_messenger.web.controller;

import edu.example.light_messenger.dto.MessageResponseDto;
import edu.example.light_messenger.dto.MessageSendDto;
import edu.example.light_messenger.dto.PageResponse;
import edu.example.light_messenger.mapper.MessageMapper;
import edu.example.light_messenger.service.ChatService;
import edu.example.light_messenger.service.MessageService;
import edu.example.light_messenger.web.security.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class MessageController {

    private final MessageService messageService;
    private final ChatService chatService;

    private final MessageMapper messageMapper = MessageMapper.INSTANCE;

    public MessageController(MessageService messageService, ChatService chatService) {
        this.messageService = messageService;
        this.chatService = chatService;
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
                                                        @RequestParam String companion,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return PageResponse.of(
                messageService.getMessages(userDetails.user(), companion, pageNumber, pageSize),
                messageMapper::toMessageResponseDto);
    }

    @GetMapping("/companions")
    @Operation(description = "Gets list of users with which user have chats")
    public Set<String> findCompanions(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return chatService.findCompanionsForUser(userDetails.user());
    }

}

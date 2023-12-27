package edu.example.kafkatest.web.controller;

import edu.example.kafkatest.web.security.UserDetailsImpl;
import edu.example.kafkatest.web.socket.WebSocketHandler;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class MessageController {

    private final WebSocketHandler webSocketHandler;

    public MessageController(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @PostMapping
    public void sendMessage(@RequestBody String message,
                            @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        webSocketHandler.sendMessage(userDetails.getUsername() + ": '" + message + "'");
    }

}

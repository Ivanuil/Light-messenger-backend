package edu.example.kafkatest.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class TestController {

    private ServerWebSocketHandler webSocketHandler;

    public TestController(ServerWebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @GetMapping
    public void sendPeriodicMessages() throws IOException {
        webSocketHandler.sendPeriodicMessages();
    }

}

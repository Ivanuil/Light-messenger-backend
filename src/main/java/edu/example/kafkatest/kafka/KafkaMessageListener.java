package edu.example.kafkatest.kafka;

import edu.example.kafkatest.web.socket.WebSocketHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class KafkaMessageListener {

    private final WebSocketHandler webSocketHandler;

    public KafkaMessageListener(WebSocketHandler webSocketHandler) {
        this.webSocketHandler = webSocketHandler;
    }

    @org.springframework.kafka.annotation.KafkaListener(topics = "${spring.kafka.topic-name}",
            groupId = "${spring.kafka.group-id}")
    public void listenGroupMessages(String message) throws IOException {
        webSocketHandler.sendMessage(message);
    }

}

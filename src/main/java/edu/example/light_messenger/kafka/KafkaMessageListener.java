package edu.example.light_messenger.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.example.light_messenger.dto.MessageDto;
import edu.example.light_messenger.exception.EntityNotFoundException;
import edu.example.light_messenger.service.MessageService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.LoggerFactory;

@Component
public class KafkaMessageListener {

    private final MessageService messageService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KafkaMessageListener(MessageService messageService) {
        this.messageService = messageService;
    }

    @KafkaListener(topics = "${spring.kafka.topic-name}", autoStartup = "true")
    public void listenGroupMessages(ConsumerRecord<?, ?> cr,
                                    Acknowledgment ack) throws JsonProcessingException {
        MessageDto messageDto = objectMapper.readValue((String) cr.value(), MessageDto.class);;
        try {
            messageService.sendMessageToWebSocket(messageDto.getTo(),
                    messageDto.getFrom(),
                    messageDto.getText());
            ack.acknowledge();
        } catch (EntityNotFoundException ignored) {
            LoggerFactory.getLogger(KafkaMessageListener.class).warn("No such user at this node");
	}
    }

}

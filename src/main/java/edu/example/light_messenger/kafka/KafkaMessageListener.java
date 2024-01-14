package edu.example.light_messenger.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.example.light_messenger.dto.MessageDto;
import edu.example.light_messenger.exception.EntityNotFoundException;
import edu.example.light_messenger.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.apache.kafka.clients.consumer.ConsumerRecord;

@Component
public class KafkaMessageListener {

    private final MessageService messageService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${logging.messages:false}")
    private boolean logMessages;
    private final Logger logger = LoggerFactory.getLogger(KafkaMessageListener.class);

    public KafkaMessageListener(MessageService messageService) {
        this.messageService = messageService;
    }

    @KafkaListener(topics = "${spring.kafka.topic-name}", autoStartup = "true")
    public void listenGroupMessages(ConsumerRecord<?, ?> cr,
                                    Acknowledgment ack) throws JsonProcessingException {
        MessageDto messageDto = objectMapper.readValue((String) cr.value(), MessageDto.class);
        try {
            messageService.sendMessageToWebSocket(messageDto.getTo(),
                    messageDto.getFrom(),
                    messageDto.getText());
            if (logMessages)
                logger.info("Received message for user connected to this instance (" + messageDto.getTo() + ")");
            ack.acknowledge();
        } catch (EntityNotFoundException ignored) {}
    }

}

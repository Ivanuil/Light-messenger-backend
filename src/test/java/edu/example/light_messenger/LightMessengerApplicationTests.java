package edu.example.light_messenger;

import edu.example.light_messenger.config.KafkaTestConfig;
import edu.example.light_messenger.config.PostgresTestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(initializers = {
        PostgresTestConfig.Initializer.class,
        KafkaTestConfig.Initializer.class})
class LightMessengerApplicationTests {

    @Test
    void contextLoads() {

    }

}

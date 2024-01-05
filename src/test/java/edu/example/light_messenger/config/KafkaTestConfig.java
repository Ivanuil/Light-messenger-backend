package edu.example.light_messenger.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;

@SpringBootTest
@ContextConfiguration
public class KafkaTestConfig {

    private static volatile KafkaContainer kafkaContainer = null;

    private static KafkaContainer getKafkaContainer() {
        KafkaContainer instance = kafkaContainer;
        if (instance == null) {
            synchronized (KafkaContainer.class) {
                instance = kafkaContainer;
                if (instance == null) {
                    kafkaContainer = instance = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka"))
                            .withStartupTimeout(Duration.ofSeconds(60))
                            .withReuse(true);
                    kafkaContainer.start();
                }
            }
        }
        return instance;
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            var kafkaContainer = getKafkaContainer();

            var bootstrapServers = kafkaContainer.getBootstrapServers();

            TestPropertyValues.of(
                    "spring.kafka.bootstrap-servers=" + bootstrapServers
            ).applyTo(applicationContext.getEnvironment());
        }

    }

}

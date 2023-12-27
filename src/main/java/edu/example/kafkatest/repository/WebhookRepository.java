package edu.example.kafkatest.repository;

import edu.example.kafkatest.model.WebHookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebhookRepository extends JpaRepository<WebHookEntity, Long> {

    List<WebHookEntity> findByUsername(String username);

}

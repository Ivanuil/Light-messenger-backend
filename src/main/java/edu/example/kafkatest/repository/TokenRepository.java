package edu.example.kafkatest.repository;

import edu.example.kafkatest.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByToken(String tokenValue);

    List<Token> findByUserUsername(String username);

}

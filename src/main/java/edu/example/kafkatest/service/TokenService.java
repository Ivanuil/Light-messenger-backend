package edu.example.kafkatest.service;

import edu.example.kafkatest.model.Token;
import edu.example.kafkatest.model.UserModel;
import edu.example.kafkatest.repository.TokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenService {

    private final TokenRepository tokenRepository;

    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public Token saveNewToken(String tokenValue, UserModel user) {
        Token token = new Token();
        token.setToken(tokenValue);
        token.setActive(true);
        token.setUser(user);
        return tokenRepository.save(token);
    }

    public Optional<Token> findByTokenValue(String token) {
        return tokenRepository.findByToken(token);
    }

    public void makeInactive(Token token) {
        token.setActive(false);
        tokenRepository.save(token);
    }

    public void deactivateUserTokens(String username) {
        List<Token> activeTokens = tokenRepository.findByUserUsername(username);
        activeTokens.forEach(token -> token.setActive(false));
        tokenRepository.saveAll(activeTokens);
    }


}

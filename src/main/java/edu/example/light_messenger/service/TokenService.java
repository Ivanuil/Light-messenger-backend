package edu.example.light_messenger.service;

import edu.example.light_messenger.model.Token;
import edu.example.light_messenger.model.UserModel;
import edu.example.light_messenger.repository.TokenRepository;
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

    public void saveNewToken(String tokenValue, UserModel user) {
        Token token = new Token();
        token.setToken(tokenValue);
        token.setActive(true);
        token.setUser(user);
        tokenRepository.save(token);
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

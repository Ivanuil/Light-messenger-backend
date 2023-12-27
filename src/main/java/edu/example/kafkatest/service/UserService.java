package edu.example.kafkatest.service;

import edu.example.kafkatest.exception.EntityNotFoundException;
import edu.example.kafkatest.model.UserModel;
import edu.example.kafkatest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserModel getUserByUsername(String username) {
        return userRepository.findById(username).orElseThrow(
                () -> new EntityNotFoundException("User with specified username not found"));
    }

}
package edu.example.light_messenger.service;

import edu.example.light_messenger.exception.EntityNotFoundException;
import edu.example.light_messenger.model.UserModel;
import edu.example.light_messenger.repository.UserRepository;
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
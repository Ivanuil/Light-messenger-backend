package edu.example.light_messenger.web.security;

import edu.example.light_messenger.model.UserModel;
import edu.example.light_messenger.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel user = userRepository.findById(username).orElseThrow(() ->
                new UsernameNotFoundException("Unable to find user with username: " + username));

        return new UserDetailsImpl(user);
    }
}

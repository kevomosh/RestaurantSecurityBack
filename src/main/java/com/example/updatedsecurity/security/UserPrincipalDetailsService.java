package com.example.updatedsecurity.security;

import com.example.updatedsecurity.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserPrincipalDetailsService implements UserDetailsService {
    private UserRepository userRepository;

    public UserPrincipalDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        var user = userRepository.findUserByName(name).orElseThrow(
                () -> new UsernameNotFoundException("user not found"));

        return new UserPrincipal(user);
    }

    public UserDetails loadUserById(String idStr) throws UsernameNotFoundException {
        var id = UUID.fromString(idStr);
        var user = userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found")
        );
        return new UserPrincipal(user);
    }
}

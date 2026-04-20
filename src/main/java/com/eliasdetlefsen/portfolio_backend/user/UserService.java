package com.eliasdetlefsen.portfolio_backend.user;

import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.eliasdetlefsen.portfolio_backend.exception.EmailAlreadyExistsException;
import com.eliasdetlefsen.portfolio_backend.exception.UserNotFoundException;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public User createUser(String email, String password, UserRole role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("User with email already exists: " + email);
        }

        String passwordHash = passwordEncoder.encode(password);

        User user = new User(email, passwordHash, role);

        return userRepository.save(user);
    }
}

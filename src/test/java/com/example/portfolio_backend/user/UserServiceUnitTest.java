package com.example.portfolio_backend.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.eliasdetlefsen.portfolio_backend.user.EmailAlreadyExistsException;
import com.eliasdetlefsen.portfolio_backend.user.User;
import com.eliasdetlefsen.portfolio_backend.user.UserRepository;
import com.eliasdetlefsen.portfolio_backend.user.UserRole;
import com.eliasdetlefsen.portfolio_backend.user.UserService;

public class UserServiceUnitTest {
    private UserRepository userRepository = mock(UserRepository.class);

    private PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private UserService userService = new UserService(userRepository, passwordEncoder);

    @Test
    void createUser_success() {
        // Arrange
        String email = "foo@bar.com", password = "password";
        UserRole role = UserRole.PUBLIC;

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode(password))
                .thenReturn("hashed-password");
        when(userRepository.save(any(User.class)))
                .thenAnswer(i -> i.getArgument(0));

        // Act
        User user = userService.createUser(email, password, role);

        // Assert
        assertEquals(email, user.getEmail());
        assertEquals(role, user.getRole());

        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void createUser_emailAlreadyExists_throwsException() {
        // Arrange
        String email = "foo@bar.com";

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(new User(email, "password", UserRole.PUBLIC)));

        // Act
        Executable action = () -> userService.createUser(email, "password", UserRole.PUBLIC);

        // Assert
        assertThrows(EmailAlreadyExistsException.class, action);

        verify(passwordEncoder, never()).encode(any(String.class));
        verify(userRepository, never()).save(any(User.class));
    }
}

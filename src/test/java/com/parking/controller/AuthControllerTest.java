package com.parking.controller;

import com.parking.dto.*;
import com.parking.entity.User;
import com.parking.enums.Role;
import com.parking.repository.UserRepository;
import com.parking.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtService jwtService;
    @InjectMocks private AuthController authController;

    @Test
    void register_ShouldCreateCustomerUser() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john");
        request.setEmail("john@test.com");
        request.setPassword("password123");

        when(userRepository.existsByUsername("john")).thenReturn(false);
        when(userRepository.existsByEmail("john@test.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        String result = authController.register(request);

        assertThat(result).isEqualTo("User registered successfully");
        verify(userRepository).save(argThat(u ->
                u.getUsername().equals("john") &&
                u.getRole() == Role.ROLE_CUSTOMER &&
                u.getStatus().equals("ACTIVE")
        ));
    }

    @Test
    void register_ShouldThrowWhenDuplicateUsername() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("john");
        request.setEmail("john@test.com");
        request.setPassword("pass");

        when(userRepository.existsByUsername("john")).thenReturn(true);

        assertThatThrownBy(() -> authController.register(request))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Username already exists");
    }

    @Test
    void login_ShouldReturnToken() {
        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("pass");

        User user = User.builder().username("john").password("encoded").role(Role.ROLE_CUSTOMER).build();
        when(userRepository.findByUsername("john")).thenReturn(java.util.Optional.of(user));
        when(jwtService.generateToken("john", "ROLE_CUSTOMER")).thenReturn("mock-jwt-token");

        AuthResponse response = authController.login(request);

        assertThat(response.getToken()).isEqualTo("mock-jwt-token");
        assertThat(response.getUsername()).isEqualTo("john");
        assertThat(response.getRole()).isEqualTo("ROLE_CUSTOMER");
        verify(authenticationManager).authenticate(any());
    }
}

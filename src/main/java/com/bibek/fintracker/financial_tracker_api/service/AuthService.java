package com.bibek.fintracker.financial_tracker_api.service;

import com.bibek.fintracker.financial_tracker_api.dto.AuthResponse;
import com.bibek.fintracker.financial_tracker_api.dto.LoginRequest;
import com.bibek.fintracker.financial_tracker_api.model.User;
import com.bibek.fintracker.financial_tracker_api.repository.UserRepository;
import com.bibek.fintracker.financial_tracker_api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;


    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username is already taken!");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email is already in use!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // ** NEW LOGIN METHOD **
    public AuthResponse login(LoginRequest loginRequest) {
        // 1. Authenticate the user's credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        // 2. Set the authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Generate the JWT
        String token = tokenProvider.generateToken(authentication);

        // 4. Return the response containing the token
        return new AuthResponse(token);
    }
}
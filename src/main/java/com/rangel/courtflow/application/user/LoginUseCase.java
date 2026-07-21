package com.rangel.courtflow.application.user;

import com.rangel.courtflow.domain.exception.InvalidCredentialsException;
import com.rangel.courtflow.infrastructure.persistence.UserJpaEntity;
import com.rangel.courtflow.infrastructure.persistence.UserJpaRepository;
import com.rangel.courtflow.infrastructure.security.JwtService;
import com.rangel.courtflow.infrastructure.web.dto.LoginRequestDTO;
import com.rangel.courtflow.infrastructure.web.dto.LoginResponseDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCase {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginUseCase(UserJpaRepository userJpaRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userJpaRepository = userJpaRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO execute(LoginRequestDTO requestDTO) {
        UserJpaEntity entity = userJpaRepository.findByEmail(requestDTO.email())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordEncoder.matches(requestDTO.password(), entity.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = jwtService.generateToken(entity.getId(), entity.getRole().name());

        return new LoginResponseDTO(token);
    }
}
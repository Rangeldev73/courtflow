package com.rangel.courtflow.application.user;

import com.rangel.courtflow.domain.exception.EmailAlreadyInUseException;
import com.rangel.courtflow.domain.model.User;
import com.rangel.courtflow.domain.model.UserRole;
import com.rangel.courtflow.infrastructure.persistence.UserJpaEntity;
import com.rangel.courtflow.infrastructure.persistence.UserJpaRepository;
import com.rangel.courtflow.infrastructure.web.dto.RegisterUserRequestDTO;
import com.rangel.courtflow.infrastructure.web.dto.UserResponseDTO;
import com.rangel.courtflow.infrastructure.web.mapper.UserMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class RegisterUserUseCase {

    private final UserJpaRepository userJpaRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(UserJpaRepository userJpaRepository, PasswordEncoder passwordEncoder) {
        this.userJpaRepository = userJpaRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO execute(RegisterUserRequestDTO requestDTO) {
        if (userJpaRepository.findByEmail(requestDTO.email()).isPresent()) {
            throw new EmailAlreadyInUseException("Email already in use: " + requestDTO.email());
        }

        String hashedPassword = passwordEncoder.encode(requestDTO.password());

        User user = new User(
                UUID.randomUUID(),
                requestDTO.email(),
                hashedPassword,
                UserRole.CUSTOMER
        );

        UserJpaEntity entityToSave = UserMapper.toJpaEntity(user);
        userJpaRepository.save(entityToSave);

        return UserMapper.toResponseDTO(user);
    }
}
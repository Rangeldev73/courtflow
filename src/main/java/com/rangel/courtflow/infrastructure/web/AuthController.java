package com.rangel.courtflow.infrastructure.web;

import com.rangel.courtflow.application.user.LoginUseCase;
import com.rangel.courtflow.application.user.RegisterUserUseCase;
import com.rangel.courtflow.infrastructure.web.dto.LoginRequestDTO;
import com.rangel.courtflow.infrastructure.web.dto.LoginResponseDTO;
import com.rangel.courtflow.infrastructure.web.dto.RegisterUserRequestDTO;
import com.rangel.courtflow.infrastructure.web.dto.UserResponseDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUseCase loginUseCase;
    public AuthController(RegisterUserUseCase registerUserUseCase,LoginUseCase loginUseCase) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterUserRequestDTO requestDTO) {
        UserResponseDTO responseDTO = registerUserUseCase.execute(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDTO) {
        LoginResponseDTO responseDTO = loginUseCase.execute(requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
}
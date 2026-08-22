package com.peerspaceClone.backend.api;

import com.peerspaceClone.backend.authentication.AuthenticationService;
import com.peerspaceClone.backend.dto.AuthenticationRequestDTO;
import com.peerspaceClone.backend.dto.AuthenticationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> login(@Valid @RequestBody AuthenticationRequestDTO authenticationRequestDTO) {
        AuthenticationResponseDTO response = authenticationService.authenticate(authenticationRequestDTO);
        return ResponseEntity.ok(response);
    }
}

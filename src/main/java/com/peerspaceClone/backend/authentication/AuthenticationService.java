package com.peerspaceClone.backend.authentication;

import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.peerspaceClone.backend.dto.AuthenticationRequestDTO;
import com.peerspaceClone.backend.dto.AuthenticationResponseDTO;
import com.peerspaceClone.backend.model.Role;
import com.peerspaceClone.backend.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));

        User user = (User) authentication.getPrincipal();
        String roleStr = user.getAllRoles().stream()
                .map(Role::getName)
                .collect(Collectors.joining(","));

        String token = jwtService.generateToken(authentication.getName(), roleStr);
        return new AuthenticationResponseDTO(token);
    }
}

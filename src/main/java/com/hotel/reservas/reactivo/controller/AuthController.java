package com.hotel.reservas.reactivo.controller;

import com.hotel.reservas.reactivo.dto.Usuario.LoginDto;
import com.hotel.reservas.reactivo.repository.UsuarioRepository;
import com.hotel.reservas.reactivo.shared.exception.InvalidCredentialsException;
import com.hotel.reservas.reactivo.shared.security.jwt.JwtResponse;
import com.hotel.reservas.reactivo.shared.security.jwt.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SecurityRequirements(value = {})
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Auth")
public class AuthController {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Login endpoint
    @PostMapping("/login")
    public Mono<ResponseEntity<?>> login(@RequestBody LoginDto request) {
        return usuarioRepository.findByEmail(request.getEmail())
                .switchIfEmpty(Mono.error(new InvalidCredentialsException("Credenciales inválidas")))
                .flatMap(usuario -> {
                    if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
                        return Mono.error(new InvalidCredentialsException("Credenciales inválidas"));
                    }
                    String token = jwtUtil.generateToken(usuario.getEmail());
                    JwtResponse response = new JwtResponse();
                    response.setToken(token);
                    return Mono.just(ResponseEntity.ok(response));
                });
    }

}

package com.hotel.reservas.reactivo.shared.security;

import com.hotel.reservas.reactivo.repository.RolRepository;
import com.hotel.reservas.reactivo.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements ReactiveUserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;

    @Override
    public Mono<UserDetails> findByUsername(String email) {
        return usuarioRepository.findByEmail(email)
                .flatMap(usuario ->
                        rolRepository.findById(usuario.getRolId())
                                .map(rol -> User.withUsername(usuario.getEmail())
                                        .password(usuario.getPassword())
                                        .roles(rol.getNombre()) // Spring espera un nombre tipo “ADMIN”, no el ID
                                        .build()
                                )
                );
    }
}

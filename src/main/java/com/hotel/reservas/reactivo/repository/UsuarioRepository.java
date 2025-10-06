package com.hotel.reservas.reactivo.repository;

import com.hotel.reservas.reactivo.model.Habitacion;
import com.hotel.reservas.reactivo.model.Usuario;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface UsuarioRepository extends ReactiveCrudRepository<Usuario, Long> {
    Mono<Usuario> findByEmail(String email); // usaremos email como username para el login
}

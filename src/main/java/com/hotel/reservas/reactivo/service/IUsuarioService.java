package com.hotel.reservas.reactivo.service;

import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionDto;
import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionSaveDto;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioDto;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioSaveDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IUsuarioService {
    Flux<UsuarioDto> findAll();
    Mono<UsuarioDto> findById(Long id);
    Mono<UsuarioDto> create(UsuarioSaveDto usuarioSaveDto);
    Mono<UsuarioDto> update(Long id, UsuarioSaveDto usuarioSaveDto);
    Mono<Void> delete(Long id);
}

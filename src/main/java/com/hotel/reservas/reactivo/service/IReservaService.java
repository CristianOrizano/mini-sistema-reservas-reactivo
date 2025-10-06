package com.hotel.reservas.reactivo.service;

import com.hotel.reservas.reactivo.dto.Reserva.ReservaDto;
import com.hotel.reservas.reactivo.dto.Reserva.ReservaSaveDto;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioDto;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioSaveDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IReservaService {
    Flux<ReservaDto> findAll();
    Mono<ReservaDto> findById(Long id);
    Mono<ReservaDto> create(ReservaSaveDto reservaSaveDto);
    Mono<ReservaDto> update(Long id, ReservaSaveDto reservaSaveDto);
    Mono<Void> delete(Long id);
}

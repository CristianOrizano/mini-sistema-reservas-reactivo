package com.hotel.reservas.reactivo.service;

import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionDto;
import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionSaveDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IHabitacionService {
    Flux<HabitacionDto> findAll();
    Mono<HabitacionDto> findById(Long id);
    Mono<HabitacionDto> create(HabitacionSaveDto habitacionBody);
    Mono<HabitacionDto> update(Long id, HabitacionSaveDto habitacionBody);
    Mono<Void> delete(Long id);
}

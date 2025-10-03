package com.hotel.reservas.reactivo.service.impl;

import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionDto;
import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionSaveDto;
import com.hotel.reservas.reactivo.dto.Habitacion.mapper.HabitacionMapper;
import com.hotel.reservas.reactivo.repository.HabitacionRepository;
import com.hotel.reservas.reactivo.service.IHabitacionService;
import com.hotel.reservas.reactivo.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class HabitacionServiceImpl implements IHabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;
    @Override
    public Flux<HabitacionDto> findAll() {
        return habitacionRepository.findAll()
                .map(habitacionMapper::toDto); // convierte cada entidad a DTO
    }

    @Override
    public Mono<HabitacionDto> findById(Long id) {
        //.map() sirve para acceder al objeto que está dentro del Mono y transformarlo
        return habitacionRepository.findById(id)
                .map(habitacionMapper::toDto)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Habitación no encontrada con id: " + id)));
    }

    @Override
    public Mono<HabitacionDto> create(HabitacionSaveDto habitacionBody) {
        return habitacionRepository.save(habitacionMapper.toEntity(habitacionBody))
                .map(habitacionMapper::toDto);
    }

    @Override
    public Mono<HabitacionDto> update(Long id, HabitacionSaveDto habitacionBody) {
        // Buscamos la habitación en la BD de forma reactiva (aún no tenemos el objeto, sino un Mono<Habitacion>)
        return habitacionRepository.findById(id)
                // Con flatMap "abrimos" el Mono para trabajar con la entidad Habitacion real
                .flatMap(habitacion -> {
                    habitacionMapper.updateEntity(habitacionBody, habitacion); // actualiza los campos
                    return habitacionRepository.save(habitacion);
                })
                .map(habitacionMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return habitacionRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "Habitación no encontrada")))
                .flatMap(habitacion -> habitacionRepository.delete(habitacion));
    }
}

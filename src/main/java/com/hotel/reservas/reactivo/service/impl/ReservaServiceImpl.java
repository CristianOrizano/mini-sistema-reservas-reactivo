package com.hotel.reservas.reactivo.service.impl;

import com.hotel.reservas.reactivo.dto.Habitacion.mapper.HabitacionMapper;
import com.hotel.reservas.reactivo.dto.Reserva.ReservaDto;
import com.hotel.reservas.reactivo.dto.Reserva.ReservaSaveDto;
import com.hotel.reservas.reactivo.dto.Reserva.mapper.ReservaMapper;
import com.hotel.reservas.reactivo.dto.Usuario.mapper.UsuarioMapper;
import com.hotel.reservas.reactivo.model.Reserva;
import com.hotel.reservas.reactivo.repository.HabitacionRepository;
import com.hotel.reservas.reactivo.repository.ReservaRepository;
import com.hotel.reservas.reactivo.repository.UsuarioRepository;
import com.hotel.reservas.reactivo.service.IHabitacionService;
import com.hotel.reservas.reactivo.service.IReservaService;
import com.hotel.reservas.reactivo.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ReservaServiceImpl  implements IReservaService {
    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final HabitacionRepository habitacionRepository;

    private final HabitacionMapper habitacionMapper;
    private final ReservaMapper reservaMapper;

    @Override
    public Flux<ReservaDto> findAll() {
        return reservaRepository.findAll()
                .flatMap(reserva ->
                        habitacionRepository.findById(reserva.getHabitacionId())
                                .map(habitacion -> {
                                    // Mapeamos reserva + habitacion
                                    ReservaDto dto = reservaMapper.toDto(reserva);
                                    dto.setHabitacion(habitacionMapper.toDto(habitacion));
                                    return dto;
                                })
                );
    }

    @Override
    public Mono<ReservaDto> findById(Long id) {
        return reservaRepository.findById(id)
                .map(reservaMapper::toDto)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Reserva no encontrada con id: " + id)));
    }

    @Override
    public Mono<ReservaDto> create(ReservaSaveDto reservaSaveDto) {
        return usuarioRepository.findById(reservaSaveDto.getUsuarioId())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario no encontrado")))
                .then(habitacionRepository.findById(reservaSaveDto.getHabitacionId())
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException("Habitación no encontrada"))))
                .then(Mono.defer(() -> {
                    Reserva reserva = reservaMapper.toEntity(reservaSaveDto);
                    reserva.setEstado("CONFIRMADA"); // o "PENDIENTE"
                    return reservaRepository.save(reserva).map(reservaMapper::toDto);
                }));
    }
    @Override
    public Mono<ReservaDto> update(Long id, ReservaSaveDto reservaSaveDto) {
        return reservaRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Reserva no encontrada con id: " + id)))
                .flatMap(habitacion -> {
                    reservaMapper.updateEntity(reservaSaveDto, habitacion);
                    return reservaRepository.save(habitacion);
                })
                .map(reservaMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return reservaRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Reserva no encontrada con id: " + id)))
                .flatMap(habitacion -> reservaRepository.delete(habitacion));
    }
}

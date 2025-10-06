package com.hotel.reservas.reactivo.service.impl;

import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionDto;
import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionFilterRequest;
import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionSaveDto;
import com.hotel.reservas.reactivo.dto.Habitacion.mapper.HabitacionMapper;
import com.hotel.reservas.reactivo.model.Habitacion;
import com.hotel.reservas.reactivo.repository.HabitacionRepository;
import com.hotel.reservas.reactivo.service.IHabitacionService;
import com.hotel.reservas.reactivo.shared.exception.ResourceNotFoundException;
import com.hotel.reservas.reactivo.shared.page.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HabitacionServiceImpl implements IHabitacionService {

    private final HabitacionRepository habitacionRepository;
    private final HabitacionMapper habitacionMapper;
    private final R2dbcEntityTemplate template;

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
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Habitación no encontrada con id: " + id)))
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
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Habitación no encontrada con id: " + id)))
                .flatMap(habitacion -> habitacionRepository.delete(habitacion));
    }

    public Mono<PageResponse<HabitacionDto>> findHabitaciones(HabitacionFilterRequest filter) {
        Criteria criteria = Criteria.empty();

        if (filter.getNumero() != null && !filter.getNumero().isBlank()) {
            criteria = criteria.and("numero").like("%" + filter.getNumero() + "%");
        }
        if (filter.getTipo() != null && !filter.getTipo().isBlank()) {
            criteria = criteria.and("tipo").like("%" + filter.getTipo() + "%");
        }
        if (filter.getDisponible() != null) {
            criteria = criteria.and("disponible").is(filter.getDisponible());
        }

        Sort sort = Sort.by(
                "desc".equalsIgnoreCase(filter.getSortDir()) ? Sort.Direction.DESC : Sort.Direction.ASC,
                filter.getSortBy() != null ? filter.getSortBy() : "id"
        );

        Query query = Query.query(criteria)
                .sort(sort)
                .limit(filter.getSize())
                .offset((long) filter.getPage() * filter.getSize());

        Mono<List<HabitacionDto>> content = template.select(query, Habitacion.class)
                .map(habitacionMapper::toDto)
                .collectList();

        Mono<Long> total = template.count(Query.query(criteria), Habitacion.class);

        return content.zipWith(total)
                .map(tuple -> new PageResponse<>(tuple.getT1(),filter.getPage() + 1, filter.getSize(), tuple.getT2()));
    }



}

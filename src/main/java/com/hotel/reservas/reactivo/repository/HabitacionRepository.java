package com.hotel.reservas.reactivo.repository;

import com.hotel.reservas.reactivo.model.Habitacion;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface HabitacionRepository extends ReactiveCrudRepository<Habitacion, Long> {
    // Aquí puedes agregar consultas personalizadas si quieres
}

package com.hotel.reservas.reactivo.repository;

import com.hotel.reservas.reactivo.model.Reserva;
import com.hotel.reservas.reactivo.model.Usuario;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ReservaRepository extends ReactiveCrudRepository<Reserva, Long> {

}

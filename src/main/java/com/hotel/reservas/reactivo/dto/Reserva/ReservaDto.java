package com.hotel.reservas.reactivo.dto.Reserva;

import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionDto;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioDto;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDate;

@Data
public class ReservaDto {
    private Long id;
    private HabitacionDto habitacion;
    private Long usuarioId;
    private Long habitacionId;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private String estado;
    private Double total;
}

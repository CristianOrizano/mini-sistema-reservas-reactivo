package com.hotel.reservas.reactivo.dto.Habitacion;

import lombok.*;

@Data
public class HabitacionDto {
    private Long id;
    private String numero;
    private String tipo;
    private Double precio;
    private Boolean disponible;
}

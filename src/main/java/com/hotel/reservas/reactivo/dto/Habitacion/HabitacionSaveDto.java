package com.hotel.reservas.reactivo.dto.Habitacion;

import lombok.Data;

@Data
public class HabitacionSaveDto {
    private String numero;
    private String tipo;
    private Double precio;
    private Boolean disponible;
}

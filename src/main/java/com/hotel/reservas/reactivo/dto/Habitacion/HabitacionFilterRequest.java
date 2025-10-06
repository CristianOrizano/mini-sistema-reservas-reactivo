package com.hotel.reservas.reactivo.dto.Habitacion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabitacionFilterRequest {
    private String numero;
    private String tipo;
    private Boolean disponible;
    private Integer page = 0;        // valor por defecto
    private Integer size = 10;       // valor por defecto
    private String sortBy = "id";    // campo por defecto
    private String sortDir = "asc";  // dirección por defecto
}
package com.hotel.reservas.reactivo.dto.Habitacion;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class HabitacionSaveDto {

    @NotBlank(message = "El número de habitación es obligatorio")
    @Size(max = 10, message = "El número no puede tener más de 10 caracteres")
    private String numero;

    @NotBlank(message = "El tipo de habitación es obligatorio")
    @Size(max = 50, message = "El tipo no puede tener más de 50 caracteres")
    private String tipo;

    @NotNull(message = "El precio es obligatorio")
    @Positive(message = "El precio debe ser mayor que 0")
    private Double precio;

    @NotNull(message = "Debe indicar si la habitación está disponible o no")
    private Boolean disponible;
}

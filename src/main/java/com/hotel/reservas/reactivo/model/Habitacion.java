package com.hotel.reservas.reactivo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("habitacion")
public class Habitacion {
    @Id
    private Long id;
    private String numero;       // Ej: "101", "Suite A"
    private String tipo;         // Ej: "Simple", "Doble", "Suite"
    private Double precio;
    private Boolean disponible;  // True si la habitación está disponible
}

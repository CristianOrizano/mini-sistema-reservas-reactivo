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
@Table("rol")
public class Rol {
    @Id
    private Long id;
    private String nombre; // Ej: "ADMIN", "USER"
}

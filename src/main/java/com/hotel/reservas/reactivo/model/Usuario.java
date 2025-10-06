package com.hotel.reservas.reactivo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table("usuario")
public class Usuario {
    @Id
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String password; // almacenaremos el hash
    private String telefono;
    @Column("fecha_registro")   // porque en la BD está en snake_case
    private LocalDate fechaRegistro;
    @Column("rol_id")
    private Long rolId; // 🔗 FK hacia tabla rol
}

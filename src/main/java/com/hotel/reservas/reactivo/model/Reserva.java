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
@Table("reserva")
public class Reserva {
    @Id
    private Long id;
    @Column("usuario_id")
    private Long usuarioId;    // FK hacia Usuario

    @Column("habitacion_id")
    private Long habitacionId; // FK hacia Habitacion

    @Column("fecha_entrada")
    private LocalDate fechaEntrada;

    @Column("fecha_salida")
    private LocalDate fechaSalida;
    private String estado;
    private Double total;
}

package com.hotel.reservas.reactivo.dto.Reserva;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservaSaveDto {
    private Long usuarioId;
    private Long habitacionId;
    private LocalDate fechaEntrada;
    private LocalDate fechaSalida;
    private String estado;
    private Double total;
}

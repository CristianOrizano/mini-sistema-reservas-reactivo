package com.hotel.reservas.reactivo.dto.Usuario;

import lombok.Data;

import java.time.LocalDate;


@Data
public class UsuarioDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String telefono;
    private LocalDate fechaRegistro;
}

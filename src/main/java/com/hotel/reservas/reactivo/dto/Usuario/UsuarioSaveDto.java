package com.hotel.reservas.reactivo.dto.Usuario;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UsuarioSaveDto {
    private String nombre;
    private String apellido;
    private String email;
    private String password;
    private String telefono;
}

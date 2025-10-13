package com.hotel.reservas.reactivo.dto.Usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginDto {

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El formato del email no es válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 3, max = 30, message = "La contraseña debe tener entre 3 y 30 caracteres")
    private String password;
}

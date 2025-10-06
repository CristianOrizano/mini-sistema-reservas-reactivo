package com.hotel.reservas.reactivo.controller;

import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionDto;
import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionSaveDto;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioDto;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioSaveDto;
import com.hotel.reservas.reactivo.service.IHabitacionService;
import com.hotel.reservas.reactivo.service.IUsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/usuario")
@Tag(name = "Usuario")
public class UsuarioController {

    private final IUsuarioService usuarioService;

    @Operation(summary = "Obtener todas las Usuarios", description = "Devuelve un listado de Usuarios registradas")
    @GetMapping
    public Flux<UsuarioDto> findAll() {
        return usuarioService.findAll();
    }

    @Operation(summary = "Obtener usuario por ID", description = "Busca una usuario en base a su identificador")
    @ApiResponse(responseCode = "200", description = "usuario encontrada")
    @ApiResponse(responseCode = "404", description = "usuario no encontrada")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<UsuarioDto>> findById(@PathVariable Long id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok); // Devuelve 200 OK si se encuentra
    }


    @PostMapping
    public Mono<ResponseEntity<UsuarioDto>> create(@RequestBody UsuarioSaveDto usuarioSaveDto) {
        return usuarioService.create(usuarioSaveDto)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<UsuarioDto>> update(@PathVariable("id") Long id,
                                                      @RequestBody UsuarioSaveDto usuarioSaveDto) {
        return usuarioService.update(id, usuarioSaveDto)
                .map(dto -> ResponseEntity.ok(dto));
    }

}

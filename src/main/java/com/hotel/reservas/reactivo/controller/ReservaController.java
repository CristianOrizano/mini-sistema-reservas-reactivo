package com.hotel.reservas.reactivo.controller;

import com.hotel.reservas.reactivo.dto.Reserva.ReservaDto;
import com.hotel.reservas.reactivo.dto.Reserva.ReservaSaveDto;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioDto;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioSaveDto;
import com.hotel.reservas.reactivo.service.IReservaService;
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
@RequestMapping("/api/reserva")
@Tag(name = "Reserva")
public class ReservaController {

    private final IReservaService reservaService;

    @Operation(summary = "Obtener todas las Reservas", description = "Devuelve un listado de reservas registradas")
    @GetMapping
    public Flux<ReservaDto> findAll() {
        return reservaService.findAll();
    }

    @Operation(summary = "Obtener reserva por ID", description = "Busca una reserva en base a su identificador")
    @ApiResponse(responseCode = "200", description = "usuario encontrada")
    @ApiResponse(responseCode = "404", description = "usuario no encontrada")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<ReservaDto>> findById(@PathVariable Long id) {
        return reservaService.findById(id)
                .map(ResponseEntity::ok); // Devuelve 200 OK si se encuentra
    }


    @PostMapping
    public Mono<ResponseEntity<ReservaDto>> create(@RequestBody ReservaSaveDto reservaSaveDto) {
        return reservaService.create(reservaSaveDto)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ReservaDto>> update(@PathVariable("id") Long id,
                                                   @RequestBody ReservaSaveDto reservaSaveDto) {
        return reservaService.update(id, reservaSaveDto)
                .map(dto -> ResponseEntity.ok(dto));
    }


}

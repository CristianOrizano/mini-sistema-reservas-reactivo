package com.hotel.reservas.reactivo.controller;

import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionDto;
import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionFilterRequest;
import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionSaveDto;
import com.hotel.reservas.reactivo.service.IHabitacionService;
import com.hotel.reservas.reactivo.service.impl.HabitacionServiceImpl;
import com.hotel.reservas.reactivo.shared.page.PageResponse;
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
@RequestMapping("/api/habitacion")
@Tag(name = "Habitacion")
public class HabitacionController {

    private final IHabitacionService habitacionService;

    @Operation(summary = "Obtener todas las habitaciones", description = "Devuelve un listado de habitaciones registradas")
    @GetMapping
    public Flux<HabitacionDto> findAll() {
        return habitacionService.findAll();
    }

    @Operation(summary = "Obtener habitación por ID", description = "Busca una habitación en base a su identificador")
    @ApiResponse(responseCode = "200", description = "Habitación encontrada")
    @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    @GetMapping("/{id}")
    public Mono<ResponseEntity<HabitacionDto>> findById(@PathVariable Long id) {
        return habitacionService.findById(id)
                .map(ResponseEntity::ok); // Devuelve 200 OK si se encuentra
    }


    @PostMapping
    public Mono<ResponseEntity<HabitacionDto>> create(@RequestBody HabitacionSaveDto habitacion) {
        return habitacionService.create(habitacion)
                .map(dto -> ResponseEntity.status(HttpStatus.CREATED).body(dto));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<HabitacionDto>> update(@PathVariable("id") Long id,
                                                      @RequestBody HabitacionSaveDto habitacion) {
        return habitacionService.update(id, habitacion)
                .map(dto -> ResponseEntity.ok(dto));
    }

    @Operation(summary = "Eliminar habitación")
    @ApiResponse(responseCode = "204", description = "Habitación eliminada con éxito")
    @ApiResponse(responseCode = "404", description = "Habitación no encontrada")
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable Long id) {
        return habitacionService.delete(id)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @GetMapping("paginated")
    public Mono<ResponseEntity<PageResponse<HabitacionDto>>> getHabitaciones(
            @RequestParam(required = false) String numero,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) Boolean disponible,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir
    ) {
        HabitacionFilterRequest filter = new HabitacionFilterRequest(
                numero,
                tipo,
                disponible,
                page > 0 ? page - 1 : 0,   // evita negativos
                size,
                sortBy,
                sortDir
        );
        return habitacionService.findHabitaciones(filter)
                .map(ResponseEntity::ok);
    }
}

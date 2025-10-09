package com.hotel.reservas.reactivo.service;

import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionDto;
import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionSaveDto;
import com.hotel.reservas.reactivo.dto.Habitacion.mapper.HabitacionMapper;
import com.hotel.reservas.reactivo.model.Habitacion;
import com.hotel.reservas.reactivo.repository.HabitacionRepository;
import com.hotel.reservas.reactivo.service.impl.HabitacionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class HabitacionServiceTest {
    @Mock
    private HabitacionRepository habitacionRepository;
    @Mock
    private HabitacionMapper habitacionMapper;
    @Mock
    private R2dbcEntityTemplate template;
    @InjectMocks
    private HabitacionServiceImpl habitacionService;

    private Habitacion habitacion;
    private HabitacionDto habitacionDto;
    private HabitacionSaveDto habitacionSaveDto;
    @BeforeEach
    void setUp() {
        habitacion = new Habitacion();
        habitacion.setId(1L);
        habitacion.setNumero("101");
        habitacion.setTipo("Suite");
        habitacion.setPrecio(250.0);
        habitacion.setDisponible(true);

        habitacionDto = new HabitacionDto();
        habitacionDto.setId(1L);
        habitacionDto.setNumero("101");
        habitacionDto.setTipo("Suite");
        habitacionDto.setPrecio(250.0);
        habitacionDto.setDisponible(true);

        habitacionSaveDto = new HabitacionSaveDto();
        habitacionSaveDto.setNumero("101");
        habitacionSaveDto.setTipo("Suite");
        habitacionSaveDto.setPrecio(250.0);
        habitacionSaveDto.setDisponible(true);
    }

    @DisplayName("Test reactivo para listar habitaciones")
    @Test
    void testFindAll() {
        when(habitacionRepository.findAll()).thenReturn(Flux.just(habitacion));
        when(habitacionMapper.toDto(habitacion)).thenReturn(habitacionDto);

        StepVerifier.create(habitacionService.findAll())
                .expectNext(habitacionDto)  // espera que el primer elemento emitido sea "habitacionDto"
                .verifyComplete();

        verify(habitacionRepository).findAll();
        verify(habitacionMapper).toDto(habitacion);
    }

    @DisplayName("Test reactivo para obtener habitación por ID")
    @Test
    void testFindById() {
        when(habitacionRepository.findById(1L)).thenReturn(Mono.just(habitacion));
        when(habitacionMapper.toDto(habitacion)).thenReturn(habitacionDto);

        StepVerifier.create(habitacionService.findById(1L))
                .expectNext(habitacionDto)
                .verifyComplete();

        verify(habitacionRepository).findById(1L);
        verify(habitacionMapper).toDto(habitacion);
    }

    @DisplayName("Test reactivo para crear habitación")
    @Test
    void testCreateHabitacion() {
        when(habitacionMapper.toEntity(habitacionSaveDto)).thenReturn(habitacion);
        when(habitacionRepository.save(habitacion)).thenReturn(Mono.just(habitacion));
        when(habitacionMapper.toDto(habitacion)).thenReturn(habitacionDto);

        StepVerifier.create(habitacionService.create(habitacionSaveDto))
                .expectNextMatches(dto ->
                        dto.getNumero().equals("101") &&
                                dto.getTipo().equals("Suite") &&
                                dto.getPrecio().equals(250.0))
                .verifyComplete();

        verify(habitacionRepository).save(habitacion);
    }

    @DisplayName("Test reactivo para actualizar habitación")
    @Test
    void testUpdateHabitacion() {
        Long id = 1L;

        when(habitacionRepository.findById(id)).thenReturn(Mono.just(habitacion));
        when(habitacionMapper.updateEntity(habitacionSaveDto, habitacion)).thenReturn(habitacion);
        when(habitacionRepository.save(habitacion)).thenReturn(Mono.just(habitacion));
        when(habitacionMapper.toDto(habitacion)).thenReturn(habitacionDto);

        StepVerifier.create(habitacionService.update(id, habitacionSaveDto))
                .expectNext(habitacionDto)
                .verifyComplete();

        verify(habitacionRepository).findById(id);
        verify(habitacionMapper).updateEntity(habitacionSaveDto, habitacion);
        verify(habitacionRepository).save(habitacion);
    }


}

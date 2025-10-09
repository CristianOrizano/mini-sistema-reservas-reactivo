package com.hotel.reservas.reactivo.service;
import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionDto;
import com.hotel.reservas.reactivo.dto.Habitacion.mapper.HabitacionMapper;
import com.hotel.reservas.reactivo.dto.Reserva.ReservaDto;
import com.hotel.reservas.reactivo.dto.Reserva.ReservaSaveDto;
import com.hotel.reservas.reactivo.dto.Reserva.mapper.ReservaMapper;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioDto;
import com.hotel.reservas.reactivo.model.Habitacion;
import com.hotel.reservas.reactivo.model.Reserva;
import com.hotel.reservas.reactivo.model.Usuario;
import com.hotel.reservas.reactivo.repository.HabitacionRepository;
import com.hotel.reservas.reactivo.repository.ReservaRepository;
import com.hotel.reservas.reactivo.repository.UsuarioRepository;
import com.hotel.reservas.reactivo.service.impl.ReservaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private HabitacionRepository habitacionRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private HabitacionMapper habitacionMapper;

    @Mock
    private ReservaMapper reservaMapper;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    private Reserva reserva;
    private ReservaDto reservaDto;
    private Usuario usuario;
    private UsuarioDto usuarioDto;
    private ReservaSaveDto reservaSaveDto;
    private Habitacion habitacion;
    private HabitacionDto habitacionDto;

    @BeforeEach
    void setUp() {
        habitacion = new Habitacion();
        habitacion.setId(5L);

        habitacionDto = new HabitacionDto();
        habitacionDto.setId(5L);

        usuario = new Usuario();
        usuario.setId(10L);

        usuarioDto = new UsuarioDto();
        usuarioDto.setId(10L);

        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setUsuarioId(10L);
        reserva.setHabitacionId(5L);
        reserva.setEstado("CONFIRMADA");
        reserva.setTotal(500.0);

        reservaDto = new ReservaDto();
        reservaDto.setId(1L);
        reservaDto.setUsuarioId(10L);
        reservaDto.setHabitacionId(5L);
        reservaDto.setEstado("CONFIRMADA");
        reservaDto.setTotal(500.0);

        reservaSaveDto = new ReservaSaveDto();
        reservaSaveDto.setUsuarioId(10L);
        reservaSaveDto.setHabitacionId(5L);
        reservaSaveDto.setEstado("CONFIRMADA");
        reservaSaveDto.setTotal(500.0);
    }

    @DisplayName("Test reactivo para listar reservas")
    @Test
    void testFindAll() {
        when(reservaRepository.findAll()).thenReturn(Flux.just(reserva));
        when(habitacionRepository.findById(5L)).thenReturn(Mono.just(habitacion));
        when(habitacionMapper.toDto(habitacion)).thenReturn(habitacionDto);
        when(reservaMapper.toDto(reserva)).thenReturn(reservaDto);

        StepVerifier.create(reservaService.findAll())
                .expectNext(reservaDto)
                .verifyComplete();

        verify(reservaRepository).findAll();
        verify(habitacionRepository).findById(5L);
        verify(habitacionMapper).toDto(habitacion);
        verify(reservaMapper).toDto(reserva);
    }

    @DisplayName("Test reactivo para obtener reserva por ID")
    @Test
    void testFindById() {
        when(reservaRepository.findById(1L)).thenReturn(Mono.just(reserva));
        when(reservaMapper.toDto(reserva)).thenReturn(reservaDto);

        StepVerifier.create(reservaService.findById(1L))
                .expectNext(reservaDto)
                .verifyComplete();

        verify(reservaRepository).findById(1L);
        verify(reservaMapper).toDto(reserva);
    }

    @DisplayName("Test reactivo para crear reserva")
    @Test
    void testCreateReserva() {
        when(usuarioRepository.findById(10L)).thenReturn(Mono.just(usuario));
        when(habitacionRepository.findById(5L)).thenReturn(Mono.just(habitacion));
        when(reservaMapper.toEntity(reservaSaveDto)).thenReturn(reserva);
        when(reservaRepository.save(reserva)).thenReturn(Mono.just(reserva));
        when(reservaMapper.toDto(reserva)).thenReturn(reservaDto);

        StepVerifier.create(reservaService.create(reservaSaveDto))
                .expectNextMatches(dto ->
                        dto.getUsuarioId().equals(10L) &&
                                dto.getHabitacionId().equals(5L) &&
                                dto.getEstado().equals("CONFIRMADA"))
                .verifyComplete();

        verify(reservaRepository).save(reserva);
    }

    @DisplayName("Test reactivo para actualizar reserva")
    @Test
    void testUpdateReserva() {
        Long id = 1L;

        when(reservaRepository.findById(id)).thenReturn(Mono.just(reserva));
        when(reservaMapper.updateEntity(reservaSaveDto, reserva)).thenReturn(reserva);
        when(reservaRepository.save(reserva)).thenReturn(Mono.just(reserva));
        when(reservaMapper.toDto(reserva)).thenReturn(reservaDto);

        StepVerifier.create(reservaService.update(id, reservaSaveDto))
                .expectNext(reservaDto)
                .verifyComplete();

        verify(reservaRepository).findById(id);
        verify(reservaMapper).updateEntity(reservaSaveDto, reserva);
        verify(reservaRepository).save(reserva);
    }
}

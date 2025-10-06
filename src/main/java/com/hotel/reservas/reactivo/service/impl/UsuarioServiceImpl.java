package com.hotel.reservas.reactivo.service.impl;

import com.hotel.reservas.reactivo.dto.Habitacion.mapper.HabitacionMapper;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioDto;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioSaveDto;
import com.hotel.reservas.reactivo.dto.Usuario.mapper.UsuarioMapper;
import com.hotel.reservas.reactivo.model.Usuario;
import com.hotel.reservas.reactivo.repository.HabitacionRepository;
import com.hotel.reservas.reactivo.repository.UsuarioRepository;
import com.hotel.reservas.reactivo.service.IUsuarioService;
import com.hotel.reservas.reactivo.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@RequiredArgsConstructor
@Service
public class UsuarioServiceImpl implements IUsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    @Override
    public Flux<UsuarioDto> findAll() {
        return usuarioRepository.findAll()
                .map(usuarioMapper::toDto);
    }

    @Override
    public Mono<UsuarioDto> findById(Long id) {
        return usuarioRepository.findById(id)
                .map(usuarioMapper::toDto)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario no encontrada con id: " + id)));
    }


    @Override
    public Mono<UsuarioDto> create(UsuarioSaveDto usuarioSaveDto) {
        return usuarioRepository.findByEmail(usuarioSaveDto.getEmail())
                .flatMap(existingUser ->
                        Mono.error(new IllegalArgumentException("El email ya está registrado"))
                )
                .then(Mono.defer(() -> {
                    Usuario entity = usuarioMapper.toEntity(usuarioSaveDto);
                    entity.setPassword(passwordEncoder.encode(usuarioSaveDto.getPassword()));
                    if (entity.getRolId() == null) {
                        entity.setRolId(2L); // por ejemplo, 2 = "USER"
                    }
                    entity.setFechaRegistro(LocalDate.now());
                    return usuarioRepository.save(entity)
                            .map(usuarioMapper::toDto);
                }));
    }
    @Override
    public Mono<UsuarioDto> update(Long id, UsuarioSaveDto usuarioSaveDto) {
        return usuarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario no encontrada con id: " + id)))
                .flatMap(usuario -> {
                    usuarioMapper.updateEntity(usuarioSaveDto, usuario); // actualiza los campos
                    return usuarioRepository.save(usuario);
                })
                .map(usuarioMapper::toDto);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return usuarioRepository.findById(id)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException("Usuario no encontrada con id: " + id)))
                .flatMap(habitacion -> usuarioRepository.delete(habitacion));
    }
}

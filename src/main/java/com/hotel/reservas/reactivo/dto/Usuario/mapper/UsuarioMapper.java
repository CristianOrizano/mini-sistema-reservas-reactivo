package com.hotel.reservas.reactivo.dto.Usuario.mapper;

import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionDto;
import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionSaveDto;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioDto;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioSaveDto;
import com.hotel.reservas.reactivo.model.Habitacion;
import com.hotel.reservas.reactivo.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {
    UsuarioDto toDto(Usuario usuario);
    Usuario toEntity(UsuarioSaveDto usuarioSaveDto);
    Usuario updateEntity(UsuarioSaveDto usuarioSaveDto, @MappingTarget Usuario usuario);
}

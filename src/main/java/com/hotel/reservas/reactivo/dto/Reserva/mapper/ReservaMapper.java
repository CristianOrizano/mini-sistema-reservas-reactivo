package com.hotel.reservas.reactivo.dto.Reserva.mapper;

import com.hotel.reservas.reactivo.dto.Reserva.ReservaDto;
import com.hotel.reservas.reactivo.dto.Reserva.ReservaSaveDto;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioDto;
import com.hotel.reservas.reactivo.dto.Usuario.UsuarioSaveDto;
import com.hotel.reservas.reactivo.model.Reserva;
import com.hotel.reservas.reactivo.model.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReservaMapper {
    ReservaDto toDto(Reserva reserva);
    Reserva toEntity(ReservaSaveDto reservaSaveDto);
    Reserva updateEntity(ReservaSaveDto reservaSaveDto, @MappingTarget Reserva reserva);
}

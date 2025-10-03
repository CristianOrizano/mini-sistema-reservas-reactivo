package com.hotel.reservas.reactivo.dto.Habitacion.mapper;

import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionDto;
import com.hotel.reservas.reactivo.dto.Habitacion.HabitacionSaveDto;
import com.hotel.reservas.reactivo.model.Habitacion;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface HabitacionMapper {
    HabitacionDto toDto(Habitacion habitacion);
    Habitacion toEntity(HabitacionSaveDto habitacionSaveDto);
    // Para actualizar una entidad existente con datos del DTO (PATCH / PUT)
    Habitacion updateEntity(HabitacionSaveDto habitacionSaveDto, @MappingTarget Habitacion habitacion);

}

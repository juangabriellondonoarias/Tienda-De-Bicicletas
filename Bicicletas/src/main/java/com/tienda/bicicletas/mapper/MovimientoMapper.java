package com.tienda.bicicletas.mapper;

import com.tienda.bicicletas.dto.request.MovimientoRequestDTO;
import com.tienda.bicicletas.dto.response.MovimientoResponseDTO;
import com.tienda.bicicletas.entity.Movimiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MovimientoMapper {

    @Mapping(target = "idBicicleta", source = "bicicleta.idBicicleta")
    @Mapping(target = "nombreBicicleta", expression = "java(movimiento.getBicicleta().getMarca() + \" \" + movimiento.getBicicleta().getModelo())")
    @Mapping(target = "idUsuario", source = "usuario.idUsuario")
    @Mapping(target = "nombreUsuario", source = "usuario.nombre")
    MovimientoResponseDTO toResponseDTO(Movimiento movimiento);

    @Mapping(target = "idMovimiento", ignore = true)
    @Mapping(target = "bicicleta", ignore = true)
    @Mapping(target = "usuario", ignore = true)
    Movimiento toEntity(MovimientoRequestDTO dto);
}
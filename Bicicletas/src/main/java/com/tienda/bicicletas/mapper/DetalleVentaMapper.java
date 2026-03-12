package com.tienda.bicicletas.mapper;


import com.tienda.bicicletas.dto.request.DetalleVentaRequestDTO;
import com.tienda.bicicletas.dto.response.DetalleVentaResponseDTO;
import com.tienda.bicicletas.entity.DetalleVenta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DetalleVentaMapper {
    // extraemos la info de las relaciones
    @Mapping(source = "venta.idVenta", target = "idVenta")
    @Mapping(source = "bicicleta.modelo", target = "nombreBicicleta")
    DetalleVentaResponseDTO toResponseDTO(DetalleVenta detalleVenta);

    List<DetalleVentaResponseDTO> toResponseDTOList(List<DetalleVenta> detalles);


    @Mapping(target = "idDetalleVenta" , ignore = true)
    @Mapping(target = "venta" , ignore = true)
    @Mapping(target = "bicicleta", ignore = true)
    @Mapping(target = "totalDetalle", ignore = true) // se calcula en servicio
    DetalleVenta toEntity(DetalleVentaRequestDTO rdto);


    @Mapping(target = "idDetalleVenta", ignore = true)
    @Mapping(target = "venta" , ignore = true)
    @Mapping(target = "bicicleta", ignore = true)
    @Mapping(target = "totalDetalle" , ignore = true)
    void updateEntityFromDTO(DetalleVentaRequestDTO dto, @MappingTarget DetalleVenta entidad);
}

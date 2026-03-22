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
    // 1. EL MÉTODO QUE TE FALTA (Este es el que busca el Service)
    @Mapping(source = "venta.idVenta", target = "idVenta")
    @Mapping(source = "bicicleta.modelo", target = "nombreBicicleta")
    DetalleVentaResponseDTO toResponseDTO(DetalleVenta detalleVenta);

    // 2. La lista (usará automáticamente el mapeo de arriba)
    List<DetalleVentaResponseDTO> toResponseDTOList(List<DetalleVenta> detalles);

    // 3. De DTO a Entidad (Para crear)
    @Mapping(target = "idDetalleVenta", ignore = true)
    @Mapping(target = "venta", ignore = true)
    @Mapping(target = "bicicleta", ignore = true)
    @Mapping(target = "totalDetalle", ignore = true)
    @Mapping(target = "cantidad", ignore = true)
    DetalleVenta toEntity(DetalleVentaRequestDTO rdto);

    // 4. Para actualizar
    @Mapping(target = "idDetalleVenta", ignore = true)
    @Mapping(target = "venta", ignore = true)
    @Mapping(target = "bicicleta", ignore = true)
    @Mapping(target = "totalDetalle", ignore = true)
    void updateEntityFromDTO(DetalleVentaRequestDTO dto, @MappingTarget DetalleVenta entidad);
}
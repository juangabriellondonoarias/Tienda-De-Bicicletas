package com.tienda.bicicletas.mapper;



import com.tienda.bicicletas.dto.request.VentaRequestDTO;
import com.tienda.bicicletas.dto.response.VentaResponseDTO;
import com.tienda.bicicletas.entity.Venta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VentaMapper {

    @Mapping(source = "usuario.nombre", target = "nombreCliente")
    VentaResponseDTO toResponseDTO(Venta venta);

    @Mapping(target = "idVenta", ignore = true)
    @Mapping(target = "fecha" , ignore = true)
    @Mapping(target = "usuario" , ignore = true)
    Venta toEntity(VentaRequestDTO dto);

    @Mapping(target = "idVenta" , ignore = true)
    @Mapping(target = "fecha" , ignore = true)
    @Mapping(target = "usuario" , ignore = true)
    void updateEntityFromDTO(VentaRequestDTO dto, @MappingTarget Venta entidad);

    List<VentaResponseDTO> toResponseDTOList(List<Venta> ventas);

}

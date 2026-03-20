package com.tienda.bicicletas.mapper;



import com.tienda.bicicletas.dto.request.VentaRequestDTO;
import com.tienda.bicicletas.dto.response.VentaResponseDTO;
import com.tienda.bicicletas.entity.Venta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = {DetalleVentaMapper.class})
public interface VentaMapper {

    // 1. Mapeo para la Respuesta (Response)
    @Mapping(source = "cliente.nombre", target = "nombreCliente")
    @Mapping(source = "cliente.documento", target = "documentoCliente")
    @Mapping(source = "vendedor.nombre", target = "nombreVendedor")
    VentaResponseDTO toResponseDTO(Venta venta);

    // 2. Mapeo para Crear (Entity)
    // Ignoramos estos campos porque el Service se encarga de llenarlos manualmente
    @Mapping(target = "idVenta", ignore = true)
    @Mapping(target = "fecha" , ignore = true)
    @Mapping(target = "cliente" , ignore = true) // Cambiado de 'usuario' a 'cliente'
    @Mapping(target = "vendedor" , ignore = true) // Nuevo campo a ignorar
    @Mapping(target = "totalVenta", ignore = true)
    Venta toEntity(VentaRequestDTO dto);

    // 3. Mapeo para Actualizar
    @Mapping(target = "idVenta" , ignore = true)
    @Mapping(target = "fecha" , ignore = true)
    @Mapping(target = "cliente" , ignore = true) // Cambiado
    @Mapping(target = "vendedor" , ignore = true) // Nuevo
    @Mapping(target = "totalVenta", ignore = true)
    void updateEntityFromDTO(VentaRequestDTO dto, @MappingTarget Venta entidad);

    List<VentaResponseDTO> toResponseDTOList(List<Venta> ventas);
}

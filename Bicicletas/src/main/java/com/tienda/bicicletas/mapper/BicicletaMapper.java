package com.tienda.bicicletas.mapper;

import com.tienda.bicicletas.dto.request.BicicletaRequestDTO;
import com.tienda.bicicletas.dto.response.BicicletaResponseDTO;
import com.tienda.bicicletas.entity.Bicicleta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BicicletaMapper {

    // De entidad a Response DTO
    BicicletaResponseDTO toResponseDTO(Bicicleta bicicleta);

    @Mapping(target = "idBicicleta", ignore = true) // El ID lo genera la base de datos
    @Mapping(target = "stock", constant = "0")      // El stock inicializa en 0 según tu SQL
    @Mapping(target = "activo", constant = "true")  // Valor por defecto para nuevas bicis
    Bicicleta toEntity(BicicletaRequestDTO dto);

    @Mapping(target = "idBicicleta", ignore = true) // No permitimos cambiar el ID
    @Mapping(target = "stock", ignore = true)       // El stock no se toca en el PUT (se hace vía movimientos)
    @Mapping(target = "activo", ignore = true)      // El estado se suele manejar en otro endpoint
    void updateEntityFromDto(BicicletaRequestDTO dto, @MappingTarget Bicicleta entidad);
}

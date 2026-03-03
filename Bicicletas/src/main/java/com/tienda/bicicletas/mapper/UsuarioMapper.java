package com.tienda.bicicletas.mapper;

import com.tienda.bicicletas.dto.request.UsuarioRequestDTO;
import com.tienda.bicicletas.dto.response.UsuarioResponseDTO;
import com.tienda.bicicletas.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    UsuarioResponseDTO toResposeDTO(Usuario usuario);

    @Mapping(target = "idUsuario", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);

    @Mapping(target = "idUsuario", ignore = true)
    void updateEntityFromDto(UsuarioRequestDTO dto, @MappingTarget Usuario entidad);
}

package com.tienda.bicicletas.mapper;

import com.tienda.bicicletas.dto.request.UsuarioRequestDTO;
import com.tienda.bicicletas.dto.response.UsuarioResponseDTO;
import com.tienda.bicicletas.entity.Usuario;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UsuarioMapper {

    @Mapping(target = "rolNombre", expression = "java(mapearRolNombre(usuario))")
    UsuarioResponseDTO toResposeDTO(Usuario usuario);


    @Mapping(target = "idUsuario", ignore = true)
    @Mapping(target = "roles", ignore = true)
    Usuario toEntity(UsuarioRequestDTO dto);

    @Mapping(target = "idUsuario", ignore = true)
    void updateEntityFromDto(UsuarioRequestDTO dto, @MappingTarget Usuario entidad);

    // Metodo para extraer el nombre del primer rol disponible
    default String mapearRolNombre(Usuario usuario){
        if (usuario.getRoles() == null || usuario.getRoles().isEmpty()){
            return "SIN_ROL";
        }

        // Retornar el nombre del primer rol (ej: "ROL_ADMIN")
        return usuario.getRoles().iterator().next().getNombre();
    }
}

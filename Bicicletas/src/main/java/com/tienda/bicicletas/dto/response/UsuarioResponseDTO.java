package com.tienda.bicicletas.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private Integer idUsuario;
    private String documento;
    private String nombre;
    private String telefono;
    private String email;
}

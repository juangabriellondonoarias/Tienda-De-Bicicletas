package com.tienda.bicicletas.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String type;
    private Integer userId;
    private Integer rolId;
    private String email;
    private String nombre;
    private String documento;
}

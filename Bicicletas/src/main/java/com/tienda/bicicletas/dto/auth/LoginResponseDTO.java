package com.tienda.bicicletas.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String type = "Bearer";
    private Integer userId;
    private Integer rolId;
    private String email;
}

package com.tienda.bicicletas.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor // Agrégale esto para que Jackson trabaje mejor
public class LoginResponseDTO {
    private String token;
    private String type;
    private Integer userId;
    private Integer rolId;
    private String email;
}

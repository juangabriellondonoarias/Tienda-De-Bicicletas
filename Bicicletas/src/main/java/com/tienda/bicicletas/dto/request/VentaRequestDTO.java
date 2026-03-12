package com.tienda.bicicletas.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class VentaRequestDTO {

    private Integer idUsuario;
    private BigDecimal totalVenta;

}

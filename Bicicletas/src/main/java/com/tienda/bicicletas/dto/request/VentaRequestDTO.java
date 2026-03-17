package com.tienda.bicicletas.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class VentaRequestDTO {

    private Integer idUsuario;
    private BigDecimal totalVenta;

    // Aqui mofique
    private List<DetalleVentaRequestDTO> detalles;

}

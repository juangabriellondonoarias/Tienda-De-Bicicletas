package com.tienda.bicicletas.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleVentaResponseDTO {

    private Integer idDetalleVenta;
    private Integer idVenta;
    private String nombreBicicleta; // se extrae el nombre para el fronted
    private Integer cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal totalDetalle;
}

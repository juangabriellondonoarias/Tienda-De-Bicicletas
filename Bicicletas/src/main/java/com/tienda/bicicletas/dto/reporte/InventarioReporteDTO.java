package com.tienda.bicicletas.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InventarioReporteDTO {
    private String codigo;
    private String marca;
    private String modelo;
    private String categoria;
    private Integer stockActual;
    private Integer stockMinimo;
    private BigDecimal precio;
    private BigDecimal valorTotal;
    private String estadoStock; // "Bajo", "Agotado", "Normal"
}

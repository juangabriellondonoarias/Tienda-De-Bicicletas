package com.tienda.bicicletas.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VentaReporteDTO {
    private Integer idVenta;
    private String cliente;
    private String documento;
    private LocalDate fecha;
    private BigDecimal total;
    private String vendedor;
    private String productos; // Resumen de productos vendidos
}

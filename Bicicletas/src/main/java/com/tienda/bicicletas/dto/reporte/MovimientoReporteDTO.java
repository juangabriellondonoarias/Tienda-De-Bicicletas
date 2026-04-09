package com.tienda.bicicletas.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovimientoReporteDTO {
    private Integer idMovimiento;
    private LocalDateTime fecha;
    private String tipo;
    private String bicicleta;
    private Integer cantidad;
    private String proveedor;
    private String usuarioResponsable;
}

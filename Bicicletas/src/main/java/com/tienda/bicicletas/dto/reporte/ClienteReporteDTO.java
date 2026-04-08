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
public class ClienteReporteDTO {
    private Integer idCliente;
    private String nombre;
    private String documento;
    private String email;
    private Integer totalCompras;
    private BigDecimal montoTotalGastado;
    private LocalDate ultimaCompra;
    private String estado; // "Activo", "Inactivo"
}

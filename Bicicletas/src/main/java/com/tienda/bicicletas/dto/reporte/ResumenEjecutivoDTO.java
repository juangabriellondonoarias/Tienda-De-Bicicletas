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
public class ResumenEjecutivoDTO {
    private BigDecimal totalVentas;
    private Long totalClientes;
    private Long totalBicicletas;
    private Long bicicletasBajoStock;
    private BigDecimal valorVentasMesActual;
    private BigDecimal valorInventarioTotal;
}

package com.tienda.bicicletas.dto.response;

import com.tienda.bicicletas.enums.TipoMovimiento;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovimientoResponseDTO {
    private Integer idMovimiento;
    private Integer idBicicleta;
    private String nombreBicicleta;
    private Integer idUsuario;
    private String nombreUsuario;
    private TipoMovimiento tipo;
    private Integer cantidad;
    private LocalDateTime fecha;
    private String proveedor;
}

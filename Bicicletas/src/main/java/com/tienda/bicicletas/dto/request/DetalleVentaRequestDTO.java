package com.tienda.bicicletas.dto.request;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class DetalleVentaRequestDTO {

    private Integer idVenta;
    private Integer idBicicleta;
    private Integer cantidad;
    private BigDecimal precioUnitario;

}

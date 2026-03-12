package com.tienda.bicicletas.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class VentaResponseDTO {

    private Integer idVenta;
    private LocalDate fecha;
    private BigDecimal totalVenta;

    private String nombreCliente;

}

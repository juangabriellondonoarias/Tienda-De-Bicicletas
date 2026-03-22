package com.tienda.bicicletas.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class VentaResponseDTO {

    private Integer idVenta;
    private LocalDate fecha;
    private BigDecimal totalVenta;

    // Datos del Cliente (Lo que pediste)
    private String nombreCliente;
    private String documentoCliente;

    // Datos del Vendedor (Para saber quién atendió)
    private String nombreVendedor;

    private List<DetalleVentaResponseDTO> detalles;

}

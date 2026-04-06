package com.tienda.bicicletas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompraClienteRequestDTO {
    private Integer idUsuario;
    private String documentoCliente;
    private List<DetalleVentaRequestDTO> detalles;
}

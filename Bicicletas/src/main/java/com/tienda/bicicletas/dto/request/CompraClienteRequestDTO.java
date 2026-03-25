package com.tienda.bicicletas.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CompraClienteRequestDTO {
    private String documentoCliente;
    private List<DetalleVentaRequestDTO> detalles;

    // Getters y Setters
    public String getDocumentoCliente() { return documentoCliente; }
    public void setDocumentoCliente(String documentoCliente) { this.documentoCliente = documentoCliente; }

    public List<DetalleVentaRequestDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVentaRequestDTO> detalles) { this.detalles = detalles; }
}

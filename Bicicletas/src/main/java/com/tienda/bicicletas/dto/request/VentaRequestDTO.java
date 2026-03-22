package com.tienda.bicicletas.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


import java.util.List;

@Data
public class VentaRequestDTO {

    // Cambia 'idUsuario' por esto:
    private Integer idVendedor;

    // Agrega este campo (que es el que usaremos para buscar al cliente):
    private String documentoCliente;

    private List<DetalleVentaRequestDTO> detalles;

    // --- RECUERDA: Si no usas @Data de Lombok, debes generar los Getters y Setters ---
    public Integer getIdVendedor() { return idVendedor; }
    public void setIdVendedor(Integer idVendedor) { this.idVendedor = idVendedor; }

    public String getDocumentoCliente() { return documentoCliente; }
    public void setDocumentoCliente(String documentoCliente) { this.documentoCliente = documentoCliente; }

    public List<DetalleVentaRequestDTO> getDetalles() { return detalles; }
    public void setDetalles(List<DetalleVentaRequestDTO> detalles) { this.detalles = detalles; }
}

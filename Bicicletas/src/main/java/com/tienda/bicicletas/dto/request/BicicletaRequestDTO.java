package com.tienda.bicicletas.dto.request;

import com.tienda.bicicletas.enums.TipoBicicleta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BicicletaRequestDTO {
    private Integer codigo;
    private String marca;
    private String modelo;
    private TipoBicicleta tipo;
    private Integer stockMinimo;
    private Integer valorUnitario;
}
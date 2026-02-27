package com.tienda.bicicletas.dto.response;

import com.tienda.bicicletas.enums.TipoBicicleta;
import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BicicletaResponseDTO {
    private Integer idBicicleta;
    private String codigo;
    private String marca;
    private String modelo;
    private TipoBicicleta tipo;
    private Integer stock;
    private Integer stockMinimo;
    private Integer valorUnitario;
    private String activo;
}
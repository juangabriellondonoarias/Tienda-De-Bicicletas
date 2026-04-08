package com.tienda.bicicletas.dto.response;

import com.tienda.bicicletas.enums.TipoBicicleta;
import lombok.*;

import java.math.BigDecimal;

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
    private BigDecimal valorUnitario;
    private String activo;
    private String imagen;
}
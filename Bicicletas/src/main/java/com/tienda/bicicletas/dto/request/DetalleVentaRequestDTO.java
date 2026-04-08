package com.tienda.bicicletas.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Data
@Getter
@Setter
public class DetalleVentaRequestDTO {

    @NotNull(message = "El ID de la bicicleta es obligatorio")
    private Integer idBicicleta;

    @NotNull(message = "La cantidad de bicicleta es obligatorio")
    private Integer cantidad;

}

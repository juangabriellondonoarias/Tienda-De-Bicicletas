package com.tienda.bicicletas.dto.request;

import com.tienda.bicicletas.enums.TipoMovimiento;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
public class MovimientoRequestDTO {

    @NotNull(message = "El ID de la bicicleta es obligatorio")
    private Integer idBicicleta;

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer idUsuario;

    @NotNull(message = "El tipo (entrada/salida) es obligatorio")
    private TipoMovimiento tipo;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;
}

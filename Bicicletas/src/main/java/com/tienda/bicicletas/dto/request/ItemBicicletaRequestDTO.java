package com.tienda.bicicletas.dto.request;

import com.tienda.bicicletas.enums.TipoBicicleta;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ItemBicicletaRequestDTO {

    @NotNull(message = "Es obligatorio el nombre proveedor")
    private String proveedor;

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    private Integer cantidad;

    @NotNull(message = "El ID de la bicicleta es obligatorio")
    private Integer idBicicleta;


}

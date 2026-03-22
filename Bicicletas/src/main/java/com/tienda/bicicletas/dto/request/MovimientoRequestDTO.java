package com.tienda.bicicletas.dto.request;

import com.tienda.bicicletas.enums.TipoMovimiento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
// import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Data
public class MovimientoRequestDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Integer idUsuario;

    @NotNull(message = "El tipo (entrada/salida) es obligatorio")
    private TipoMovimiento tipo;

    @NotNull(message = "La lista de bicicletas no puede estar vacía")
    @Valid
    private List<ItemBicicletaRequestDTO> listaProveedor;
}

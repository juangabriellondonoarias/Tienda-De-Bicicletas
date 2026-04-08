package com.tienda.bicicletas.dto.request;

import com.tienda.bicicletas.enums.TipoBicicleta;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BicicletaRequestDTO {

    @NotBlank(message = "El codigo es obligatorio")
    @Size(min = 3, max = 10, message = "El codigo debe tener entre 3 y 10 caracteres")
    private String codigo;

    @NotBlank(message = "La marca no puede estar vacia")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    private String modelo;

    @NotNull(message = "El tipo de bicicleta es obligatorio")
    private TipoBicicleta tipo;

    @NotNull(message = "El stock minimo es obligatorio")
    @Min(value = 0, message = "El stock minimo no puede ser negativo")
    private Integer stockMinimo;

    @NotNull(message = "El valor unitario es obligatorio")
    @Positive(message = "El precio debe ser mayor a cero(0)")
    private BigDecimal valorUnitario;

    private String imagen;
}
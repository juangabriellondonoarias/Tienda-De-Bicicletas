package com.tienda.bicicletas.dto.reporte;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReporteFiltroDTO {
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String formato; // "PDF" o "EXCEL"
    private String estado;
    private String categoria;
    private Integer limite = 10;
}

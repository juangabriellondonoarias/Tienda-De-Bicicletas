package com.tienda.bicicletas.util;

import com.tienda.bicicletas.dto.reporte.ReporteFiltroDTO;
import java.util.List;

public interface ReporteStrategy {
    byte[] generar(String titulo, String[] columnas, List<Object[]> filas, ReporteFiltroDTO filtro);
    String getFormato();
}

package com.tienda.bicicletas.service;

import com.tienda.bicicletas.dto.reporte.*;
import org.springframework.http.ResponseEntity;
import java.time.LocalDate;
import java.util.List;

public interface ReporteService {
    
    List<VentaReporteDTO> obtenerVentas(ReporteFiltroDTO filtro);
    
    List<InventarioReporteDTO> obtenerInventario(ReporteFiltroDTO filtro);
    
    List<ClienteReporteDTO> obtenerClientesMasCompras();
    
    ResumenEjecutivoDTO obtenerResumenEjecutivo();
    
    ResponseEntity<byte[]> exportarReporte(String tipo, ReporteFiltroDTO filtro);
}

package com.tienda.bicicletas.controller;

import com.tienda.bicicletas.dto.reporte.ReporteFiltroDTO;
import com.tienda.bicicletas.dto.reporte.ResumenEjecutivoDTO;
import com.tienda.bicicletas.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reportes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Ajustar según necesidad de CORS
public class ReporteController {

    private final ReporteService reporteService;

    @GetMapping("/resumen")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ResumenEjecutivoDTO> getResumen() {
        return ResponseEntity.ok(reporteService.obtenerResumenEjecutivo());
    }

    @GetMapping("/exportar/{tipo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> exportar(
            @PathVariable String tipo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin,
            @RequestParam(defaultValue = "PDF") String formato,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String categoria
    ) {
        ReporteFiltroDTO filtro = new ReporteFiltroDTO(fechaInicio, fechaFin, formato, estado, categoria, 10);
        return reporteService.exportarReporte(tipo, filtro);
    }
}

package com.tienda.bicicletas.service.impl;

import com.tienda.bicicletas.dto.reporte.*;
import com.tienda.bicicletas.entity.Bicicleta;
import com.tienda.bicicletas.entity.Venta;
import com.tienda.bicicletas.exception.ReporteGeneracionException;
import com.tienda.bicicletas.repository.BicicletaRepository;
import com.tienda.bicicletas.repository.ReporteRepository;
import com.tienda.bicicletas.repository.UsuarioRepository;
import com.tienda.bicicletas.service.ReporteService;
import com.tienda.bicicletas.util.ReporteFactory;
import com.tienda.bicicletas.util.ReporteStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final ReporteRepository reporteRepository;
    private final BicicletaRepository bicicletaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ReporteFactory reporteFactory;

    @Override
    public List<VentaReporteDTO> obtenerVentas(ReporteFiltroDTO filtro) {
        LocalDate inicio = filtro.getFechaInicio() != null ? filtro.getFechaInicio() : LocalDate.now().minusDays(30);
        LocalDate fin = filtro.getFechaFin() != null ? filtro.getFechaFin() : LocalDate.now();

        return reporteRepository.findVentasByRango(inicio, fin).stream()
                .map(v -> VentaReporteDTO.builder()
                        .idVenta(v.getIdVenta())
                        .cliente(v.getNombreCliente())
                        .documento(v.getDocumentoCliente())
                        .fecha(v.getFecha())
                        .total(v.getTotalVenta())
                        .vendedor(v.getVendedor() != null ? v.getVendedor().getNombre() : "S/N")
                        .productos(v.getDetalles().size() + " Items")
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<InventarioReporteDTO> obtenerInventario(ReporteFiltroDTO filtro) {
        return bicicletaRepository.findAll().stream()
                .map(b -> {
                    BigDecimal valorTotal = b.getValorUnitario().multiply(new BigDecimal(b.getStock()));
                    String estado = b.getStock() <= 0 ? "AGOTADO" : (b.getStock() <= b.getStockMinimo() ? "BAJO" : "NORMAL");
                    return InventarioReporteDTO.builder()
                            .codigo(b.getCodigo())
                            .marca(b.getMarca())
                            .modelo(b.getModelo())
                            .categoria(b.getTipo() != null ? b.getTipo().toString() : "S/N")
                            .stockActual(b.getStock())
                            .stockMinimo(b.getStockMinimo())
                            .precio(b.getValorUnitario())
                            .valorTotal(valorTotal)
                            .estadoStock(estado)
                            .build();
                }).collect(Collectors.toList());
    }

    @Override
    public List<ClienteReporteDTO> obtenerClientesMasCompras() {
        List<Object[]> results = reporteRepository.findClientesMasCompras();
        List<ClienteReporteDTO> dtos = new ArrayList<>();
        for (Object[] res : results) {
            dtos.add(ClienteReporteDTO.builder()
                    .idCliente((Integer) res[0])
                    .nombre((String) res[1])
                    .documento((String) res[2])
                    .email((String) res[3])
                    .totalCompras(((Long) res[4]).intValue())
                    .montoTotalGastado((BigDecimal) res[5])
                    .ultimaCompra((LocalDate) res[6])
                    .estado("ACTIVO")
                    .build());
        }
        return dtos;
    }

    @Override
    public ResumenEjecutivoDTO obtenerResumenEjecutivo() {
        List<Bicicleta> bicis = bicicletaRepository.findAll();
        BigDecimal totalVentas = reporteRepository.findAll().stream()
                .map(Venta::getTotalVenta)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ResumenEjecutivoDTO.builder()
                .totalVentas(totalVentas)
                .totalClientes(usuarioRepository.count())
                .totalBicicletas((long) bicis.size())
                .bicicletasBajoStock(bicis.stream().filter(b -> b.getStock() <= b.getStockMinimo()).count())
                .valorInventarioTotal(bicis.stream()
                        .map(b -> b.getValorUnitario().multiply(new BigDecimal(b.getStock())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add))
                .build();
    }

    @Override
    public ResponseEntity<byte[]> exportarReporte(String tipo, ReporteFiltroDTO filtro) {
        String titulo;
        String[] columnas;
        List<Object[]> filas = new ArrayList<>();

        switch (tipo.toUpperCase()) {
            case "VENTAS":
                titulo = "Reporte de Ventas por Rango";
                columnas = new String[]{"ID", "Fecha", "Cliente", "Documento", "Total", "Vendedor"};
                for (VentaReporteDTO v : obtenerVentas(filtro)) {
                    filas.add(new Object[]{v.getIdVenta(), v.getFecha(), v.getCliente(), v.getDocumento(), v.getTotal(), v.getVendedor()});
                }
                break;
            case "INVENTARIO":
                titulo = "Estado de Inventario General";
                columnas = new String[]{"Código", "Producto", "Cat.", "Stock", "Precio", "Valor Total", "Estado"};
                for (InventarioReporteDTO i : obtenerInventario(filtro)) {
                    filas.add(new Object[]{i.getCodigo(), i.getMarca() + " " + i.getModelo(), i.getCategoria(), i.getStockActual(), i.getPrecio(), i.getValorTotal(), i.getEstadoStock()});
                }
                break;
            case "CLIENTES":
                titulo = "Reporte de Clientes VIP";
                columnas = new String[]{"Nombre", "Documento", "Email", "Total Compras", "Monto Gastado", "Ultima Compra"};
                for (ClienteReporteDTO c : obtenerClientesMasCompras()) {
                    filas.add(new Object[]{c.getNombre(), c.getDocumento(), c.getEmail(), c.getTotalCompras(), c.getMontoTotalGastado(), c.getUltimaCompra()});
                }
                break;
            default:
                throw new ReporteGeneracionException("Tipo de reporte no válido: " + tipo);
        }

        ReporteStrategy strategy = reporteFactory.getStrategy(filtro.getFormato());
        byte[] data = strategy.generar(titulo, columnas, filas, filtro);

        String filename = "Reporte_" + tipo.toLowerCase() + "_" + LocalDate.now();
        MediaType mediaType = filtro.getFormato().equalsIgnoreCase("PDF") ? MediaType.APPLICATION_PDF : MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        String extension = filtro.getFormato().equalsIgnoreCase("PDF") ? ".pdf" : ".xlsx";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentDispositionFormData("attachment", filename + extension);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(data, headers, HttpStatus.OK);
    }
}

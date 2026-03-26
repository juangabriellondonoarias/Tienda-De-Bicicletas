package com.tienda.bicicletas.service;

import com.tienda.bicicletas.dto.request.CompraClienteRequestDTO;
import com.tienda.bicicletas.dto.request.VentaRequestDTO;
import com.tienda.bicicletas.dto.request.DetalleVentaRequestDTO;
import com.tienda.bicicletas.dto.response.VentaResponseDTO;
import com.tienda.bicicletas.entity.*;
import com.tienda.bicicletas.enums.TipoMovimiento;
import com.tienda.bicicletas.exception.ResourceNotFoundException;
import com.tienda.bicicletas.mapper.VentaMapper;
import com.tienda.bicicletas.repository.BicicletaRepository;
import com.tienda.bicicletas.repository.MovimientoRepository;
import com.tienda.bicicletas.repository.UsuarioRepository;
import com.tienda.bicicletas.repository.VentaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final BicicletaRepository bicicletaRepository;
    private final MovimientoRepository movimientoRepository;
    private final VentaMapper ventaMapper;

    public VentaService(VentaRepository ventaRepository,
                        UsuarioRepository usuarioRepository,
                        BicicletaRepository bicicletaRepository,
                        MovimientoRepository movimientoRepository,
                        VentaMapper ventaMapper) {
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
        this.bicicletaRepository = bicicletaRepository;
        this.movimientoRepository = movimientoRepository;
        this.ventaMapper = ventaMapper;
    }

    // --- 1. REGISTRO POR VENDEDOR (Venta Asistida) ---
    @Transactional
    public VentaResponseDTO registrarVenta(VentaRequestDTO request) {
        Usuario vendedor = usuarioRepository.findById(request.getIdVendedor())
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado con ID: " + request.getIdVendedor()));

        return procesarVentaBase(request.getDocumentoCliente(), vendedor, request.getDetalles());
    }

    // --- 2. LÓGICA CENTRALIZADA (Original) ---
    private VentaResponseDTO procesarVentaBase(String documento, Usuario vendedor, List<DetalleVentaRequestDTO> detallesDTO) {
        Usuario cliente = usuarioRepository.findByDocumento(documento)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con documento: " + documento));

        Venta nuevaVenta = new Venta();
        nuevaVenta.setVendedor(vendedor);
        nuevaVenta.setCliente(cliente);
        nuevaVenta.setFecha(LocalDate.now());

        BigDecimal acumuladorTotal = BigDecimal.ZERO;

        for (DetalleVentaRequestDTO detalleDTO : detallesDTO) {
            Bicicleta bicicleta = bicicletaRepository.findById(detalleDTO.getIdBicicleta())
                    .orElseThrow(() -> new ResourceNotFoundException("Bicicleta no encontrada con ID: " + detalleDTO.getIdBicicleta()));

            int cantidadSolicitada = (detalleDTO.getCantidad() != null) ? detalleDTO.getCantidad() : 1;

            // Validación de Stock (Básica)
            int stockActual = (bicicleta.getStock() != null) ? bicicleta.getStock() : 0;
            if (stockActual < cantidadSolicitada) {
                throw new IllegalArgumentException("Stock insuficiente para: " + bicicleta.getModelo());
            }

            // Restar stock
            bicicleta.setStock(stockActual - cantidadSolicitada);
            bicicletaRepository.save(bicicleta);

            // Crear el detalle
            DetalleVenta nuevoDetalle = new DetalleVenta();
            nuevoDetalle.setBicicleta(bicicleta);
            nuevoDetalle.setCantidad(cantidadSolicitada);

            BigDecimal precio = bicicleta.getValorUnitario();
            BigDecimal subtotal = precio.multiply(new BigDecimal(cantidadSolicitada));

            nuevoDetalle.setPrecioUnitario(precio);
            nuevoDetalle.setTotalDetalle(subtotal);

            nuevaVenta.agregarDetalle(nuevoDetalle);
            acumuladorTotal = acumuladorTotal.add(subtotal);
        }

        nuevaVenta.setTotalVenta(acumuladorTotal);
        Venta guardada = ventaRepository.save(nuevaVenta);

        return ventaMapper.toResponseDTO(guardada);
    }

    // --- OTROS MÉTODOS ---

    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerLasVentas() {
        return ventaMapper.toResponseDTOList(ventaRepository.findAll());
    }

    @Transactional(readOnly = true)
    public VentaResponseDTO obtenerPorId(Integer id) {
        Venta venta = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró venta por este id: " + id));
        return ventaMapper.toResponseDTO(venta);
    }

    @Transactional
    public VentaResponseDTO actualizarVenta(Integer id, VentaRequestDTO requestDTO) {
        Venta ventaExistente = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró venta por el id: " + id));

        if (requestDTO.getDocumentoCliente() != null) {
            Usuario nuevoCliente = usuarioRepository.findByDocumento(requestDTO.getDocumentoCliente())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));
            ventaExistente.setCliente(nuevoCliente);
        }

        if (requestDTO.getIdVendedor() != null) {
            Usuario nuevoVendedor = usuarioRepository.findById(requestDTO.getIdVendedor())
                    .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado"));
            ventaExistente.setVendedor(nuevoVendedor);
        }

        return ventaMapper.toResponseDTO(ventaRepository.save(ventaExistente));
    }

    @Transactional
    public void eliminarLaVenta(Integer id) {
        Venta ventaExistente = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con Id: " + id));

        if (ventaExistente.getDetalles() != null) {
            for (DetalleVenta detalle : ventaExistente.getDetalles()) {
                Bicicleta bicicleta = detalle.getBicicleta();
                bicicleta.setStock(bicicleta.getStock() + detalle.getCantidad());
                bicicletaRepository.save(bicicleta);
            }
        }
        ventaRepository.delete(ventaExistente);
    }

}

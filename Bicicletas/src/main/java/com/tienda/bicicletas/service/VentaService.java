package com.tienda.bicicletas.service;

import com.tienda.bicicletas.dto.request.VentaRequestDTO;
import com.tienda.bicicletas.dto.request.DetalleVentaRequestDTO;
import com.tienda.bicicletas.dto.response.VentaResponseDTO;
import com.tienda.bicicletas.entity.Bicicleta;
import com.tienda.bicicletas.entity.DetalleVenta;
import com.tienda.bicicletas.entity.Usuario;
import com.tienda.bicicletas.entity.Venta;
import com.tienda.bicicletas.exception.ResourceNotFoundException;
import com.tienda.bicicletas.mapper.VentaMapper;
import com.tienda.bicicletas.repository.BicicletaRepository;
import com.tienda.bicicletas.repository.UsuarioRepository;
import com.tienda.bicicletas.repository.VentaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final BicicletaRepository bicicletaRepository;
    private final VentaMapper ventaMapper;

    public VentaService(VentaRepository ventaRepository,
                        UsuarioRepository usuarioRepository,
                        BicicletaRepository bicicletaRepository,
                        VentaMapper ventaMapper) {
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
        this.bicicletaRepository = bicicletaRepository;
        this.ventaMapper = ventaMapper;
    }

    @Transactional
    public VentaResponseDTO registrarVenta(VentaRequestDTO request) {

        // 1. Buscamos al Vendedor (Admin) por ID
        Usuario vendedor = usuarioRepository.findById(request.getIdVendedor())
                .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado"));

        // 2. Buscamos al Cliente por su CÉDULA (Documento)
        Usuario cliente = usuarioRepository.findByDocumento(request.getDocumentoCliente())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con cédula: " + request.getDocumentoCliente()));

        Venta nuevaVenta = new Venta();
        nuevaVenta.setVendedor(vendedor);
        nuevaVenta.setCliente(cliente);
        nuevaVenta.setFecha(LocalDate.now());

        BigDecimal acumuladorTotal = BigDecimal.ZERO;

        for (DetalleVentaRequestDTO detalleDTO : request.getDetalles()) {
            Bicicleta bicicleta = bicicletaRepository.findById(detalleDTO.getIdBicicleta())
                    .orElseThrow(() -> new ResourceNotFoundException("Bicicleta no encontrada"));

            // --- Lógica de Stock y Stock Mínimo (Ahora siempre restamos 1) ---
            int stockActual = (bicicleta.getStock() != null) ? bicicleta.getStock() : 0;
            int stockMinimo = (bicicleta.getStockMinimo() != null) ? bicicleta.getStockMinimo() : 0;

            // CAMBIO: Validamos contra 1, no contra detalleDTO.getCantidad()
            if (stockActual < 1) {
                throw new IllegalArgumentException("No hay stock disponible para: " + bicicleta.getModelo());
            }

            int stockFinal = stockActual - 1; // Siempre restamos 1
            if (stockFinal < stockMinimo) {
                throw new IllegalArgumentException("Venta bloqueada: El stock quedaría por debajo del mínimo (" + stockMinimo + ")");
            }

            bicicleta.setStock(stockFinal);
            bicicletaRepository.save(bicicleta);

            // --- Crear Detalle ---
            DetalleVenta nuevoDetalle = new DetalleVenta();
            nuevoDetalle.setBicicleta(bicicleta);
            nuevoDetalle.setCantidad(1); // <--- CAMBIO: Seteamos 1 fijo

            BigDecimal precio = bicicleta.getValorUnitario();
            // El subtotal es igual al precio porque la cantidad es 1
            BigDecimal subtotal = precio;

            nuevoDetalle.setPrecioUnitario(precio);
            nuevoDetalle.setTotalDetalle(subtotal);

            nuevaVenta.agregarDetalle(nuevoDetalle);
            acumuladorTotal = acumuladorTotal.add(subtotal);
        }

        nuevaVenta.setTotalVenta(acumuladorTotal);
        Venta guardada = ventaRepository.save(nuevaVenta);

        return ventaMapper.toResponseDTO(guardada);
    }

    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerLasVentas() {
        List<Venta> ventas = ventaRepository.findAll();
        return ventaMapper.toResponseDTOList(ventas);
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

        // 1. Actualizar Cliente por Documento
        if (requestDTO.getDocumentoCliente() != null) {
            Usuario nuevoCliente = usuarioRepository.findByDocumento(requestDTO.getDocumentoCliente())
                    .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con cédula: " + requestDTO.getDocumentoCliente()));
            ventaExistente.setCliente(nuevoCliente);
        }

        // 2. Actualizar Vendedor por ID
        if (requestDTO.getIdVendedor() != null) {
            Usuario nuevoVendedor = usuarioRepository.findById(requestDTO.getIdVendedor())
                    .orElseThrow(() -> new ResourceNotFoundException("Vendedor no encontrado con ID: " + requestDTO.getIdVendedor()));
            ventaExistente.setVendedor(nuevoVendedor);
        }

        // Guardamos y devolvemos el ResponseDTO con los nombres nuevos
        Venta ventaGuardada = ventaRepository.save(ventaExistente);
        return ventaMapper.toResponseDTO(ventaGuardada);
    }

    @Transactional
    public void eliminarLaVenta(Integer id) {
        Venta ventaExistente = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con Id: " + id));

        // MAGIA DE INVENTARIO: Devolver el stock a la tienda
        if (ventaExistente.getDetalles() != null) {
            for (DetalleVenta detalle : ventaExistente.getDetalles()) {
                Bicicleta bicicleta = detalle.getBicicleta();

                // Sumamos lo que el cliente devuelve al stock actual
                int stockDevuelto = (bicicleta.getStock() != null ? bicicleta.getStock() : 0) + detalle.getCantidad();
                bicicleta.setStock(stockDevuelto);

                bicicletaRepository.save(bicicleta);
            }
        }

        // Borramos la factura y sus detalles (CascadeType.ALL)
        ventaRepository.delete(ventaExistente);
    }
}

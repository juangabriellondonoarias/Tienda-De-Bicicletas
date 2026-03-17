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
        // 1. Validar que el usuario exista
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontró el usuario con el Id: " + request.getIdUsuario()));

        // 2. Mapear DTO a Entidad (MapStruct crea la lista de detalles, pero vacíos de objetos complejos)
        Venta nuevaVenta = ventaMapper.toEntity(request);
        nuevaVenta.setUsuario(usuario);
        nuevaVenta.setFecha(LocalDate.now());

        BigDecimal acumuladorTotalVenta = BigDecimal.ZERO;

        // 3. Procesar cada detalle para cálculos y stock
        if (nuevaVenta.getDetalles() != null && request.getDetalles() != null) {

            for (int i = 0; i < request.getDetalles().size(); i++) {
                DetalleVentaRequestDTO detalleDTO = request.getDetalles().get(i);
                DetalleVenta detalleEntidad = nuevaVenta.getDetalles().get(i);

                // A. Buscar la bicicleta por ID para obtener precio y stock real
                Bicicleta bicicleta = bicicletaRepository.findById(detalleDTO.getIdBicicleta())
                        .orElseThrow(() -> new ResourceNotFoundException("Bicicleta no encontrada con Id: " + detalleDTO.getIdBicicleta()));

                // B. VALIDACIÓN DE STOCK
                if (bicicleta.getStock() < detalleDTO.getCantidad()) {
                    throw new RuntimeException("Stock insuficiente para la bicicleta: " + bicicleta.getModelo() +
                            ". Disponible: " + bicicleta.getStock());
                }

                // C. ACTUALIZAR STOCK (Restar lo vendido)
                bicicleta.setStock(bicicleta.getStock() - detalleDTO.getCantidad());
                bicicletaRepository.save(bicicleta);

                // D. VINCULACIÓN BIDIRECCIONAL (Evita Error 500 y NullPointerException)
                detalleEntidad.setVenta(nuevaVenta); // El hijo conoce al padre
                detalleEntidad.setBicicleta(bicicleta); // El detalle conoce su producto

                // E. CÁLCULO DE TOTALES (Seguridad: Usamos el precio de la DB, no del Front)
                BigDecimal precioReal = bicicleta.getValorUnitario();
                BigDecimal totalDelDetalle = precioReal.multiply(BigDecimal.valueOf(detalleDTO.getCantidad()));

                detalleEntidad.setPrecioUnitario(precioReal);
                detalleEntidad.setTotalDetalle(totalDelDetalle);

                // Sumar al total de la factura
                acumuladorTotalVenta = acumuladorTotalVenta.add(totalDelDetalle);
            }
        }

        // 4. Asignar el total final calculado
        nuevaVenta.setTotalVenta(acumuladorTotalVenta);

        // 5. Guardar la venta (CascadeType.ALL guarda los detalles automáticamente)
        Venta ventaGuardada = ventaRepository.save(nuevaVenta);

        return ventaMapper.toResponseDTO(ventaGuardada);
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

        if (requestDTO.getIdUsuario() != null) {
            Usuario nuevoUsuario = usuarioRepository.findById(requestDTO.getIdUsuario())
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con el id: " + requestDTO.getIdUsuario()));
            ventaExistente.setUsuario(nuevoUsuario);
        }

        ventaMapper.updateEntityFromDTO(requestDTO, ventaExistente);
        return ventaMapper.toResponseDTO(ventaRepository.save(ventaExistente));
    }

    @Transactional
    public void eliminarLaVenta(Integer id) {
        Venta ventaExistente = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con Id: " + id));
        ventaRepository.delete(ventaExistente);
    }
}
package com.tienda.bicicletas.service;


import com.tienda.bicicletas.dto.request.DetalleVentaRequestDTO;
import com.tienda.bicicletas.dto.response.DetalleVentaResponseDTO;
import com.tienda.bicicletas.entity.Bicicleta;
import com.tienda.bicicletas.entity.DetalleVenta;
import com.tienda.bicicletas.entity.Venta;
import com.tienda.bicicletas.exception.ResourceNotFoundException;
import com.tienda.bicicletas.mapper.DetalleVentaMapper;
import com.tienda.bicicletas.repository.BicicletaRepository;
import com.tienda.bicicletas.repository.DetalleVentaRepository;
import com.tienda.bicicletas.repository.VentaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@AllArgsConstructor
@Service
public class DetalleVentaService {

    private final DetalleVentaRepository detalleRepository;
    private final VentaRepository ventaRepository;
    private final BicicletaRepository bicicletaRepository;
    private final DetalleVentaMapper mapper;

    // para crear
    @Transactional
    public DetalleVentaResponseDTO registrarDetalle(Integer idVenta, DetalleVentaRequestDTO requestDTO){

        // 1. Buscamos la Venta
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con el Id:  " + idVenta));

        // Buscamos la Bicicleta
        Bicicleta bicicleta = bicicletaRepository.findById(requestDTO.getIdBicicleta())
                .orElseThrow(() -> new ResourceNotFoundException("Bicicleta no encontrada con Id: " + requestDTO.getIdBicicleta()));

        // ==========================================
        // 2. REGLAS DE NEGOCIO (Ahora la cantidad es 1)
        // ==========================================
        if (bicicleta.getStock() < 1) {
            throw new IllegalArgumentException("Stock insuficiente. No quedan unidades de esta bicicleta.");
        }

        // Restamos 1 unidad del stock
        bicicleta.setStock(bicicleta.getStock() - 1);
        bicicletaRepository.save(bicicleta);

        // ==========================================
        // 3. CÁLCULOS MATEMÁTICOS SEGUROS
        // ==========================================
        BigDecimal precioReal = bicicleta.getValorUnitario();
        // Como cantidad es 1, el total del detalle es igual al precio unitario
        BigDecimal totalDelDetalle = precioReal;

        // ==========================================
        // 4. CREAMOS EL DETALLE
        // ==========================================
        DetalleVenta nuevoDetalle = mapper.toEntity(requestDTO);
        nuevoDetalle.setBicicleta(bicicleta);
        nuevoDetalle.setVenta(venta);
        nuevoDetalle.setCantidad(1); // <-- ASIGNAMOS 1 MANUALMENTE
        nuevoDetalle.setPrecioUnitario(precioReal);
        nuevoDetalle.setTotalDetalle(totalDelDetalle);

        // ==========================================
        // 5. ACTUALIZAMOS EL TOTAL DE LA FACTURA PADRE
        // ==========================================
        BigDecimal totalVentaActual = venta.getTotalVenta() != null ? venta.getTotalVenta() : BigDecimal.ZERO;
        venta.setTotalVenta(totalVentaActual.add(totalDelDetalle));

        ventaRepository.save(venta);

        // Guardamos y devolvemos el ResponseDTO (que ya tiene los nombres de cliente/vendedor)
        return mapper.toResponseDTO(detalleRepository.save(nuevoDetalle));
    }
    //leer todo
    @Transactional(readOnly = true)
    public List<DetalleVentaResponseDTO> obtenerTodo(){
        return mapper.toResponseDTOList(detalleRepository.findAll());
    }

    //leer uno especifico
    @Transactional(readOnly = true)
    public DetalleVentaResponseDTO obtenerPorId(Integer id){
        DetalleVenta detalle = detalleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("no se encontro detalle con Id: " + id ));
        return mapper.toResponseDTO(detalle);
    }

    //actualizar
    @Transactional
    public DetalleVentaResponseDTO actualizarDetalle(Integer id, DetalleVentaRequestDTO requestDTO) {

        // 1. Buscamos el detalle existente
        DetalleVenta detalleExistente = detalleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle no encontrado con Id: " + id));

        Venta venta = detalleExistente.getVenta();
        Bicicleta bicicletaAntigua = detalleExistente.getBicicleta();
        BigDecimal totalAntiguoDetalle = detalleExistente.getTotalDetalle();

        // 2. Buscamos la NUEVA bicicleta que quieren poner en este lugar
        Bicicleta bicicletaNueva = bicicletaRepository.findById(requestDTO.getIdBicicleta())
                .orElseThrow(() -> new ResourceNotFoundException("Bicicleta no encontrada con Id: " + requestDTO.getIdBicicleta()));

        // 3. REGLA DE INVENTARIO: Devolvemos la vieja y restamos la nueva
        // Devolvemos al stock la que ya no se va a llevar
        bicicletaAntigua.setStock(bicicletaAntigua.getStock() + 1);
        bicicletaRepository.save(bicicletaAntigua);

        // Verificamos si hay de la nueva
        if (bicicletaNueva.getStock() < 1) {
            throw new IllegalArgumentException("No hay stock disponible para la nueva bicicleta: " + bicicletaNueva.getModelo());
        }

        // Restamos 1 de la nueva
        bicicletaNueva.setStock(bicicletaNueva.getStock() - 1);
        bicicletaRepository.save(bicicletaNueva);

        // 4. ACTUALIZAMOS EL DETALLE
        BigDecimal nuevoPrecio = bicicletaNueva.getValorUnitario();
        detalleExistente.setBicicleta(bicicletaNueva);
        detalleExistente.setPrecioUnitario(nuevoPrecio);
        detalleExistente.setTotalDetalle(nuevoPrecio); // Cantidad siempre es 1
        detalleExistente.setCantidad(1);

        // 5. AJUSTAMOS EL TOTAL DE LA VENTA PADRE
        // Restamos el valor viejo y sumamos el nuevo
        BigDecimal totalVentaActual = venta.getTotalVenta() != null ? venta.getTotalVenta() : BigDecimal.ZERO;
        BigDecimal totalVentaLimpio = totalVentaActual.subtract(totalAntiguoDetalle);
        venta.setTotalVenta(totalVentaLimpio.add(nuevoPrecio));

        ventaRepository.save(venta);

        return mapper.toResponseDTO(detalleRepository.save(detalleExistente));
    }
    //eliminar
    @Transactional
    public void eliminarDetalle(Integer id){
        DetalleVenta detalle = detalleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle no encontrado con Id: " + id));
        detalleRepository.delete(detalle);
    }
}

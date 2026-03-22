package com.tienda.bicicletas.service;

//import com.tienda.bicicletas.dto.request.CompradoLoteRequestDTO;
import com.tienda.bicicletas.dto.request.ItemBicicletaRequestDTO;
import com.tienda.bicicletas.dto.request.MovimientoRequestDTO;
import com.tienda.bicicletas.dto.response.MovimientoResponseDTO;
import com.tienda.bicicletas.entity.Bicicleta;
import com.tienda.bicicletas.entity.Movimiento;
import com.tienda.bicicletas.entity.Usuario;
//import com.tienda.bicicletas.enums.TipoMovimiento;
import com.tienda.bicicletas.enums.TipoMovimiento;
import com.tienda.bicicletas.exception.ResourceNotFoundException;
import com.tienda.bicicletas.mapper.MovimientoMapper;
import com.tienda.bicicletas.repository.BicicletaRepository;
import com.tienda.bicicletas.repository.MovimientoRepository;
import com.tienda.bicicletas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovimientoService {

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private BicicletaRepository bicicletaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MovimientoMapper movimientoMapper;

    /**
     * Registra un nuevo movimiento de entrada o salida.
     * Al guardar, el trigger 'trg_actualizar_stock' en MySQL actualizará el stock de la bicicleta.
     */
   /* @Transactional
    public MovimientoResponseDTO registrar(MovimientoRequestDTO request) {
        Bicicleta bici = bicicletaRepository.findById(request.getIdBicicleta())
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada"));

        Usuario user = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (request.getTipo() == TipoMovimiento.salida && bici.getStock() < request.getCantidad()) {
            throw new RuntimeException("Stock insuficiente");
        }

        Movimiento movimiento = movimientoMapper.toEntity(request);
        movimiento.setBicicleta(bici);
        movimiento.setUsuario(user);

        Movimiento guardado = movimientoRepository.save(movimiento);

        return movimientoMapper.toResponseDTO(guardado);
    }*/

    /**
     * Lista todo el historial de movimientos registrados.
     */
    public List<MovimientoResponseDTO> listarTodos() {
        return movimientoRepository.findAll().stream()
                .map(movimientoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public List<Object[]> consultarProveedor(String proveedor) {
        // Llamamos al nuevo método del repositorio que usa GROUP BY
        List<Object[]> reporte = movimientoRepository.reporteCantidadesPorTipo(proveedor);

        return reporte;
    }

    @Transactional
    public void registrar(MovimientoRequestDTO request) {

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        for (ItemBicicletaRequestDTO item : request.getListaProveedor()) {

            Bicicleta bicicleta = bicicletaRepository.findById(item.getIdBicicleta())
                    .orElseThrow(() -> new ResourceNotFoundException("Bicicleta no encontrada con ID: " + item.getIdBicicleta()));

            // ==========================================
            // NUEVA LÓGICA: ACTUALIZACIÓN DE STOCK
            // ==========================================
            // 1. Obtenemos el stock actual (por si está en null, lo tomamos como 0)
            int stockActual = (bicicleta.getStock() != null) ? bicicleta.getStock() : 0;

            // 2. Sumamos o restamos dependiendo del tipo de movimiento
            if (request.getTipo() == TipoMovimiento.entrada) {
                bicicleta.setStock(stockActual + item.getCantidad());

            } else if (request.getTipo() == TipoMovimiento.salida) {
                // Validación extra de arquitecto: ¡No podemos vender lo que no tenemos!
                if (stockActual < item.getCantidad()) {
                    throw new IllegalArgumentException("No hay suficiente stock para la bicicleta ID: " + item.getIdBicicleta() + ". Stock actual: " + stockActual);
                }
                bicicleta.setStock(stockActual - item.getCantidad());
            }

            // 3. Guardamos la bicicleta con su nuevo stock
            bicicletaRepository.save(bicicleta);
            // ==========================================

            // El código de tu movimiento sigue exactamente igual
            Movimiento nuevoMovimiento = new Movimiento();
            nuevoMovimiento.setUsuario(usuario);
            nuevoMovimiento.setTipo(request.getTipo());
            nuevoMovimiento.setBicicleta(bicicleta);
            nuevoMovimiento.setProveedor(item.getProveedor());
            nuevoMovimiento.setCantidad(item.getCantidad());

            movimientoRepository.save(nuevoMovimiento);
        }
    }
}
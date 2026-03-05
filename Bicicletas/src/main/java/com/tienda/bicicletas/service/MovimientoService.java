package com.tienda.bicicletas.service;

import com.tienda.bicicletas.dto.request.MovimientoRequestDTO;
import com.tienda.bicicletas.dto.response.MovimientoResponseDTO;
import com.tienda.bicicletas.entity.Bicicleta;
import com.tienda.bicicletas.entity.Movimiento;
import com.tienda.bicicletas.entity.Usuario;
import com.tienda.bicicletas.enums.TipoMovimiento;
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
    @Transactional
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
    }

    /**
     * Lista todo el historial de movimientos registrados.
     */
    public List<MovimientoResponseDTO> listarTodos() {
        return movimientoRepository.findAll().stream()
                .map(movimientoMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
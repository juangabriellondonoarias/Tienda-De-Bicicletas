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
    public DetalleVentaResponseDTO registrarDetalle(DetalleVentaRequestDTO requestDTO){
        Venta venta = ventaRepository.findById(requestDTO.getIdVenta())
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con el Id:  " + requestDTO.getIdVenta()));

        Bicicleta bicicleta = bicicletaRepository.findById(requestDTO.getIdBicicleta())
                .orElseThrow(() -> new ResourceNotFoundException("Bicicleta no encontyrada con Id: " + requestDTO.getIdBicicleta()));

        DetalleVenta nuevoDetalle = mapper.toEntity(requestDTO);

        nuevoDetalle.setBicicleta(bicicleta);
        nuevoDetalle.setVenta(venta);
        nuevoDetalle.setBicicleta(bicicleta);

        // calcular el total
        BigDecimal total = requestDTO.getPrecioUnitario().multiply(new BigDecimal(requestDTO.getCantidad()));
        nuevoDetalle.setTotalDetalle(total);
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
    public DetalleVentaResponseDTO actualizarDetalle (Integer id, DetalleVentaRequestDTO requestDTO){
        DetalleVenta detalleExistente = detalleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Detalle no encontrado con Id:" + requestDTO.getIdVenta()));

        if (requestDTO.getIdVenta() != null){
            Venta venta = ventaRepository.findById(requestDTO.getIdVenta())
                    .orElseThrow(() -> new ResourceNotFoundException("Detalle no encontrado con Id; " + requestDTO.getIdVenta()));
            detalleExistente.setVenta(venta);
        }

        if (requestDTO.getIdBicicleta() != null){
            Bicicleta bicicleta = bicicletaRepository.findById(requestDTO.getIdBicicleta())
                    .orElseThrow(() -> new ResourceNotFoundException("Bicicleta no se encontro con Id: " + requestDTO.getIdBicicleta()));
                        detalleExistente.setBicicleta(bicicleta);
        }

        mapper.updateEntityFromDTO(requestDTO, detalleExistente);

        // recalcular el total si cambio la cantidad o precio
        BigDecimal total = detalleExistente.getPrecioUnitario().multiply(new BigDecimal(detalleExistente.getCantidad()));
            detalleExistente.setTotalDetalle(total);

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

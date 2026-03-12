package com.tienda.bicicletas.service;


import com.tienda.bicicletas.dto.request.VentaRequestDTO;
import com.tienda.bicicletas.dto.response.VentaResponseDTO;
import com.tienda.bicicletas.entity.Usuario;
import com.tienda.bicicletas.entity.Venta;
import com.tienda.bicicletas.exception.ResourceNotFoundException;
import com.tienda.bicicletas.mapper.VentaMapper;
import com.tienda.bicicletas.repository.UsuarioRepository;
import com.tienda.bicicletas.repository.VentaRepository;
//import jakarta.transaction.Transactional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


@Service
public class VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final VentaMapper ventaMapper;

    public VentaService(VentaRepository ventaRepository,
                        UsuarioRepository usuarioRepository, VentaMapper ventaMapper) {
        this.ventaRepository = ventaRepository;
        this.usuarioRepository = usuarioRepository;
        this.ventaMapper = ventaMapper;
    }

    @Transactional
    public VentaResponseDTO registrarVenta(VentaRequestDTO request){
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new ResourceNotFoundException("No se encontro el usuario con el Id: "
                        + request.getIdUsuario()));

        Venta nuevaVenta = ventaMapper.toEntity(request);

        nuevaVenta.setUsuario(usuario);
        nuevaVenta.setFecha(LocalDate.now());

        Venta ventaGuardada = ventaRepository.save(nuevaVenta);

        return ventaMapper.toResponseDTO(ventaGuardada);
    }

    //leer todo
    @Transactional(readOnly = true)
    public List<VentaResponseDTO> obtenerLasVentas(){
        List<Venta> ventas = ventaRepository.findAll();
        return ventaMapper.toResponseDTOList(ventas);
    }

    //para leer
    @Transactional(readOnly = true)
    public VentaResponseDTO obtenerPorId(Integer id){
        Venta venta = ventaRepository.findById(id).orElseThrow(() -> new
                ResourceNotFoundException("No se encontro venta por este id:" + id));
        return ventaMapper.toResponseDTO(venta);
    }

    //actualizar
    @Transactional
    public VentaResponseDTO actualizarVenta (Integer id, VentaRequestDTO requestDTO){
        Venta ventaExistente = ventaRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("No se encontro venta por el id: " + id));


        // si el request trae cambio de usurio lo validamos y se cambia

        if (requestDTO.getIdUsuario() != null){
            Usuario nuevoUsuario = usuarioRepository.findById(requestDTO.getIdUsuario())
                    .orElseThrow(() -> new ResourceNotFoundException("usuario no se encontro con el id: " +
                            requestDTO.getIdUsuario()));
            ventaExistente.setUsuario(nuevoUsuario);
        }

        //actualizamos los demas datos en el mapper
        ventaMapper.updateEntityFromDTO(requestDTO , ventaExistente);

        //se guardan y se retornan
        return ventaMapper.toResponseDTO(ventaRepository.save(ventaExistente));
    }

    // para eliminar

    @Transactional
    public void eliminarLaVenta(Integer id){
        // verificamos que exista antes de eliminarla
        Venta ventaExistente = ventaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con Id: " + id));
            ventaRepository.delete(ventaExistente);

    }

}

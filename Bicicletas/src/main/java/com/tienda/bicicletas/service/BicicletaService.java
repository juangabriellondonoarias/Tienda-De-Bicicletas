package com.tienda.bicicletas.service;

import com.tienda.bicicletas.dto.request.BicicletaRequestDTO;
import com.tienda.bicicletas.dto.response.BicicletaResponseDTO;
import com.tienda.bicicletas.entity.Bicicleta;
import com.tienda.bicicletas.mapper.BicicletaMapper;
import com.tienda.bicicletas.repository.BicicletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BicicletaService {

    @Autowired
    private BicicletaRepository bicicletaRepository;

    @Autowired
    private BicicletaMapper bicicletaMapper;

    public List<BicicletaResponseDTO> listarTodas() {
        return bicicletaRepository.findAll().stream()
                .map(bicicletaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<BicicletaResponseDTO> buscarPorId(Integer id) {
        return bicicletaRepository.findById(id)
                .map(bicicletaMapper::toResponseDTO);
    }

    public BicicletaResponseDTO guardar(BicicletaRequestDTO request) {
        Bicicleta entidad = bicicletaMapper.toEntity(request);
        return bicicletaMapper.toResponseDTO(bicicletaRepository.save(entidad));
    }

    public void eliminar(Integer id) {
        if (bicicletaRepository.existsById(id)) {
            bicicletaRepository.deleteById(id);
        }
    }

    public Optional<BicicletaResponseDTO> actualizar(Integer id, BicicletaRequestDTO request) {
        return bicicletaRepository.findById(id).map(existente -> {
            // Aquí usas el Mapper para actualizar solo lo que viene en el DTO
            bicicletaMapper.updateEntityFromDto(request, existente);
            return bicicletaMapper.toResponseDTO(bicicletaRepository.save(existente));
        });
    }
}
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
import org.springframework.transaction.annotation.Transactional;

@Service
public class BicicletaService {

    @Autowired
    private BicicletaRepository bicicletaRepository;

    @Autowired
    private BicicletaMapper bicicletaMapper;

    public List<BicicletaResponseDTO> listarTodas() {
        return bicicletaRepository.findByActivo("true").stream()
                .map(bicicletaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    public Optional<BicicletaResponseDTO> buscarPorId(Integer id) {
        return bicicletaRepository.findById(id)
                .map(bicicletaMapper::toResponseDTO);
    }

    /*public BicicletaResponseDTO guardar(BicicletaRequestDTO request) {
        Bicicleta entidad = bicicletaMapper.toEntity(request);
        return bicicletaMapper.toResponseDTO(bicicletaRepository.save(entidad));
    }*/

    @Transactional
    public Bicicleta registrarUnaSolaBicicleta(BicicletaRequestDTO dto) {
        // Creamos la entidad vacía
        Bicicleta nuevaBici = new Bicicleta();

        // La llenamos con los datos del DTO único
        nuevaBici.setCodigo(dto.getCodigo());
        nuevaBici.setMarca(dto.getMarca());
        nuevaBici.setModelo(dto.getModelo());
        nuevaBici.setTipo(dto.getTipo());
        nuevaBici.setStockMinimo(dto.getStockMinimo());
        nuevaBici.setValorUnitario(dto.getValorUnitario());
        nuevaBici.setActivo("true"); // Asegúrate de marcarla como activa si es necesario

        // Guardamos en la base de datos
        return bicicletaRepository.save(nuevaBici);
    }

    public void eliminar(Integer id) {
        bicicletaRepository.findById(id).ifPresent(bicicleta -> {
            bicicleta.setActivo("false");
            bicicletaRepository.save(bicicleta);
        });
    }

    public Optional<BicicletaResponseDTO> actualizar(Integer id, BicicletaRequestDTO request) {
        // Cambiamos findById por findByIdAndActivo para ignorar las borradas
        return bicicletaRepository.findByIdBicicletaAndActivo(id, "true").map(existente -> {
            // MapStruct actualiza los campos permitidos
            bicicletaMapper.updateEntityFromDto(request, existente);
            return bicicletaMapper.toResponseDTO(bicicletaRepository.save(existente));
        });
    }
}
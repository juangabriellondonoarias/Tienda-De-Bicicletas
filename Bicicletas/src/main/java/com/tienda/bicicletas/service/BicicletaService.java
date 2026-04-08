package com.tienda.bicicletas.service;

import com.tienda.bicicletas.dto.request.BicicletaRequestDTO;
import com.tienda.bicicletas.dto.response.BicicletaResponseDTO;
import com.tienda.bicicletas.entity.Bicicleta;
import com.tienda.bicicletas.enums.TipoBicicleta;
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

    @Transactional
    public Bicicleta registrarUnaSolaBicicleta(BicicletaRequestDTO dto) {
        Bicicleta nuevaBici = new Bicicleta();

        nuevaBici.setCodigo(dto.getCodigo());
        nuevaBici.setMarca(dto.getMarca());
        nuevaBici.setModelo(dto.getModelo());

        // Aseguramos que el tipo no sea null
        if (dto.getTipo() != null) {
            nuevaBici.setTipo(dto.getTipo());
        }

        // OJO: Agrega el stock inicial si lo mandas desde el front
        // Como tu DTO no tiene 'stock', puedes usar stockMinimo o agregarlo al DTO
        nuevaBici.setStock(0); // O el valor que desees por defecto

        nuevaBici.setStockMinimo(dto.getStockMinimo() != null ? dto.getStockMinimo() : 5);
        nuevaBici.setValorUnitario(dto.getValorUnitario());
        nuevaBici.setActivo("true");
        nuevaBici.setImagen(dto.getImagen());

        return bicicletaRepository.save(nuevaBici);
    }

    public void eliminar(Integer id) {
        bicicletaRepository.findById(id).ifPresent(bicicleta -> {
            bicicleta.setActivo("false");
            bicicletaRepository.save(bicicleta);
        });
    }

    public Optional<BicicletaResponseDTO> actualizar(Integer id, BicicletaRequestDTO request) {
        return bicicletaRepository.findByIdBicicletaAndActivo(id, "true").map(existente -> {
            bicicletaMapper.updateEntityFromDto(request, existente);

            // Si actualizas y mandas una foto nueva, se guarda aquí
            if (request.getImagen() != null) {
                existente.setImagen(request.getImagen());
            }

            return bicicletaMapper.toResponseDTO(bicicletaRepository.save(existente));
        });
    }
}
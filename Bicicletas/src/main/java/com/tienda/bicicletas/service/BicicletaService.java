package com.tienda.bicicletas.service;

import com.tienda.bicicletas.entity.Bicicleta;
import com.tienda.bicicletas.repository.BicicletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class BicicletaService {

    @Autowired
    private BicicletaRepository bicicletaRepository;

    public  List<Bicicleta> listarTodas(){
        return bicicletaRepository.findAll();
    }

    public Optional<Bicicleta> buscarPorId(Integer id){
        return  bicicletaRepository.findById(id);
    }

    public Bicicleta guardar(Bicicleta bicicleta){
        return bicicletaRepository.save(bicicleta);
    }

    public void eliminar(Integer id){
        bicicletaRepository.deleteById(id);
    }

    public Bicicleta actualizar(Integer id, Bicicleta bicicletaActualizada){
        if (bicicletaRepository.existsById(id)){
            bicicletaActualizada.setIdBicicleta(id);
            return bicicletaRepository.save(bicicletaActualizada);
        }
        return null;
    }
}

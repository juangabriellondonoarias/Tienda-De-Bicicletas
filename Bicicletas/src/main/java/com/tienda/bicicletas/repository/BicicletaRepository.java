package com.tienda.bicicletas.repository;

import com.tienda.bicicletas.entity.Bicicleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BicicletaRepository extends JpaRepository<Bicicleta, Integer> {
    // Solo devuelve las bicicletas que tengan activo = "true";
    List<Bicicleta> findByActivo(String activo);

    // Busca una bicicleta por id pero solo si su estado es 'true'
    Optional<Bicicleta> findByIdBicicletaAndActivo(Integer idBicicleta, String activo);
}

package com.tienda.bicicletas.repository;

import com.tienda.bicicletas.entity.Bicicleta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BicicletaRepository extends JpaRepository<Bicicleta, Integer> {
}

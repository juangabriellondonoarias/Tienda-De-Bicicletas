package com.tienda.bicicletas.repository;

import com.tienda.bicicletas.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VentaRepository  extends JpaRepository<Venta, Integer>{
    boolean existsByClienteIdUsuario(Integer idUsuario);
}

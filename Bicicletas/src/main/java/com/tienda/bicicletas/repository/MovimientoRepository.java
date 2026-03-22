package com.tienda.bicicletas.repository;

import com.tienda.bicicletas.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {

    @Query("SELECT m.bicicleta.tipo, SUM(m.cantidad) " +
            "FROM Movimiento m " +
            "WHERE m.proveedor = :nombreProveedor " +
            "AND m.tipo = 'entrada' " +
            "GROUP BY m.bicicleta.tipo")
    List<Object[]> reporteCantidadesPorTipo(@Param("nombreProveedor") String nombreProveedor);
}

package com.tienda.bicicletas.repository;

import com.tienda.bicicletas.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {


        @Query("SELECT SUM(m.cantidad) FROM Movimiento m WHERE m.proveedor = :nombreProveedor" +
                " AND m.bicicleta.modelo = :modelo AND m.tipo = 'ENTRADA'")
        Integer cantidadProveedor(@Param("nombreProveedor") String nombreProveedor,
                                                   @Param("modelo") String modelo);



}

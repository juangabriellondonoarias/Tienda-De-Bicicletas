package com.tienda.bicicletas.repository;

import com.tienda.bicicletas.entity.Venta;
import com.tienda.bicicletas.dto.reporte.VentaReporteDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReporteRepository extends JpaRepository<Venta, Integer> {

    @Query("SELECT v FROM Venta v WHERE v.fecha BETWEEN :inicio AND :fin ORDER BY v.fecha DESC")
    List<Venta> findVentasByRango(@Param("inicio") LocalDate inicio, @Param("fin") LocalDate fin);

    @Query("SELECT v.cliente.idUsuario, v.cliente.nombre, v.cliente.documento, v.cliente.email, COUNT(v), SUM(v.totalVenta), MAX(v.fecha) " +
           "FROM Venta v GROUP BY v.cliente.idUsuario, v.cliente.nombre, v.cliente.documento, v.cliente.email " +
           "ORDER BY SUM(v.totalVenta) DESC")
    List<Object[]> findClientesMasCompras();

    @Query("SELECT b.idBicicleta, b.marca, b.modelo, SUM(d.cantidad) " +
           "FROM DetalleVenta d JOIN d.bicicleta b " +
           "GROUP BY b.idBicicleta, b.marca, b.modelo " +
           "ORDER BY SUM(d.cantidad) DESC")
    List<Object[]> findTopBicicletasMasVendidas();
}

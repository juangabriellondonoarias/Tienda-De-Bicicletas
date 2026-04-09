package com.tienda.bicicletas.repository;

import com.tienda.bicicletas.entity.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {
    @Query(value = "SELECT tipo, SUM(cantidad) FROM movimientos WHERE tipo = :tipo GROUP BY tipo", nativeQuery = true)
    List<Object[]> reporteCantidadesPorTipo(@Param("tipo") String tipo);

    boolean existsByUsuarioIdUsuario(Integer idUsuario);

    List<Movimiento> findByFechaBetween(java.time.LocalDateTime start, java.time.LocalDateTime end);
}

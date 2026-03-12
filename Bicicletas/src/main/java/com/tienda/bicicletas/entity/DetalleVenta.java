package com.tienda.bicicletas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Entity
@Table(name = "detalle_venta")
@Data
@AllArgsConstructor
@NoArgsConstructor

public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idDetalleVenta;

    //relacion con venta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_venta", referencedColumnName = "id_venta" , nullable = false)
    private Venta venta;

    // la relacion con bicicleta
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_bicicleta", referencedColumnName = "id_bicicleta", nullable = false)
    private Bicicleta bicicleta;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_unitario")
    private BigDecimal precioUnitario;

    @Column(name = "total_detalle")
    private BigDecimal totalDetalle;

}

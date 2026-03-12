package com.tienda.bicicletas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;


@Table(name = "venta")
@Entity
@Data
@NoArgsConstructor // constructo vacio
@AllArgsConstructor // contructor de los atributos
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    @Column(name = "id_venta")
    private Integer idVenta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name= "fecha")
    @CreationTimestamp
    private LocalDate fecha;

    @Column(name = "total_venta")
    private BigDecimal totalVenta;

    /*@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Id_usuario", nullable = false)
    private Usuario usuario;*/



}

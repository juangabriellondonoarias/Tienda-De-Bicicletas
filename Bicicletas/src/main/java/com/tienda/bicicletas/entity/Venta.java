package com.tienda.bicicletas.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


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

    // Voy a modificar aqui, pero esto no es mio
    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles;

    public void agregarDetalle(DetalleVenta detalle){
        if (detalles == null){
            detalles = new java.util.ArrayList<>();
        }
        detalles.add(detalle);
        detalle.setVenta(this);
    }

}

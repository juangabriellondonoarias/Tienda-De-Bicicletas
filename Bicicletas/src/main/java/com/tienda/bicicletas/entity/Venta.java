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

    @Column(name = "nombre_cliente", length = 100)
    private String nombreCliente;

    @Column(name = "documento_cliente", length = 20)
    private String documentoCliente;

    @Column(name = "email_cliente", length = 100)
    private String email;

    // --- CAMBIO AQUÍ: El que compra ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente", referencedColumnName = "id_usuario", nullable = false)
    private Usuario cliente;

    // --- CAMBIO AQUÍ: El Admin que vende ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_vendedor", referencedColumnName = "id_usuario", nullable = false)
    private Usuario vendedor;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "total_venta")
    private BigDecimal totalVenta;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleVenta> detalles;

    // Método de utilidad para vincular padre e hijos
    public void agregarDetalle(DetalleVenta detalle){
        if (detalles == null){
            detalles = new java.util.ArrayList<>();
        }
        detalles.add(detalle);
        detalle.setVenta(this);
    }

}

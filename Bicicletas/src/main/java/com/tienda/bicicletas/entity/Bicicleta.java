package com.tienda.bicicletas.entity;

import com.tienda.bicicletas.enums.TipoBicicleta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "bicicletas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bicicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bicicleta")

    private Integer idBicicleta;
    private String codigo;
    private String marca;
    private String modelo;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('Montaña', 'Ruta', 'Urbana')")
    private TipoBicicleta tipo;

    private Integer stock;

    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    @Column(name = "valor_unitario")
    private BigDecimal valorUnitario;

    private String activo = "true";

    @Lob
    @Column(name = "imagen", columnDefinition = "LONGTEXT")
    private String imagen; // Ahora es un String para mayor facilidad en Angular
}

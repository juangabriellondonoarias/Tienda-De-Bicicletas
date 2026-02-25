package com.tienda.bicicletas.entity;

import com.tienda.bicicletas.enums.TipoBicicleta;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bicicleta")
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
    private Integer valorUnitario;

    private String activo;

}

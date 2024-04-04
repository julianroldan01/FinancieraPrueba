package com.prueba.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "cuenta")
public class Cuenta implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCuenta")
    private Long idCuenta;

    @Column(name = "tipoCuenta")
    private String tipoCuenta;

    @Column(name = "numeroCuenta")
    private Long numeroCuenta;

    @Column(name = "estado")
    private String estado;

    @Column(name = "saldo")
    private Long saldo;

    @Column(name = "exentaGMF")
    private String exentaGMF;

    @Column(name = "fechaCreacion")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaCreacion;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = new Date();
    }

    @Column(name = "fechaModificacion")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaModificacion;

    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = new Date();
    }

    @Column(name = "usuario")
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCliente")
    private Cliente cliente;
}

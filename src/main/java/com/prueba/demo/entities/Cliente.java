package com.prueba.demo.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import java.util.List;
import java.io.Serializable;
import java.util.Date;

@Entity
@Data
@Table(name = "cliente")
public class Cliente implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="idCliente")
    private Long idCliente;

    @Column(name = "tipoIdentificacion")
    private String tipoIdentificacion;

    @Column(name = "numeroIdentificacion")
    private Integer numeroIdentificacion;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "apellido")
    private String apellido;

    @Column(name = "correo")
    private String correo;

    @Column(name = "fechaNacimiento")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaNacimiento;

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

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Cuenta> cuentas;
}

package com.prueba.demo.modelo;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString

public class Cliente {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long idCliente;
    String tipoIdentificacion;
    Integer numeroIdentificacion;
    String nombre;
    String apellido;
    String correo;
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date fechaNacimiento;
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date fechaCreacion;
    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = new Date();
    }
    @JsonFormat(pattern = "yyyy-MM-dd")
    Date fechaModificacion;
    @PreUpdate
    protected void onUpdate() {
        this.fechaModificacion = new Date();
    }
}

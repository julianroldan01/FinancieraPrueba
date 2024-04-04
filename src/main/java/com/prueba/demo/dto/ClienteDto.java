package com.prueba.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClienteDto {
    private Long idCliente;
    private String tipoIdentificacion;
    private Integer numeroIdentificacion;
    private String nombre;
    private String apellido;
    private String correo;
    private Date fechaNacimiento;
    private Date fechaCreacion;
    private Date fechaModificacion;
}

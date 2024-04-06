package com.prueba.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuentaDto {
    private Long idCuenta;
    private String tipoCuenta;
    private Long numeroCuenta;
    private String estado;
    private Long saldo;
    private String exentaGMF;
    private Date fechaCreacion;
    private Date fechaModificacion;

}

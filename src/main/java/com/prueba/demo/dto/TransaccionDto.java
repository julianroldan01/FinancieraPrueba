package com.prueba.demo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.prueba.demo.entities.Cuenta;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionDto {
    private Long id;
    private String tipo;
    private Long monto;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fecha;
    @PrePersist
    protected void onCreate() {
        this.fecha = new Date();
    }
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;
}

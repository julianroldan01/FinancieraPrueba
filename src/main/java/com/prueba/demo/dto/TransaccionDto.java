package com.prueba.demo.dto;

import com.prueba.demo.entities.Cuenta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionDto {
    private Long id;
    private String tipo;
    private BigDecimal monto;
    private Date fecha;
    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;
}

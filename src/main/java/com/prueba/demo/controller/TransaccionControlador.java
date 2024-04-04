package com.prueba.demo.controller;

import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.entities.Transaccion;
import com.prueba.demo.service.TransaccionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

public class TransaccionControlador {
@Autowired
    private TransaccionServicio transaccionServicio;
    @PutMapping("/consignacion/{idCuenta}")
    public ResponseEntity<Transaccion> realizarConsignacion(
            @PathVariable Cuenta idCuenta,
            @RequestParam BigDecimal monto) {
        Transaccion transaccion = transaccionServicio.realizarConsignacion(idCuenta, monto);
        return ResponseEntity.ok(transaccion);
    }
}

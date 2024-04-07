package com.prueba.demo.service;

import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.entities.Transaccion;

import java.math.BigDecimal;

public interface ITransaccionServicio {
    Transaccion realizarConsignacion(Cuenta cuenta, Long monto);

    Transaccion realizarRetiro(Cuenta cuenta, Long monto);

    Transaccion realizarTransferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, Long monto);
}

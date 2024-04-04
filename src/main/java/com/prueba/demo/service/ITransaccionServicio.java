package com.prueba.demo.service;

import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.entities.Transaccion;

import java.math.BigDecimal;

public interface ITransaccionServicio {
    Transaccion realizarConsignacion(Cuenta cuenta, BigDecimal monto);

    Transaccion realizarRetiro(Transaccion transaccion);

    Transaccion realizarTransferencia(Transaccion transaccion);
}

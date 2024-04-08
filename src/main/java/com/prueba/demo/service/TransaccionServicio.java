package com.prueba.demo.service;
import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.entities.Transaccion;

import java.util.List;

public interface TransaccionServicio {
    Transaccion realizarConsignacion(Cuenta cuenta, Long monto);

    Transaccion realizarRetiro(Cuenta cuenta, Long monto);

    Transaccion realizarTransferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, Long monto);


    List<Object[]> movimiento();
}

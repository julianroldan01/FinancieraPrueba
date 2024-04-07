package com.prueba.demo.service;

import com.prueba.demo.dto.TransaccionDto;
import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.entities.Transaccion;

public interface TransaccionServicio {
    Transaccion realizarConsignacion(Transaccion transaccion, Long idCuenta);

    Transaccion realizarRetiro(Cuenta cuenta, Long monto);

    Transaccion realizarTransferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, Long monto);


}

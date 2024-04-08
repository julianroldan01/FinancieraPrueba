package com.prueba.demo.service.impl;

import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.entities.Transaccion;
import com.prueba.demo.repository.CuentaRepositorio;
import com.prueba.demo.service.TransaccionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.prueba.demo.repository.TransaccionRepositorio;

import java.util.List;

@Service
public class TransaccionServicioImpl implements TransaccionServicio {
    @Autowired
    private TransaccionRepositorio transaccionRepository;
    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Override
    public Transaccion realizarConsignacion(Cuenta cuenta, Long monto) {
        // Lógica para realizar una consignación y actualizar el saldo de la cuenta
        // Verifica que la cuenta y el monto sean válidos
        if (cuenta == null || monto <= 0) {
            throw new IllegalArgumentException("La cuenta o el monto no son válidos para la consignación.");
        }
        // Crea una nueva transacción de tipo "Consignación"
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo("Consignación");
        transaccion.setMonto(monto);;
        transaccion.setCuentaOrigen(cuenta);
        transaccion.setCuentaDestino(cuenta);
        // Actualiza el saldo de la cuenta
        Long nuevoSaldo = cuenta.getSaldo() + monto;
        cuenta.setSaldo(nuevoSaldo);
        cuentaRepositorio.save(cuenta);
        // Guarda la transacción y actualiza la cuenta en la base de datos
        transaccionRepository.save(transaccion);
        // Retorna la transacción creada
        return transaccion;
    }
    @Override
    public Transaccion realizarRetiro(Cuenta cuenta, Long monto) {
        // Verifica que la cuenta y el monto sean válidos
        if (cuenta == null || monto <= 0) {
            throw new IllegalArgumentException("La cuenta o el monto no son válidos para el retiro.");
        }

        // Verifica que la cuenta tenga saldo suficiente para el retiro
        if (cuenta.getSaldo().compareTo(monto) < 0) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar el retiro.");
        }

        // Crea una nueva transacción de tipo "Retiro"
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo("Retiro");
        transaccion.setMonto(monto); // El retiro es un monto negativo

        transaccion.setCuentaOrigen(cuenta);
        transaccion.setCuentaDestino(cuenta);

        // Actualiza el saldo de la cuenta y guarda la transacción
        cuenta.setSaldo(cuenta.getSaldo() - monto);
        cuentaRepositorio.save(cuenta);
        transaccionRepository.save(transaccion);

        // Retorna la transacción creada
        return transaccion;
    }
    @Override
    public Transaccion realizarTransferencia(Cuenta cuentaOrigen, Cuenta cuentaDestino, Long monto) {
        // Verifica que las cuentas y el monto sean válidos
        if (cuentaOrigen == null || cuentaDestino == null || monto <= 0) {
            throw new IllegalArgumentException("Las cuentas o el monto no son válidos para la transferencia.");
        }

        // Verifica que la cuenta de origen tenga saldo suficiente para la transferencia
        if (cuentaOrigen.getSaldo() < monto) {
            throw new IllegalArgumentException("Saldo insuficiente para realizar la transferencia.");
        }

        // Crea una nueva transacción de tipo "Transferencia"
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo("Transferencia");
        transaccion.setMonto(monto); // La transferencia es un monto negativo para la cuenta de origen
        transaccion.setCuentaOrigen(cuentaOrigen);
        transaccion.setCuentaDestino(cuentaDestino);

        // Actualiza los saldos de las cuentas y guarda la transacción
        cuentaOrigen.setSaldo(cuentaOrigen.getSaldo() - monto);
        cuentaDestino.setSaldo(cuentaDestino.getSaldo() + monto);
        cuentaRepositorio.save(cuentaOrigen);
        cuentaRepositorio.save(cuentaDestino);
        transaccionRepository.save(transaccion);

        // Retorna la transacción creada
        return transaccion;
    }
    @Override
    public List<Object[]> movimiento() {
        return transaccionRepository.movimientos();
    }
}

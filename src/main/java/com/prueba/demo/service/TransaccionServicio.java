package com.prueba.demo.service;

import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.entities.Transaccion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.prueba.demo.repository.TransaccionRepositorio;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class TransaccionServicio implements ITransaccionServicio{
    @Autowired
    private TransaccionRepositorio transaccionRepository;
    @Override
    public Transaccion realizarConsignacion(Cuenta cuenta, BigDecimal monto) {
        // Lógica para realizar una consignación y actualizar el saldo de la cuenta
        // Verifica que la cuenta y el monto sean válidos
        if (cuenta == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cuenta o el monto no son válidos para la consignación.");
        }
        // Crea una nueva transacción de tipo "Consignación"
        Transaccion transaccion = new Transaccion();
        transaccion.setTipo("Consignación");
        transaccion.setMonto(monto);
        transaccion.setFecha(LocalDate.now());
        transaccion.setCuentaOrigen(cuenta);
        transaccion.setCuentaDestino(cuenta); // En una consignación, la cuenta origen y destino son la misma
        // Actualiza el saldo de la cuenta
        Long nuevoSaldo = cuenta.getSaldo() + monto.longValue();
        cuenta.setSaldo(nuevoSaldo);
        // Guarda la transacción y actualiza la cuenta en la base de datos
        transaccionRepository.save(transaccion);
        // Retorna la transacción creada
        return transaccion;
    }
    @Override
    public Transaccion realizarRetiro(Transaccion transaccion) {
        // Lógica para realizar un retiro
        // ...
        // Guardar la transacción en la base de datos
        return transaccionRepository.save(transaccion);
    }
    @Override
    public Transaccion realizarTransferencia(Transaccion transaccion) {
        // Lógica para realizar una transferencia
        // ...
        // Guardar la transacción en la base de datos
        return transaccionRepository.save(transaccion);
    }


}

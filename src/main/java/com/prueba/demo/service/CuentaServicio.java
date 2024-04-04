package com.prueba.demo.service;


import com.prueba.demo.ecxepcion.RecursoInternalServer;
import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.repository.CuentaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class CuentaServicio implements ICuentaServicio{
    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Override
    public List<Cuenta> listarCuentas() {
        return this.cuentaRepositorio.findAll();
    }

    @Override
    public Cuenta buscarCuentaPorId(Integer idCuenta) {
        Cuenta cuenta =
                this.cuentaRepositorio.findById(Long.valueOf(idCuenta)).orElse(null);
        return cuenta;
    }

    @Override
    public Cuenta guardarCuenta(Cuenta cuenta) {
        // Verificar el tipo de cuenta y generar el número de cuenta
        if (cuenta.getTipoCuenta().equalsIgnoreCase("corriente")) {
            cuenta.setNumeroCuenta(generarNumeroCuenta("33"));
        } else if (cuenta.getTipoCuenta().equalsIgnoreCase("ahorro")) {
            cuenta.setNumeroCuenta(generarNumeroCuenta("53"));
            cuenta.setEstado("activa"); // Establecer la cuenta de ahorro como activa
        }

        // Verificar si el número de cuenta ya existe
        while (cuentaRepositorio.existsByNumeroCuenta(Math.toIntExact(cuenta.getNumeroCuenta()))) {
            cuenta.setNumeroCuenta(generarNumeroCuenta(cuenta.getTipoCuenta().equalsIgnoreCase("corriente") ? "33" : "53"));
        }
        // Verificar que la cuenta de ahorros no tenga un saldo menor a $0 (cero)
        if (cuenta.getTipoCuenta().equalsIgnoreCase("ahorro") && cuenta.getSaldo() <= 0) {
            throw new RecursoInternalServer("La cuenta de ahorros no puede tener un saldo menor a $0 (cero)");
        }
        // Verificar si el saldo es igual a $0 para cambiar el estado a "cancelada"
        if (cuenta.getSaldo() == 0) {
            cuenta.setEstado("cancelada");
        }

        // Guardar la cuenta con el número de cuenta generado
        return cuentaRepositorio.save(cuenta);
    }

    private Long generarNumeroCuenta(String prefijo) {
        Random random = new Random();
        long numeroAleatorio = random.nextInt(9000000) + 1000000; // Genera un número aleatorio de 7 dígitos
        return Long.parseLong(prefijo + numeroAleatorio);
    }

    @Override
    public void eliminarCuentaPorId(Integer idCuenta) {

        this.cuentaRepositorio.deleteById(Long.valueOf(idCuenta));
    }

}

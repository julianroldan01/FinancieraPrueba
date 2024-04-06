package com.prueba.demo.service.impl;

import com.prueba.demo.ecxepcion.RecursoInternalServer;
import com.prueba.demo.ecxepcion.RecursoNoEncontradoExcepcion;
import com.prueba.demo.entities.Cliente;
import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.repository.ClienteRepositorio;
import com.prueba.demo.repository.CuentaRepositorio;
import com.prueba.demo.service.CuentaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class CuentaServicioImpl implements CuentaServicio {
    @Autowired
    private CuentaRepositorio cuentaRepositorio;

    @Autowired
    private ClienteRepositorio clienteRepositorio;

    @Override
    public List<Cuenta> listarCuentas() {

        return this.cuentaRepositorio.findAll();
    }

    @Override
    public Cuenta buscarCuentaPorId(Integer idCuenta) {
        return this.cuentaRepositorio.findById(Long.valueOf(idCuenta)).orElse(null);
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
        while (cuentaRepositorio.existsByNumeroCuenta(cuenta.getNumeroCuenta())) {
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
        Optional<Cliente> dbResponse = this.clienteRepositorio.findById(cuenta.getCliente().getIdCliente());
        if (dbResponse.isPresent()) {
            cuenta.setCliente(dbResponse.get());
        } else {
            throw new RecursoInternalServer("El cliente no existe");
        }

        // Guardar la cuenta con el número de cuenta generado
        return cuentaRepositorio.save(cuenta);
    }

    private Long generarNumeroCuenta(String prefijo) {
        Random random = new Random();
        long numeroAleatorio = random.nextInt(90000000) + 10000000; // Genera un número aleatorio de 8 dígitos
        return Long.parseLong(prefijo + numeroAleatorio);
    }

    @Override
    public void eliminarCuentaPorId(Integer idCuenta) {

        this.cuentaRepositorio.deleteById(Long.valueOf(idCuenta));
    }

    @Override
    public Cuenta actualizarCuenta(int id, Cuenta cuentaRecibido) {
        Cuenta cuenta = cuentaRepositorio.findById((long) id).orElse(null);
        if (cuenta == null) {
            throw new RecursoNoEncontradoExcepcion("No se encontró la cuenta con el id: " + id);
        }

        cuenta.setTipoCuenta(cuentaRecibido.getTipoCuenta());
        cuenta.setNumeroCuenta(cuentaRecibido.getNumeroCuenta());
        cuenta.setEstado(cuentaRecibido.getEstado());
        cuenta.setSaldo(cuentaRecibido.getSaldo());
        cuenta.setExentaGMF(cuentaRecibido.getExentaGMF());
        cuenta.setFechaModificacion(new Date()); // Actualizar la fecha de modificación

        return cuentaRepositorio.save(cuenta);
    }
}

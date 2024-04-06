package com.prueba.demo.service;

import com.prueba.demo.entities.Cuenta;
import java.util.List;

public interface CuentaServicio {
     List<Cuenta> listarCuentas();

     Cuenta buscarCuentaPorId(Integer idCuenta);

     Cuenta guardarCuenta(Cuenta cuenta);

     void eliminarCuentaPorId(Integer idCuenta);

    Cuenta actualizarCuenta(int id, Cuenta cuentaRecibido);
}

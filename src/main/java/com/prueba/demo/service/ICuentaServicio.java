package com.prueba.demo.service;

import com.prueba.demo.entities.Cuenta;
import java.util.List;

public interface ICuentaServicio {
    public List<Cuenta> listarCuentas();

    public Cuenta buscarCuentaPorId(Integer idCuenta);

    public Cuenta guardarCuenta(Cuenta cuenta);

    public void eliminarCuentaPorId(Integer idCuenta);

    Cuenta actualizarCuenta(int id, Cuenta cuentaRecibido);
}

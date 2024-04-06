package com.prueba.demo.repository;

import com.prueba.demo.entities.Cliente;
import com.prueba.demo.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CuentaRepositorio extends JpaRepository<Cuenta, Long> {
    boolean existsByNumeroCuenta(Long numeroCuenta);

    List<Cuenta> findByCliente(Cliente cliente);
}

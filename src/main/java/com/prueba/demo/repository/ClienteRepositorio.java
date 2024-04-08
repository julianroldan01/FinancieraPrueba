package com.prueba.demo.repository;

import com.prueba.demo.entities.Cliente;
import com.prueba.demo.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
}

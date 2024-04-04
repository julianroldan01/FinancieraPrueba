package com.prueba.demo.repository;

import com.prueba.demo.entities.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransaccionRepositorio extends JpaRepository<Transaccion, Long> {

}

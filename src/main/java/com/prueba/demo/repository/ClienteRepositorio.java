package com.prueba.demo.repository;

import com.prueba.demo.entities.Cliente;
import com.prueba.demo.entities.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ClienteRepositorio extends JpaRepository<Cliente, Long> {
    @Query(value = "select c.usuario , cl.apellido, c.tipo_cuenta, c.numero_cuenta, c.estado, c.saldo, c.exentaGMF,cl.fecha_nacimiento, c.fecha_creacion, c.fecha_modificacion,\n" +
            "cl.tipo_identificacion, cl.numero_identificacion, cl.correo \n" +
            "from cuenta c inner join cliente cl on c.usuario = cl.nombre ",
            nativeQuery = true)
    List<Object[]> obtenerInformacionCuentaCliente();
}

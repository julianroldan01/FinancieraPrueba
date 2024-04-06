package com.prueba.demo.service;


import com.prueba.demo.entities.Cliente;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ClienteServicio {

     List<Cliente> listarClientes();
     Cliente buscarClientePorId(Integer idCiente);
     ResponseEntity<String> guardarCliente(Cliente cliente);
     void eliminarClientePorId(Integer idCliente);
     List<Object[]> obtenerInformacionCuentaCliente();
     Cliente actualizarCliente(int id, Cliente clienteRecibido);
}

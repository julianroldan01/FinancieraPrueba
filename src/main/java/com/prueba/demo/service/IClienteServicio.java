package com.prueba.demo.service;


import com.prueba.demo.entities.Cliente;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IClienteServicio {

    public List<Cliente> listarClientes();
    public Cliente buscarClientePorId(Integer idCiente);
    public ResponseEntity<String> guardarCliente(Cliente cliente);
    public void eliminarClientePorId(Integer idCliente);
    List<Object[]> obtenerInformacionCuentaCliente();

    Cliente actualizarCliente(int id, Cliente clienteRecibido);
}

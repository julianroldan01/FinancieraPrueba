package com.prueba.demo.servicio;

import com.prueba.demo.modelo.Cliente;

import java.util.List;

public interface IClienteServicio {

    public List<Cliente> listarClientes();
    public Cliente buscarClientePorId(Integer idCiente);

    public Cliente guardarCliente(Cliente cliente);

    public void eliminarClientePorId(Integer idCliente);

}

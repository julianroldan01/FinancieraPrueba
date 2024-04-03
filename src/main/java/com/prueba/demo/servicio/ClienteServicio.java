package com.prueba.demo.servicio;

import com.prueba.demo.modelo.Cliente;
import com.prueba.demo.repositorio.ClienteRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ClienteServicio implements IClienteServicio{

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Override
    public List<Cliente> listarClientes(){ return this.clienteRepositorio.findAll();}

    @Override
    public Cliente buscarClientePorId(Integer idCliente){
        Cliente cliente =
                this.clienteRepositorio.findById(idCliente).orElse(null);
        return cliente;
    }
    @Override
    public Cliente guardarCliente(Cliente cliente) { return this.clienteRepositorio.save(cliente);}
    @Override
    public void eliminarClientePorId(Integer idCliente){this.clienteRepositorio.deleteById(idCliente);}
}

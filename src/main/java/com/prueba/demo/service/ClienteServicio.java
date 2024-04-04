package com.prueba.demo.service;

import com.prueba.demo.ecxepcion.RecursoBadRequest;
import com.prueba.demo.ecxepcion.RecursoNoEncontradoExcepcion;
import com.prueba.demo.entities.Cliente;
import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.repository.ClienteRepositorio;
import com.prueba.demo.repository.CuentaRepositorio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.List;
@Service
public class ClienteServicio implements IClienteServicio{

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    @Autowired
    private CuentaRepositorio cuentaRepositorio;
    @Override
    public List<Cliente> listarClientes(){
        return this.clienteRepositorio.findAll();}

    @Override
    public Cliente buscarClientePorId(Integer idCliente){
        Cliente cliente =
                this.clienteRepositorio.findById(Long.valueOf(idCliente)).orElse(null);
        return cliente;
    }
    @Override
    public ResponseEntity<String> guardarCliente(Cliente cliente) {
        // Validar la edad del cliente
        LocalDate fechaNacimiento = cliente.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fechaActual = LocalDate.now();
        Period periodo = Period.between(fechaNacimiento, fechaActual);
        int edad = periodo.getYears();
        if (edad < 18) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede agregar una persona menor de edad");
        }
        try {
            // Si pasa la validación, guardar el cliente
            Cliente clienteGuardado = this.clienteRepositorio.save(cliente);
            return ResponseEntity.ok("Cliente agregado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el cliente");
        }
    }
    @Override
    public void eliminarClientePorId(Integer idCliente) {
        Cliente cliente = clienteRepositorio.findById(Long.valueOf(idCliente))
                .orElseThrow(() -> new RecursoNoEncontradoExcepcion("Cliente no encontrado"));
        List<Cuenta> cuentasCliente = cuentaRepositorio.findByCliente(cliente);
        if (!cuentasCliente.isEmpty()) {
            throw new RecursoNoEncontradoExcepcion("No se puede eliminar el cliente porque tiene cuentas asociadas");
        }

        clienteRepositorio.deleteById(Long.valueOf(idCliente));
    }
    @Override
    public List<Object[]> obtenerInformacionCuentaCliente() {
        return clienteRepositorio.obtenerInformacionCuentaCliente();
    }
}

package com.prueba.demo.controller;

import com.prueba.demo.ecxepcion.RecursoNoEncontradoExcepcion;
import com.prueba.demo.entities.Cliente;
import com.prueba.demo.service.ClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
//http://locahost:8080/financiera
@RequestMapping(path = "/financiera", produces="application/json")
//@CrossOrigin(value = "http://localhost:4200")
public class ClienteControlador {

    @Autowired
    private ClienteServicio clienteServicio;

    //http://locahost:8080/financiera/clientes
    @GetMapping("/clientes")
    public List<Cliente> obtenerClientes() {
        return this.clienteServicio.listarClientes();
    }

    @PostMapping("/clientes")
    public ResponseEntity<String> agregarCliente(@RequestBody Cliente cliente) {
        return this.clienteServicio.guardarCliente(cliente);
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(
            @PathVariable int id) {
        Cliente cliente = this.clienteServicio.buscarClientePorId(id);
        if (cliente != null)
            return ResponseEntity.ok(cliente);
        else
            throw new RecursoNoEncontradoExcepcion("No se encontro el id: " + id);

    }
    @PutMapping("/clientes/{id}")
    public ResponseEntity<Cliente> actualizarCliente(
            @PathVariable int id,
            @RequestBody Cliente clienteRecibido) {
        Cliente clienteActualizado = clienteServicio.actualizarCliente(id, clienteRecibido);
        return ResponseEntity.ok(clienteActualizado);
    }

    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarCliente(@PathVariable int id) {
        Cliente cliente = clienteServicio.buscarClientePorId(id);
        if (cliente == null)
            throw new RecursoNoEncontradoExcepcion("No se encontro el id: " + id);
        this.clienteServicio.eliminarClientePorId(Math.toIntExact(cliente.getIdCliente()));
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("eliminado", Boolean.TRUE);
        return ResponseEntity.ok(respuesta);
    }
}

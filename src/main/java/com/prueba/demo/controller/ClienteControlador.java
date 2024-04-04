package com.prueba.demo.controller;

import com.prueba.demo.ecxepcion.RecursoNoEncontradoExcepcion;
import com.prueba.demo.entities.Cliente;
import com.prueba.demo.service.ClienteServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
//http://locahost:8080/financiera
@RequestMapping("/financiera")
//@CrossOrigin(value = "http://localhost:4200")
public class ClienteControlador {
    private static final Logger logger =
            LoggerFactory.getLogger(ClienteControlador.class);
    @Autowired
    private ClienteServicio clienteServicio;
    //http://locahost:8080/financiera/clientes
    @GetMapping("/clientes")
    public  List<Cliente> obtenerClientes(){
        List<Cliente> clientes = this.clienteServicio.listarClientes();
        logger.info("Clientes obtenidos: ");
        clientes.forEach((cliente -> logger.info(cliente.toString())));
        return clientes;
    }
//@PostMapping("/clientes")
//public ResponseEntity<String> agregarPersona(@Valid @RequestBody ClienteDto cliente) {
//    LocalDate fechaNacimiento = cliente.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//    LocalDate fechaActual = LocalDate.now();
//    Period periodo = Period.between(fechaNacimiento, fechaActual);
//    int edad = periodo.getYears();
//    if (edad < 18) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede agregar una persona menor de edad");
//    }
//    ClienteDto clienteGuardado = this.clienteServicio.guardarCliente(cliente);
//    return ResponseEntity.ok("Persona agregada exitosamente");
//}
@PostMapping ("/clientes")
public ResponseEntity<String> agregarCliente(@RequestBody Cliente cliente){
    logger.info("Cliente a agregar: " + cliente);
    return this.clienteServicio.guardarCliente(cliente);
}
    @GetMapping("/clientes/{id}")
    public ResponseEntity<Cliente> obtenerClientePorId(
            @PathVariable int id){
        Cliente cliente = this.clienteServicio.buscarClientePorId(id);
        if(cliente != null)
            return  ResponseEntity.ok(cliente);
        else
            throw new RecursoNoEncontradoExcepcion("No se encontro el id: " + id);

    }
    @PutMapping("/clientes/{id}")
    public ResponseEntity<Cliente> actualizarCliente(
            @PathVariable int id,
            @RequestBody Cliente clienteRecibido){
        Cliente cliente = this.clienteServicio.buscarClientePorId(id);
        if(cliente == null)
            throw new RecursoNoEncontradoExcepcion("No se encontro el id: " + id);
        cliente.setTipoIdentificacion(clienteRecibido.getTipoIdentificacion());
        cliente.setNumeroIdentificacion(clienteRecibido.getNumeroIdentificacion());
        cliente.setNombre(clienteRecibido.getNombre());
        cliente.setApellido(clienteRecibido.getApellido());
        cliente.setCorreo(clienteRecibido.getCorreo());
        cliente.setFechaNacimiento(clienteRecibido.getFechaNacimiento());
        cliente.setFechaCreacion(clienteRecibido.getFechaCreacion());
        cliente.setFechaModificacion(clienteRecibido.getFechaModificacion());
        this.clienteServicio.guardarCliente(cliente);
        return ResponseEntity.ok(cliente);
    }
    @DeleteMapping("/clientes/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarCliente(@PathVariable int id){
        Cliente cliente = clienteServicio.buscarClientePorId(id);
        if(cliente == null)
            throw new RecursoNoEncontradoExcepcion("No se encontro el id: " + id);
        this.clienteServicio.eliminarClientePorId(Math.toIntExact(cliente.getIdCliente()));
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("eliminado", Boolean.TRUE);
        return ResponseEntity.ok(respuesta);
    }
    @GetMapping("/infCuentaCliente")
    public ResponseEntity<List<Object[]>> obtenerInformacionCuentaCliente() {
        List<Object[]> informacionCuentaCliente = clienteServicio.obtenerInformacionCuentaCliente();
        return ResponseEntity.ok(informacionCuentaCliente);
    }
}

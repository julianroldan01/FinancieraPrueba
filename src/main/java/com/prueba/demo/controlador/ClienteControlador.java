package com.prueba.demo.controlador;

import com.prueba.demo.ecxepcion.RecursoNoEncontradoExcepcion;
import com.prueba.demo.modelo.Cliente;
import com.prueba.demo.servicio.ClienteServicio;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
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
//    @PostMapping("/clientes")
//    public ResponseEntity<String> agregarPersona(@Valid @RequestBody Cliente cliente) {
//        if (cliente.getEdad() <= 17) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede agregar una persona menor de edad");
//        }
//        Cliente clienteGuardado = this.clienteServicio.guardarCliente(cliente);
//        return ResponseEntity.ok("Persona agregada exitosamente");
//    }
@PostMapping("/clientes")
public ResponseEntity<String> agregarPersona(@Valid @RequestBody Cliente cliente) {
    LocalDate fechaNacimiento = cliente.getFechaNacimiento().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    LocalDate fechaActual = LocalDate.now();
    Period periodo = Period.between(fechaNacimiento, fechaActual);
    int edad = periodo.getYears();
    if (edad < 18) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No se puede agregar una persona menor de edad");
    }
    Cliente clienteGuardado = this.clienteServicio.guardarCliente(cliente);
    return ResponseEntity.ok("Persona agregada exitosamente");
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
}

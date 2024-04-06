package com.prueba.demo.controller;

import com.prueba.demo.ecxepcion.RecursoNoEncontradoExcepcion;
import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.service.impl.CuentaServicioImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
//http://locahost:8080/financiera
@RequestMapping("/financiera")
//@CrossOrigin(value = "http://localhost:4200")
public class CuentaControlador {
    private static final Logger logger =
            LoggerFactory.getLogger(CuentaControlador.class);
    @Autowired
    private CuentaServicioImpl cuentaServicio;

    //http://locahost:8080/financiera/cuentas
    @GetMapping("/cuentas")
    public List<Cuenta> obtenerCuentas() {
        return this.cuentaServicio.listarCuentas();
    }

    @PostMapping("/cuentas")
    public ResponseEntity<Cuenta> agregarCuenta(@RequestBody Cuenta cuenta) {
        Cuenta cuentaGuardada = this.cuentaServicio.guardarCuenta(cuenta);
        return ResponseEntity.status(HttpStatus.CREATED).body(cuentaGuardada);
    }


    @GetMapping("/cuentas/{id}")
    public ResponseEntity<Cuenta> obtenerCuentaPorId(
            @PathVariable int id) {
        Cuenta cuenta = this.cuentaServicio.buscarCuentaPorId(id);
        if (cuenta != null)
            return ResponseEntity.ok(cuenta);
        else
            throw new RecursoNoEncontradoExcepcion("No se encontro por el id: " + id);
    }

    @PutMapping("/cuentas/{id}")
    public ResponseEntity<Cuenta> actualizarCuenta(
            @PathVariable int id,
            @RequestBody Cuenta cuentaRecibido) {
        Cuenta cuentaActualizada = cuentaServicio.actualizarCuenta(id, cuentaRecibido);
        return ResponseEntity.ok(cuentaActualizada);
    }

    @DeleteMapping("/cuentas/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarCuenta(@PathVariable int id) {
        Cuenta cuenta = cuentaServicio.buscarCuentaPorId(id);
        if (cuenta == null)
            throw new RecursoNoEncontradoExcepcion("No se encontro el id: " + id);
        this.cuentaServicio.eliminarCuentaPorId(Math.toIntExact(cuenta.getIdCuenta()));
        Map<String, Boolean> respuesta = new HashMap<>();
        respuesta.put("eliminado", Boolean.TRUE);
        return ResponseEntity.ok(respuesta);
    }
}

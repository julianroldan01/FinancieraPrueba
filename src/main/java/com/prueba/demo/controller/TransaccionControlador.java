package com.prueba.demo.controller;

import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.entities.Transaccion;
import com.prueba.demo.repository.CuentaRepositorio;
import com.prueba.demo.service.TransaccionServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/financiera", produces="application/json")
public class TransaccionControlador {
@Autowired
    private TransaccionServicio transaccionServicio;
@Autowired
private CuentaRepositorio cuentaRepositorio;
    @PostMapping("/consignacion")
    public ResponseEntity<String> realizarConsignacion(@RequestParam Long idCuenta, @RequestParam Long monto) {
        try {
            Cuenta cuenta = cuentaRepositorio.findById(idCuenta)
                    .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
            Transaccion transaccion = transaccionServicio.realizarConsignacion(cuenta, monto);
            return ResponseEntity.ok("Consignación realizada correctamente. ID de transacción: " + transaccion.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @PostMapping("/retiro")
    public ResponseEntity<String> realizarRetiro(@RequestParam Long idCuenta, @RequestParam Long monto) {
        try {
            Cuenta cuenta = cuentaRepositorio.findById(idCuenta)
                    .orElseThrow(() -> new IllegalArgumentException("Cuenta no encontrada"));
            Transaccion transaccion = transaccionServicio.realizarRetiro(cuenta, monto);
            return ResponseEntity.ok("Retiro realizado correctamente. ID de transacción: " + transaccion.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/transferencia")
    public ResponseEntity<String> realizarTransferencia(@RequestParam Long cuenta_origen_Id, @RequestParam Long cuenta_destino_Id, @RequestParam long monto) {
        try {
            Cuenta cuentaOrigen = cuentaRepositorio.findById(cuenta_origen_Id)
                    .orElseThrow(() -> new IllegalArgumentException("Cuenta de origen no encontrada"));
            Cuenta cuentaDestino = cuentaRepositorio.findById(cuenta_destino_Id)
                    .orElseThrow(() -> new IllegalArgumentException("Cuenta de destino no encontrada"));
            Transaccion transaccion = transaccionServicio.realizarTransferencia(cuentaOrigen, cuentaDestino, monto);
            return ResponseEntity.ok("Transferencia realizada correctamente. ID de transacción: " + transaccion.getId());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/movimientos")
    public ResponseEntity<List<Object[]>> movimientos() {
        List<Object[]> movimientos = transaccionServicio.movimiento();
        return ResponseEntity.ok(movimientos);
    }
}

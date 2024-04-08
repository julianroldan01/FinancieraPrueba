package com.prueba.demo.repository;

import com.prueba.demo.entities.Transaccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface TransaccionRepositorio extends JpaRepository<Transaccion, Long> {
@Query(value = "SELECT \n" +
        "    t.id, \n" +
        "    t.fecha, \n" +
        "    t.cuenta_origen_id, \n" +
        "    c.saldo AS saldo_origen, \n" +
        "    t.tipo, \n" +
        "    t.monto, \n" +
        "    t.cuenta_destino_id, \n" +
        "    cd.saldo AS saldo_destino,\n" +
        "    CASE \n" +
        "        WHEN t.tipo = 'Consignacion' THEN cd.saldo + t.monto\n" +
        "        WHEN t.tipo = 'Retiro' THEN cd.saldo - t.monto\n" +
        "        WHEN t.tipo = 'Transferencia' THEN c.saldo - t.monto\n" +
        "    END AS nuevo_saldo_origen,\n" +
        "    CASE \n" +
        "        WHEN t.tipo = 'Consignacion' THEN cd.saldo + t.monto\n" +
        "        WHEN t.tipo = 'Retiro' THEN cd.saldo - t.monto\n" +
        "        WHEN t.tipo = 'Transferencia' THEN cd.saldo + t.monto\n" +
        "    END AS nuevo_saldo_destino\n" +
        "FROM \n" +
        "    transacciones t\n" +
        "INNER JOIN \n" +
        "    cuenta c ON t.cuenta_origen_id = c.id_cuenta\n" +
        "INNER JOIN \n" +
        "    cuenta cd ON t.cuenta_destino_id = cd.id_cuenta", nativeQuery = true)
        List<Object[]> movimientos();
}

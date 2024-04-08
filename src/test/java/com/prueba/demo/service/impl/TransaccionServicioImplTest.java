package com.prueba.demo.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.entities.Transaccion;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import com.prueba.demo.repository.CuentaRepositorio;
import com.prueba.demo.repository.TransaccionRepositorio;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class TransaccionServicioImplTest {
    @Mock
    private CuentaRepositorio cuentaRepositorio;

    @Mock
    private TransaccionRepositorio transaccionRepositorio;
    @InjectMocks
    private TransaccionServicioImpl transaccionServicio;
    @Test
    void testRealizarConsignacion() {
        // Given
        Cuenta cuenta = new Cuenta();
        cuenta.setIdCuenta(1L);
        cuenta.setSaldo(100L);

        Long monto = 5000L;

        Transaccion transaccionMock = new Transaccion();
        transaccionMock.setTipo("Consignación");
        transaccionMock.setMonto(monto);
        transaccionMock.setCuentaOrigen(cuenta);
        transaccionMock.setCuentaDestino(cuenta);

        // Mocking
        given(cuentaRepositorio.save(cuenta)).willReturn(cuenta);
        given(transaccionRepositorio.save(transaccionMock)).willReturn(transaccionMock);

        // When
        Transaccion transaccion = transaccionServicio.realizarConsignacion(cuenta, monto);

        // Then
        assertThat(transaccion.getId()).isEqualTo(transaccionMock.getId());
    }
    @Test
    void testRealizarRetiro() {
        // Given
        Cuenta cuenta = new Cuenta();
        cuenta.setIdCuenta(1L);
        cuenta.setSaldo(100L);

        Long monto = 5000L;

        Transaccion transaccionMock = new Transaccion();
        transaccionMock.setTipo("Retiro");
        transaccionMock.setMonto(monto);
        transaccionMock.setCuentaOrigen(cuenta);
        transaccionMock.setCuentaDestino(cuenta);

        // Mocking
        given(cuentaRepositorio.save(cuenta)).willReturn(cuenta);
        given(transaccionRepositorio.save(transaccionMock)).willReturn(transaccionMock);

        // When
        Transaccion transaccion = transaccionServicio.realizarConsignacion(cuenta, monto);

        // Then
        assertThat(transaccion.getId()).isEqualTo(transaccionMock.getId());
    }
    @Test
    void testRealizarTransferencia() {
        // Given
        Cuenta cuentaOrigen = new Cuenta();
        cuentaOrigen.setIdCuenta(1L);
        cuentaOrigen.setSaldo(100L);

        Cuenta cuentaDestino = new Cuenta();
        cuentaDestino.setIdCuenta(2L);
        cuentaDestino.setSaldo(50L);

        Long monto = 50L;

        Transaccion transaccionMock = new Transaccion();
        transaccionMock.setTipo("Transferencia");
        transaccionMock.setMonto(monto);
        transaccionMock.setCuentaOrigen(cuentaOrigen);
        transaccionMock.setCuentaDestino(cuentaDestino);

        // Mocking
        given(cuentaRepositorio.save(cuentaOrigen)).willReturn(cuentaOrigen);
        given(cuentaRepositorio.save(cuentaDestino)).willReturn(cuentaDestino);
        given(transaccionRepositorio.save(transaccionMock)).willReturn(transaccionMock);

        // When
        Transaccion transaccion = transaccionServicio.realizarTransferencia(cuentaOrigen, cuentaDestino, monto);

        // Then
        assertThat(transaccion.getId()).isEqualTo(transaccionMock.getId());
    }

}

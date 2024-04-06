package com.prueba.demo.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;

import com.prueba.demo.ecxepcion.RecursoInternalServer;
import com.prueba.demo.ecxepcion.RecursoNoEncontradoExcepcion;
import com.prueba.demo.entities.Cliente;
import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.repository.ClienteRepositorio;
import com.prueba.demo.repository.CuentaRepositorio;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;
import java.util.ArrayList;
import java.util.Date;

import org.hibernate.collection.spi.PersistentBag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@DisabledInAotMode
@SpringBootTest
@ExtendWith(SpringExtension.class)
class CuentaServicioImplDiffblueTest {
  @Autowired
  private CuentaServicioImpl cuentaServicioImpl;

  @Autowired
  private CuentaServicioImpl cuentaServicio;

  @MockBean
  private CuentaRepositorio cuentaRepositorio;

  @MockBean
  private ClienteRepositorio clienteRepositorio;

  @Test
  void testListarCuentas() {
    // Given
    Cliente cliente = new Cliente();
    cliente.setIdCliente(1L);
    cliente.setNombre("Cliente1");

    Cuenta cuenta1 = new Cuenta();
    cuenta1.setIdCuenta(1L);
    cuenta1.setTipoCuenta("Corriente");
    cuenta1.setNumeroCuenta(123456789L);
    cuenta1.setEstado("Activa");
    cuenta1.setSaldo(10000L);
    cuenta1.setExentaGMF("Sí");
    cuenta1.setFechaCreacion(new Date());
    cuenta1.setFechaModificacion(new Date());
    cuenta1.setCliente(cliente);

    Cuenta cuenta2 = new Cuenta();
    cuenta2.setIdCuenta(2L);
    cuenta2.setTipoCuenta("Ahorro");
    cuenta2.setNumeroCuenta(987654321L);
    cuenta2.setEstado("Inactiva");
    cuenta2.setSaldo(5000L);
    cuenta2.setExentaGMF("No");
    cuenta2.setFechaCreacion(new Date());
    cuenta2.setFechaModificacion(new Date());
    cuenta2.setCliente(cliente);

    List<Cuenta> cuentas = Arrays.asList(cuenta1, cuenta2);

    // Mock del comportamiento del repositorio al llamar a findAll
    when(cuentaRepositorio.findAll()).thenReturn(cuentas);

    // When
    List<Cuenta> cuentasObtenidas = cuentaServicio.listarCuentas();

    // Then
    assertEquals(2, cuentasObtenidas.size());
    assertEquals("Corriente", cuentasObtenidas.getFirst().getTipoCuenta());
    assertEquals(123456789L, cuentasObtenidas.getFirst().getNumeroCuenta());
    assertEquals("Activa", cuentasObtenidas.getFirst().getEstado());
    assertEquals(10000L, cuentasObtenidas.getFirst().getSaldo());
    assertEquals("Sí", cuentasObtenidas.getFirst().getExentaGMF());
    assertEquals(cliente, cuentasObtenidas.getFirst().getCliente());
    // Verificar otros atributos de cuenta2 también
  }

  /**
   * Method under test: {@link CuentaServicioImpl#guardarCuenta(Cuenta)}
   */
  @Test
  void testGuardarCuenta() {
    Cliente cliente = new Cliente();
    cliente.setApellido("Apellido");
    cliente.setCorreo("Correo");
    cliente.setCuentas(new ArrayList<>());
    cliente.setFechaCreacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cliente.setFechaModificacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cliente.setFechaNacimiento(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cliente.setIdCliente(1L);
    cliente.setNombre("Nombre");
    cliente.setNumeroIdentificacion(1);
    cliente.setTipoIdentificacion("Tipo Identificacion");

    Cuenta cuenta = new Cuenta();
    cuenta.setCliente(cliente);
    cuenta.setEstado("Estado");
    cuenta.setExentaGMF("Exenta GMF");
    cuenta.setFechaCreacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cuenta.setFechaModificacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cuenta.setIdCuenta(1L);
    cuenta.setNumeroCuenta(1L);
    cuenta.setSaldo(1L);
    cuenta.setTipoCuenta("Tipo Cuenta");
    Cuenta actualGuardarCuentaResult = cuentaServicioImpl.guardarCuenta(cuenta);
    Cliente cliente2 = cuenta.getCliente();
    assertInstanceOf(PersistentBag.class, cliente2.getCuentas());
    assertEquals("Gutierrez Pascuas", cliente2.getApellido());
    assertEquals(1L, actualGuardarCuentaResult.getCliente().getIdCliente().longValue());
  }
  @Test
  void testGuardarCuenta1() {
    Cliente cliente = new Cliente();
    cliente.setApellido("Apellido");
    cliente.setCorreo("Correo");
    cliente.setCuentas(new ArrayList<>());
    cliente.setFechaCreacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cliente.setFechaModificacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cliente.setFechaNacimiento(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cliente.setIdCliente(2L);
    cliente.setNombre("Nombre");
    cliente.setNumeroIdentificacion(1);
    cliente.setTipoIdentificacion("Tipo Identificacion");

    Cuenta cuenta = new Cuenta();
    cuenta.setCliente(cliente);
    cuenta.setEstado("Estado");
    cuenta.setExentaGMF("Exenta GMF");
    cuenta.setFechaCreacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cuenta.setFechaModificacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cuenta.setIdCuenta(1L);
    cuenta.setNumeroCuenta(1L);
    cuenta.setSaldo(1L);
    cuenta.setTipoCuenta("Tipo Cuenta");
    assertThrows(RecursoInternalServer.class, () -> cuentaServicioImpl.guardarCuenta(cuenta));
  }
  @Test
  void testEliminarCuentaPorId() {
    doNothing().when(cuentaRepositorio).deleteById(Mockito.<Long>any());
    cuentaServicio.eliminarCuentaPorId(1);
    verify(cuentaRepositorio).deleteById(isA(Long.class));
  }

  @Test
  void testEliminarCuentaPorId2() {
    doThrow(new RecursoInternalServer("Mensaje")).when(cuentaRepositorio).deleteById(Mockito.<Long>any());
    assertThrows(RecursoInternalServer.class, () -> cuentaServicio.eliminarCuentaPorId(1));
    verify(cuentaRepositorio).deleteById(isA(Long.class));
  }

  @Test
  void testActualizarCuenta() {
    Cliente cliente = new Cliente();
    cliente.setApellido("Apellido");
    cliente.setCorreo("Correo");
    cliente.setCuentas(new ArrayList<>());
    cliente.setFechaCreacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cliente.setFechaModificacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cliente.setFechaNacimiento(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cliente.setIdCliente(1L);
    cliente.setNombre("Nombre");
    cliente.setNumeroIdentificacion(1);
    cliente.setTipoIdentificacion("Tipo Identificacion");

    Cuenta cuentaRecibido = new Cuenta();
    cuentaRecibido.setCliente(cliente);
    cuentaRecibido.setEstado("Estado");
    cuentaRecibido.setExentaGMF("Exenta GMF");
    cuentaRecibido
            .setFechaCreacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cuentaRecibido
            .setFechaModificacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cuentaRecibido.setIdCuenta(1L);
    cuentaRecibido.setNumeroCuenta(1L);
    cuentaRecibido.setSaldo(1L);
    cuentaRecibido.setTipoCuenta("Tipo Cuenta");
    assertThrows(RecursoNoEncontradoExcepcion.class, () -> cuentaServicio.actualizarCuenta(1, cuentaRecibido));
  }
  @Test
  void testActualizarCuenta2() {
    Cliente cliente = new Cliente();
    cliente.setApellido("Apellido");
    cliente.setCorreo("Correo");
    cliente.setCuentas(new ArrayList<>());
    cliente.setFechaCreacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cliente.setFechaModificacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cliente.setFechaNacimiento(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cliente.setIdCliente(1L);
    cliente.setNombre("Nombre");
    cliente.setNumeroIdentificacion(1);
    cliente.setTipoIdentificacion("Tipo Identificacion");

    Cuenta cuentaRecibido = new Cuenta();
    cuentaRecibido.setCliente(cliente);
    cuentaRecibido.setEstado("Estado");
    cuentaRecibido.setExentaGMF("Exenta GMF");
    cuentaRecibido
            .setFechaCreacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cuentaRecibido
            .setFechaModificacion(Date.from(LocalDate.of(1970, 1, 1).atStartOfDay().atZone(ZoneOffset.UTC).toInstant()));
    cuentaRecibido.setIdCuenta(1L);
    cuentaRecibido.setNumeroCuenta(1L);
    cuentaRecibido.setSaldo(1L);
    cuentaRecibido.setTipoCuenta("Tipo Cuenta");
    assertThrows(RecursoNoEncontradoExcepcion.class, () -> cuentaServicio.actualizarCuenta(2, cuentaRecibido));
  }
}

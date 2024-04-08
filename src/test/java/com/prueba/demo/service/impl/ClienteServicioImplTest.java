package com.prueba.demo.service.impl;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

import com.prueba.demo.ecxepcion.RecursoNoEncontradoExcepcion;
import com.prueba.demo.entities.Cliente;
import com.prueba.demo.entities.Cuenta;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.prueba.demo.repository.ClienteRepositorio;
import com.prueba.demo.repository.CuentaRepositorio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class ClienteServicioImplTest {
    @Autowired
    private ClienteServicioImpl clienteServicioImpl;

    @MockBean
    private ClienteRepositorio clienteRepositorio;
    @MockBean
    private CuentaRepositorio cuentaRepositorio;
    @Test
    void testListarClientes() {
        // Given
        ArrayList<Cliente> clienteList = new ArrayList<>();
        when(clienteRepositorio.findAll()).thenReturn(clienteList);
        // When
        List<Cliente> actualListarClientesResult = clienteServicioImpl.listarClientes();
        // Then
        verify(clienteRepositorio).findAll();
        assertTrue(actualListarClientesResult.isEmpty());
        assertSame(clienteList, actualListarClientesResult);
    }
    @Test
    public void testBuscarClientePorId() {
        // Given
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        // Establece otras propiedades relevantes para el cliente

        given(clienteRepositorio.findById(1L)).willReturn(Optional.of(cliente));

        // When
        Cliente clienteGuardado = clienteServicioImpl.buscarClientePorId(1);

        // Then
        assertThat(clienteGuardado).isNotNull();
    }
    @Test
    void testGuardarCliente_EdadValida() {
        // Given
        Cliente cliente = new Cliente();
        cliente.setTipoIdentificacion("corriente");
        cliente.setNumeroIdentificacion((int) 5398176548L);
        cliente.setNombre("Julian Antonio");
        cliente.setApellido("Gonzalez");
        cliente.setCorreo("julian@gmail.com");
        cliente.setFechaNacimiento(Date.from(LocalDate.of(1990, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        cliente.setFechaCreacion(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        cliente.setFechaModificacion(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        given(clienteRepositorio.findById(1L)).willReturn(Optional.of(cliente));
        given(clienteRepositorio.save(cliente)).willReturn(cliente);

        // When
        ResponseEntity<String> respuesta = clienteServicioImpl.guardarCliente(cliente);

        // Then
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(respuesta.getBody()).isEqualTo("Cliente agregado exitosamente");
    }
    @Test
    void testGuardarCliente_EdadInvalida() {
        // Given
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setTipoIdentificacion("corriente");
        cliente.setNumeroIdentificacion((int) 5398176548L);
        cliente.setNombre("Julian Antonio");
        cliente.setApellido("Gonzalez");
        cliente.setCorreo("julian@gmail.com");
        cliente.setFechaNacimiento(Date.from(LocalDate.of(2010, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        cliente.setFechaCreacion(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        cliente.setFechaModificacion(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        // When
        ResponseEntity<String> respuesta = clienteServicioImpl.guardarCliente(cliente);

        // Then
        assertThat(respuesta.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(respuesta.getBody()).isEqualTo("No se puede agregar una persona menor de edad");
    }
    @Test
    void testEliminarClientePorId_ClienteNoEncontrado() {
        // Given
        Integer idCliente = 1;
        when(clienteRepositorio.findById(anyLong())).thenReturn(Optional.empty());

        // When & Then
        assertThrows(RecursoNoEncontradoExcepcion.class, () -> clienteServicioImpl.eliminarClientePorId(idCliente));
    }

    @Test
    void testEliminarClientePorId_ClienteConCuentas() {
        // Given
        long idCliente = 1L;
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setTipoIdentificacion("corriente");
        cliente.setNumeroIdentificacion((int) 5398176548L);
        cliente.setNombre("Julian Antonio");
        cliente.setApellido("Gonzalez");
        cliente.setCorreo("julian@gmail.com");
        cliente.setFechaNacimiento(Date.from(LocalDate.of(2010, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        cliente.setFechaCreacion(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        cliente.setFechaModificacion(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Cuenta cuenta = new Cuenta();
        cuenta.setIdCuenta(1L);
        cuenta.setTipoCuenta("corriente");
        cuenta.setSaldo(1000L);
        cuenta.setCliente(cliente);

        List<Cuenta> cuentasCliente = new ArrayList<>();
        cuentasCliente.add(cuenta);

        when(clienteRepositorio.findById(anyLong())).thenReturn(Optional.of(cliente));
        when(cuentaRepositorio.findByCliente(cliente)).thenReturn(cuentasCliente);

        // When & Then
        assertThrows(RecursoNoEncontradoExcepcion.class, () -> clienteServicioImpl.eliminarClientePorId(Math.toIntExact(idCliente)));
    }

    @Test
    void testEliminarClientePorId_ClienteSinCuentas() {
        // Given
        long idCliente = 1L;
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setTipoIdentificacion("corriente");
        cliente.setNumeroIdentificacion((int) 5398176548L);
        cliente.setNombre("Julian Antonio");
        cliente.setApellido("Gonzalez");
        cliente.setCorreo("julian@gmail.com");
        cliente.setFechaNacimiento(Date.from(LocalDate.of(2010, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        cliente.setFechaCreacion(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        cliente.setFechaModificacion(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        when(clienteRepositorio.findById(anyLong())).thenReturn(Optional.of(cliente));
        when(cuentaRepositorio.findByCliente(cliente)).thenReturn(new ArrayList<>());

        // When
        clienteServicioImpl.eliminarClientePorId(Math.toIntExact(idCliente));

        // Then
        verify(clienteRepositorio, times(1)).deleteById(anyLong());
    }
    @Test
    public void testActualizarCliente() {
        // Given
        int id = 1;
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setTipoIdentificacion("corriente");
        cliente.setNumeroIdentificacion((int) 5398176548L);
        cliente.setNombre("Julian Antonio");
        cliente.setApellido("Gonzalez");
        cliente.setCorreo("julian@gmail.com");
        cliente.setFechaNacimiento(Date.from(LocalDate.of(2010, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        cliente.setFechaCreacion(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        cliente.setFechaModificacion(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

        Cliente clienteExistente = new Cliente();
        clienteExistente.setIdCliente((long) id);
        clienteExistente.setTipoIdentificacion("corriente");
        clienteExistente.setNumeroIdentificacion((int) 5398176548L);
        clienteExistente.setNombre("Nombre Original");
        clienteExistente.setApellido("Apellido Original");
        clienteExistente.setCorreo("original@gmail.com");
        clienteExistente.setFechaNacimiento(Date.from(LocalDate.of(2000, 1, 1).atStartOfDay(ZoneId.systemDefault()).toInstant()));
        clienteExistente.setFechaCreacion(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        clienteExistente.setFechaModificacion(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));

// Simular el comportamiento del repositorio
        when(clienteRepositorio.findById((long) id)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepositorio.save(any(Cliente.class))).thenReturn(clienteExistente);

// When
        Cliente clienteActualizadoResultado = clienteServicioImpl.actualizarCliente(id, cliente);

// Then
        assertNotNull(clienteActualizadoResultado);
        assertEquals(cliente.getIdCliente(), clienteExistente.getIdCliente());
        assertEquals(cliente.getTipoIdentificacion(), clienteExistente.getTipoIdentificacion());
        assertEquals(cliente.getNumeroIdentificacion(), clienteExistente.getNumeroIdentificacion());
        assertEquals(cliente.getNombre(), clienteExistente.getNombre());
        assertEquals(cliente.getApellido(), clienteExistente.getApellido());
        assertEquals(cliente.getCorreo(), clienteExistente.getCorreo());
        assertEquals(cliente.getFechaNacimiento(), clienteExistente.getFechaNacimiento());
        assertEquals(cliente.getFechaCreacion(), clienteExistente.getFechaCreacion());
        assertNotNull(clienteExistente.getFechaModificacion());

    }
}

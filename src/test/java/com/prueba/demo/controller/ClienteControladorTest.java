package com.prueba.demo.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.demo.DemoApplication;
import com.prueba.demo.entities.Cliente;

import com.prueba.demo.service.impl.ClienteServicioImpl;

import java.util.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(ClienteControlador.class)
class ClienteControladorTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ClienteServicioImpl clienteServicio;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testAgregarCliente() throws Exception {
        // Given
        Cliente cliente = new Cliente();
        cliente.setNombre("Nombre");
        cliente.setApellido("Apellido");
        cliente.setCorreo("correo@example.com");
        cliente.setTipoIdentificacion("cc");
        cliente.setNumeroIdentificacion((int) 5311212121L);

        // Simular el comportamiento del servicio al guardar el cliente
        given(clienteServicio.guardarCliente(any(Cliente.class)))
                .willReturn(ResponseEntity.status(HttpStatus.CREATED).body("Cliente agregado exitosamente"));

        // When
        ResultActions response = mockMvc.perform(post("/financiera/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cliente)));

        // Then
        response.andExpect(status().isCreated())
                .andExpect(content().string("Cliente agregado exitosamente"));
    }
    @Test
    void testObtenerClientes() throws Exception {
        // Given
        Cliente cliente1 = new Cliente();
        cliente1.setIdCliente(1L);
        cliente1.setNombre("Cliente1");

        Cliente cliente2 = new Cliente();
        cliente2.setIdCliente(2L);
        cliente2.setNombre("Cliente2");

        List<Cliente> clientes = Arrays.asList(cliente1, cliente2);

        // Configuración del comportamiento del servicio al llamar a listarClientes
        when(clienteServicio.listarClientes()).thenReturn(clientes);

        // When
        ResultActions resultActions = mockMvc.perform(get("/financiera/clientes")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre").value("Cliente1"))
                .andExpect(jsonPath("$[1].nombre").value("Cliente2"));
    }
    @Test
    void testActualizarCliente() throws Exception {
        // Dato de prueba para la actualización
        Cliente clienteRecibido = new Cliente();
        clienteRecibido.setIdCliente(1L);
        clienteRecibido.setNombre("NuevoNombre");

        // Mock del cliente actualizado por el servicio
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setIdCliente(1L);
        clienteActualizado.setNombre("NuevoNombre");

        // Configuración del comportamiento del servicio al llamar a actualizarCliente
        when(clienteServicio.actualizarCliente(anyInt(), any(Cliente.class))).thenReturn(clienteActualizado);

        // Realizar la solicitud PUT a la ruta /clientes/1 con el JSON del cliente recibido
        ResultActions resultActions = mockMvc.perform(put("/financiera/clientes/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRecibido)));

        // Verificar que la solicitud retorna el código 200 OK
        resultActions.andExpect(status().isOk())
                // Verificar que la respuesta es de tipo JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verificar que la respuesta contiene el cliente actualizado con el nuevo nombre
                .andExpect(jsonPath("$.idCliente").value(clienteActualizado.getIdCliente()))
                .andExpect(jsonPath("$.nombre").value(clienteActualizado.getNombre()));
    }
    @Test
    void testEliminarCliente() throws Exception {
        // Mock del cliente que se espera eliminar
        Cliente clienteEliminado = new Cliente();
        clienteEliminado.setIdCliente(1L);
        clienteEliminado.setNombre("ClienteEliminar");

        // Configuración del comportamiento del servicio al buscar el cliente por ID
        when(clienteServicio.buscarClientePorId(1)).thenReturn(clienteEliminado);

        // Realizar la solicitud DELETE a la ruta /clientes/1
        ResultActions resultActions = mockMvc.perform(delete("/financiera/clientes/{id}", 1));

        // Verificar que la solicitud retorna el código 200 OK
        resultActions.andExpect(status().isOk())
                // Verificar que la respuesta es de tipo JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verificar que la respuesta contiene el mensaje de eliminación exitosa
                .andExpect(jsonPath("$.eliminado").value(true));
    }
}

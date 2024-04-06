package com.prueba.demo.controller;

import com.prueba.demo.entities.Cliente;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteControladorTestRestTemplateTests {
    @Autowired
private TestRestTemplate testRestTemplate;
    @Test
    @Order(1)
    void testGuardarCliente() {
        // Given
            Cliente cliente = new Cliente();
            cliente.setIdCliente(1L);
            cliente.setNombre("Julian Antonio");
            cliente.setApellido("Gonzalez");
            cliente.setCorreo("julian@gmail.com");

            // Realizar la solicitud POST para agregar el cliente
            ResponseEntity<Cliente> respuesta = testRestTemplate.postForEntity("http://localhost:8080/financiera/clientes", cliente, Cliente.class);

            // Then
            // Verificar que la respuesta tiene el código de estado 201 (CREATED)
            assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
            // Verificar que el tipo de contenido de la respuesta es JSON
            assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());

            // Obtener el cliente creado en la respuesta
            Cliente clienteCreado = respuesta.getBody();
            assertNotNull(clienteCreado);

            // Verificar que los datos del cliente creado son los esperados
            assertEquals(1L, clienteCreado.getIdCliente());
            assertEquals("Julian Antonio", clienteCreado.getNombre());
            assertEquals("Gonzalez", clienteCreado.getApellido());
            assertEquals("julian@gmail.com", clienteCreado.getCorreo());

        }
        @Test
        @Order(2)
        void testListarCliente(){
                ResponseEntity<Cliente[]> respuesta = testRestTemplate.getForEntity("http://localhost:8080/financiera/clientes",Cliente[].class);
                List<Cliente> clientes = Arrays.asList(Objects.requireNonNull(respuesta.getBody()));

                assertEquals(HttpStatus.OK,respuesta.getStatusCode());
                assertEquals(MediaType.APPLICATION_JSON,respuesta.getHeaders().getContentType());

                assertEquals(1,clientes.size());
                assertEquals(1L,clientes.getFirst().getIdCliente());
                assertEquals("Christian",clientes.getFirst().getNombre());
                assertEquals("Ramirez",clientes.getFirst().getApellido());
                assertEquals("c1@gmail.com",clientes.getFirst().getCorreo());
        }
        @Test
        @Order(3)
        void testEliminarCliente(){
                ResponseEntity<Cliente[]> respuesta = testRestTemplate.getForEntity("http://localhost:8080/financiera/clientes",Cliente[].class);
                List<Cliente> cliente = Arrays.asList(Objects.requireNonNull(respuesta.getBody()));
                assertEquals(1,cliente.size());

                Map<String,Long> pathVariables = new HashMap<>();
                pathVariables.put("id",1L);
                ResponseEntity<Void> exchange = testRestTemplate.exchange("http://localhost:8080/financiera/clientes/{id}", HttpMethod.DELETE,null,Void.class,pathVariables);

                assertEquals(HttpStatus.OK,exchange.getStatusCode());
                assertFalse(exchange.hasBody());

                respuesta = testRestTemplate.getForEntity("http://localhost:8080/financiera/clientes",Cliente[].class);
                cliente = Arrays.asList(Objects.requireNonNull(respuesta.getBody()));
                assertEquals(0,cliente.size());

                ResponseEntity<Cliente> respuestaDetalle = testRestTemplate.getForEntity("http://localhost:8080/financiera/clientes/2",Cliente.class);
                assertEquals(HttpStatus.NOT_FOUND,respuestaDetalle.getStatusCode());
                assertFalse(respuestaDetalle.hasBody());
        }

}

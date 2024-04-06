package com.prueba.demo.controller;

import com.prueba.demo.entities.Cliente;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.hasSize;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteControladorWebTestClientTests {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    @Order(1)
    void testGuardarCliente(){
        //given
        Cliente cliente = new Cliente();
        cliente.setIdCliente(1L);
        cliente.setNombre("Julian Antonio");
        cliente.setApellido("Gonzalez");
        cliente.setCorreo("julian@gmail.com");

        //when
        webTestClient.post().uri("http://localhost:8080/financiera/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cliente)
                .exchange() //envia el request

                //then
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.idCliente").isEqualTo(cliente.getIdCliente())
                .jsonPath("$.nombre").isEqualTo(cliente.getNombre())
                .jsonPath("$.apellido").isEqualTo(cliente.getApellido())
                .jsonPath("$.correo").isEqualTo(cliente.getCorreo());
    }
    @Test
    @Order(2)
    void testListarCliente(){
        webTestClient.get().uri("http://localhost:8080/financiera/clientes").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$[1].nombre").isEqualTo("Adrian")
                .jsonPath("$[1].apellido").isEqualTo("Ramirez")
                .jsonPath("$[1].correo").isEqualTo("aab@gmail.com")
                .jsonPath("$").isArray()
                .jsonPath("$").value(hasSize(2));
    }
    @Test
    @Order(3)
    void testActualizarCliente(){
        Cliente cliente = new Cliente();
        cliente.setNombre("Julian Antonio");
        cliente.setApellido("Gonzalez");
        cliente.setCorreo("julian@gmail.com");

        webTestClient.put().uri("http://localhost:8080/financiera/clientes/2")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cliente)
                .exchange() //emvia el request

                //then
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON);
    }

    @Test
    @Order(4)
    void testEliminarCliente(){
        webTestClient.get().uri("http://localhost:8080/financiera/clientes").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cliente.class)
                .hasSize(2);

        webTestClient.delete().uri("http://localhost:8080/financiera/clientes/2")
                .exchange()
                .expectStatus().isOk();

        webTestClient.get().uri("http://localhost:8080/financiera/clientes").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBodyList(Cliente.class)
                .hasSize(1);

        webTestClient.get().uri("http://localhost:8080/financiera/clientes/1").exchange()
                .expectStatus().is4xxClientError();
    }
}

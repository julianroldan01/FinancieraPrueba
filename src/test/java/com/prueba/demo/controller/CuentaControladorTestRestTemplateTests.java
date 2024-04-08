package com.prueba.demo.controller;

import com.prueba.demo.entities.Cliente;
import com.prueba.demo.entities.Cuenta;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CuentaControladorTestRestTemplateTests {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Test
    @Order(1)
    void testGuardarCuenta() {
        // Given
        Cuenta cuenta = new Cuenta();
        cuenta.setIdCuenta(1L);
        cuenta.setNumeroCuenta(5311212121L);
        cuenta.setTipoCuenta("corriente");
        cuenta.setSaldo(10000L);
        cuenta.setExentaGMF("no");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Cuenta> request = new HttpEntity<>(cuenta, headers);
        // Realizar la solicitud POST para agregar el cliente
        ResponseEntity<String> respuesta = testRestTemplate.postForEntity("http://localhost:8080/financiera/cuentas", request, String.class);

        // Then
        // Verificar que el tipo de contenido de la respuesta es JSON
        assertEquals(MediaType.APPLICATION_JSON, respuesta.getHeaders().getContentType());
        // Verificar que la respuesta tiene el código de estado 201 (CREATED)
        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());

        // Obtener el cliente creado en la respuesta
        String cuentaCreado = respuesta.getBody();
        assertNotNull(cuentaCreado);

        // Verificar que los datos del cliente creado son los esperados
        assertEquals("Cliente agregado exitosamente", cuentaCreado);
    }
    @Test
    @Order(2)
    void testListarCuenta(){
        ResponseEntity<Cuenta[]> respuesta = testRestTemplate.getForEntity("http://localhost:8080/financiera/cuentas",Cuenta[].class);
        List<Cuenta> cuentas = Arrays.asList(Objects.requireNonNull(respuesta.getBody()));

        assertEquals(HttpStatus.OK,respuesta.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON,respuesta.getHeaders().getContentType());

        assertEquals(1,cuentas.size());
        assertEquals(1L,cuentas.getFirst().getIdCuenta());
        assertEquals(5311212121L,cuentas.getFirst().getNumeroCuenta());
        assertEquals("Ramirez",cuentas.getFirst().getTipoCuenta());
        assertEquals(200000L,cuentas.getFirst().getSaldo());
    }
    @Test
    @Order(3)
    void testEliminarCuenta(){
        ResponseEntity<Cuenta[]> respuesta = testRestTemplate.getForEntity("http://localhost:8080/financiera/cuentas",Cuenta[].class);
        List<Cuenta> cuenta = Arrays.asList(Objects.requireNonNull(respuesta.getBody()));
        assertEquals(1,cuenta.size());

        Map<String,Long> pathVariables = new HashMap<>();
        pathVariables.put("id",1L);
        ResponseEntity<Void> exchange = testRestTemplate.exchange("http://localhost:8080/financiera/cuentas/{id}", HttpMethod.DELETE,null,Void.class,pathVariables);

        assertEquals(HttpStatus.OK,exchange.getStatusCode());
        assertFalse(exchange.hasBody());

        respuesta = testRestTemplate.getForEntity("http://localhost:8080/financiera/cuentas",Cuenta[].class);
        cuenta = Arrays.asList(Objects.requireNonNull(respuesta.getBody()));
        assertEquals(0,cuenta.size());

        ResponseEntity<Cuenta> respuestaDetalle = testRestTemplate.getForEntity("http://localhost:8080/financiera/cuentas/2",Cuenta.class);
        assertEquals(HttpStatus.NOT_FOUND,respuestaDetalle.getStatusCode());
        assertFalse(respuestaDetalle.hasBody());
    }
}

package com.prueba.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.demo.DemoApplication;
import com.prueba.demo.entities.Cliente;
import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.service.impl.CuentaServicioImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(CuentaControlador.class)
@Import(DemoApplication.class)
public class CuentaControladorTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CuentaServicioImpl cuentaServicio;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void testAgregarCuenta() throws Exception {
        // Given
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(5311212121L);
        cuenta.setTipoCuenta("corriente");
        cuenta.setSaldo(10000L);
        cuenta.setExentaGMF("no");

        // Simular el comportamiento del servicio al guardar la cuenta
        when(cuentaServicio.guardarCuenta(any(Cuenta.class)))
                .thenReturn(cuenta);

        // When
        ResultActions response = mockMvc.perform(post("/financiera/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuenta)));

        // Then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCuenta").value(5311212121L))
                .andExpect(jsonPath("$.tipoCuenta").value("corriente"))
                .andExpect(jsonPath("$.saldo").value(10000L))
                .andExpect(jsonPath("$.exentaGMF").value("no"));
    }

    @Test
    void testObtenerCuentas() throws Exception {
        // Given
        Cuenta cuenta1 = new Cuenta();
        cuenta1.setIdCuenta(1L);
        cuenta1.setNumeroCuenta(5311212121L);
        cuenta1.setTipoCuenta("corriente");
        cuenta1.setSaldo(10000L);
        cuenta1.setExentaGMF("no");

        Cuenta cuenta2 = new Cuenta();
        cuenta2.setIdCuenta(2L);
        cuenta2.setNumeroCuenta(5311213114L);
        cuenta2.setTipoCuenta("corriente");
        cuenta2.setSaldo(40000L);
        cuenta2.setExentaGMF("no");

        List<Cuenta> cuentas = Arrays.asList(cuenta1, cuenta2);

        // Configuración del comportamiento del servicio al llamar a listarClientes
        when(cuentaServicio.listarCuentas()).thenReturn(cuentas);

        // When
        ResultActions resultActions = mockMvc.perform(get("/financiera/cuentas")
                .contentType(MediaType.APPLICATION_JSON));

        // Then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].numeroCuenta").value(5311212121L))
                .andExpect(jsonPath("$[1].numeroCuenta").value(5311213114L));
    }
    @Test
    void testActualizarCliente() throws Exception {
        // Dato de prueba para la actualización
        Cuenta cuentaRecibido = new Cuenta();
        cuentaRecibido.setIdCuenta(1L);
        cuentaRecibido.setNumeroCuenta(5311212121L);

        // Mock del cliente actualizado por el servicio
        Cuenta cuentaActualizado = new Cuenta();
        cuentaActualizado.setIdCuenta(1L);
        cuentaActualizado.setNumeroCuenta(5311213418L);

        // Configuración del comportamiento del servicio al llamar a actualizarCliente
        when(cuentaServicio.actualizarCuenta(anyInt(), any(Cuenta.class))).thenReturn(cuentaActualizado);

        // Realizar la solicitud PUT a la ruta /clientes/1 con el JSON del cliente recibido
        ResultActions resultActions = mockMvc.perform(put("/financiera/cuentas/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuentaRecibido)));

        // Verificar que la solicitud retorna el código 200 OK
        resultActions.andExpect(status().isOk())
                // Verificar que la respuesta es de tipo JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verificar que la respuesta contiene el cliente actualizado con el nuevo nombre
                .andExpect(jsonPath("$.idCuenta").value(cuentaActualizado.getIdCuenta()))
                .andExpect(jsonPath("$.numeroCuenta").value(cuentaActualizado.getNumeroCuenta()));
    }
    @Test
    void testEliminarCliente() throws Exception {
        // Mock del cliente que se espera eliminar
        Cuenta cuentaEliminado = new Cuenta();
        cuentaEliminado.setIdCuenta(1L);
        cuentaEliminado.setNumeroCuenta(5311212121L);

        // Configuración del comportamiento del servicio al buscar el cliente por ID
        when(cuentaServicio.buscarCuentaPorId(1)).thenReturn(cuentaEliminado);

        // Realizar la solicitud DELETE a la ruta /clientes/1
        ResultActions resultActions = mockMvc.perform(delete("/financiera/cuentas/{id}", 1));

        // Verificar que la solicitud retorna el código 200 OK
        resultActions.andExpect(status().isOk())
                // Verificar que la respuesta es de tipo JSON
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verificar que la respuesta contiene el mensaje de eliminación exitosa
                .andExpect(jsonPath("$.eliminado").value(true));
    }

}

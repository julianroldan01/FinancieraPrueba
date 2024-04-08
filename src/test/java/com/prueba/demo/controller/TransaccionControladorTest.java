package com.prueba.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prueba.demo.entities.Cuenta;
import com.prueba.demo.entities.Transaccion;
import com.prueba.demo.repository.CuentaRepositorio;
import com.prueba.demo.service.impl.TransaccionServicioImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransaccionControlador.class)
@AutoConfigureMockMvc
public class TransaccionControladorTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CuentaRepositorio cuentaRepositorio;
    @MockBean
    private TransaccionServicioImpl transaccionServicio;
    @Autowired
    private TransaccionControlador transaccionControlador;
    @Autowired
    private ObjectMapper objectMapper;
    @Test
    void testRealizarConsignacion() throws Exception {
        // Given
        Long idCuenta = 1L;
        Long monto = 100L;
        Cuenta cuenta = new Cuenta();
        cuenta.setIdCuenta(idCuenta);
        cuenta.setSaldo(200L); // Saldo suficiente para la consignación

        Transaccion transaccionMock = new Transaccion();
        transaccionMock.setId(1L);
        transaccionMock.setTipo("Consignación");
        transaccionMock.setFecha(transaccionMock.getFecha());
        transaccionMock.setCuentaOrigen(cuenta);
        transaccionMock.setCuentaDestino(cuenta);

        // Mocking
        when(cuentaRepositorio.findById(idCuenta)).thenReturn(Optional.of(cuenta));
        when(transaccionServicio.realizarConsignacion(cuenta, monto)).thenReturn(transaccionMock);

        // When/Then
        mockMvc.perform(post("/financiera/consignacion")
                        .param("idCuenta", idCuenta.toString())
                        .param("monto", monto.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string("Consignación realizada correctamente. ID de transacción: " + transaccionMock.getId()));
    }
    @Test
    void testMovimientos() throws Exception {
        when(transaccionServicio.movimiento()).thenReturn(new ArrayList<>());
        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders.get("/financiera/movimientos");
        MockMvcBuilders.standaloneSetup(transaccionControlador)
                .build()
                .perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string("[]"));
    }
}

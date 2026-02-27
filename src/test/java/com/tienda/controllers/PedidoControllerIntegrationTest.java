package com.tienda.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tienda.dtos.pedido.PedidoRequest;
import com.tienda.dtos.pedido.PedidoResponse;
import com.tienda.enums.MetodoPago;
import com.tienda.enums.TipoVenta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/data-test.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class PedidoControllerIntegrationTest {

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private ObjectMapper objectMapper;
    private MockMvc mockMvc;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public ObjectMapper objectMapper() {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper;
        }
    }

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Crear pedido local exitosamente - Happy Path")
    @WithMockUser(roles = {"ADMIN"})
    void crearPedidoLocalExitosoTest() throws Exception {
        PedidoRequest request = new PedidoRequest();
        request.setNombreCliente("Juan Perez");
        request.setTelefono("3415551234");
        request.setMetodoPago(MetodoPago.EFECTIVO);
        request.setTipoVenta(TipoVenta.LOCAL);

        PedidoRequest.DetalleRequest detalle1 = new PedidoRequest.DetalleRequest();
        detalle1.setProductId(1L);
        detalle1.setCantidad(2);

        PedidoRequest.DetalleRequest detalle2 = new PedidoRequest.DetalleRequest();
        detalle2.setProductId(2L);
        detalle2.setCantidad(1);

        request.setProductos(java.util.List.of(detalle1, detalle2));

        mockMvc.perform(post("/pedido/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombreCliente").value("Juan Perez"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.tipoVenta").value("LOCAL"))
                .andExpect(jsonPath("$.metodoPago").value("EFECTIVO"))
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.fecha").exists());
    }

    @Test
    @DisplayName("Crear pedido con envío exitosamente")
    @WithMockUser(roles = {"ADMIN"})
    void crearPedidoConEnvioExitosoTest() throws Exception {
        PedidoRequest request = new PedidoRequest();
        request.setNombreCliente("María Gomez");
        request.setTelefono("3415555678");
        request.setDireccion("Calle Falsa 123");
        request.setCodigoPostal("3200");
        request.setLocalidad("Concordia");
        request.setMetodoPago(MetodoPago.TRANSFERENCIA);
        request.setTipoVenta(TipoVenta.ENVIO);

        PedidoRequest.DetalleRequest detalle = new PedidoRequest.DetalleRequest();
        detalle.setProductId(3L);
        detalle.setCantidad(3);

        request.setProductos(java.util.List.of(detalle));

        mockMvc.perform(post("/pedido/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.nombreCliente").value("María Gomez"))
                .andExpect(jsonPath("$.estado").value("EN_PREPARACION"))
                .andExpect(jsonPath("$.tipoVenta").value("ENVIO"))
                .andExpect(jsonPath("$.direccion").value("Calle Falsa 123"))
                .andExpect(jsonPath("$.codigoPostal").value("3200"))
                .andExpect(jsonPath("$.localidad").value("Concordia"))
                .andExpect(jsonPath("$.metodoPago").value("TRANSFERENCIA"));
    }

    @Test
    @DisplayName("Error al crear pedido con datos inválidos")
    @WithMockUser(roles = {"ADMIN"})
    void crearPedidoConDatosInvalidosTest() throws Exception {
        PedidoRequest request = new PedidoRequest();
        request.setNombreCliente("Pedro Lopez");

        mockMvc.perform(post("/pedido/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Error al crear pedido con stock insuficiente")
    @WithMockUser(roles = {"ADMIN"})
    void crearPedidoConStockInsuficienteTest() throws Exception {
        PedidoRequest request = new PedidoRequest();
        request.setNombreCliente("Carlos Sanchez");
        request.setMetodoPago(MetodoPago.EFECTIVO);
        request.setTipoVenta(TipoVenta.LOCAL);

        PedidoRequest.DetalleRequest detalle = new PedidoRequest.DetalleRequest();
        detalle.setProductId(4L);
        detalle.setCantidad(100);

        request.setProductos(java.util.List.of(detalle));

        mockMvc.perform(post("/pedido/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("Obtener lista de pedidos paginada")
    @WithMockUser(roles = {"ADMIN"})
    void obtenerPedidosPaginadosTest() throws Exception {
        mockMvc.perform(get("/pedido/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10))
                .andExpect(jsonPath("$.totalElements").exists())
                .andExpect(jsonPath("$.totalPages").exists());
    }

    @Test
    @DisplayName("Obtener resumen de pedidos")
    @WithMockUser(roles = {"ADMIN"})
    void obtenerResumenPedidosTest() throws Exception {
        mockMvc.perform(get("/pedido/resumen"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").exists())
                .andExpect(jsonPath("$.pendientes").exists())
                .andExpect(jsonPath("$.en_preparacion").exists())
                .andExpect(jsonPath("$.entregados").exists())
                .andExpect(jsonPath("$.cancelados").exists());
    }

    @Test
    @DisplayName("Cambiar estado de pedido existente")
    @WithMockUser(roles = {"ADMIN"})
    void cambiarEstadoPedidoTest() throws Exception {
        PedidoRequest request = new PedidoRequest();
        request.setNombreCliente("Diego Luna");
        request.setMetodoPago(MetodoPago.EFECTIVO);
        request.setTipoVenta(TipoVenta.LOCAL);

        PedidoRequest.DetalleRequest detalle = new PedidoRequest.DetalleRequest();
        detalle.setProductId(2L);
        detalle.setCantidad(1);

        request.setProductos(java.util.List.of(detalle));

        var createResult = mockMvc.perform(post("/pedido/")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String response = createResult.getResponse().getContentAsString();
        PedidoResponse pedidoCreado = objectMapper.readValue(response, PedidoResponse.class);

        mockMvc.perform(patch("/pedido/" + pedidoCreado.getId())
                        .with(csrf())
                        .param("estadoPedido", "ENTREGADO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(pedidoCreado.getId()))
                .andExpect(jsonPath("$.estado").value("ENTREGADO"));
    }
}
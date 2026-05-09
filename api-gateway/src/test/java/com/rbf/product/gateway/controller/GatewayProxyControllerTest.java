package com.rbf.product.gateway.controller;

import com.rbf.product.gateway.routing.GatewayRoutingService;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GatewayProxyControllerTest {

    @Test
    void proxiesApiRoutesThroughRoutingService() throws Exception {
        GatewayRoutingService routingService = mock(GatewayRoutingService.class);
        when(routingService.forward(any(MockHttpServletRequest.class), isNull()))
                .thenReturn(ResponseEntity.ok("ok".getBytes()));
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new GatewayProxyController(routingService)).build();

        mockMvc.perform(get("/api/products").header("Authorization", "Bearer token").header("X-ORG-ID", "101"))
                .andExpect(status().isOk())
                .andExpect(content().string("ok"));

        verify(routingService).forward(any(MockHttpServletRequest.class), isNull());
    }
}

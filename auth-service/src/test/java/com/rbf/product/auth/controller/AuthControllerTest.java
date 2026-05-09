package com.rbf.product.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbf.product.auth.dto.LoginRequest;
import com.rbf.product.auth.dto.LoginResponse;
import com.rbf.product.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private AuthService authService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        authService = mock(AuthService.class);
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
        validator.afterPropertiesSet();
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(authService))
                .setValidator(validator)
                .build();
    }

    @Test
    void loginReturnsJwtClaimsPayload() throws Exception {
        LoginRequest request = new LoginRequest();
        request.setOrgId(101L);
        request.setUsername("admin_a");
        request.setPassword("Admin@123");
        when(authService.login(org.mockito.ArgumentMatchers.any(LoginRequest.class)))
                .thenReturn(new LoginResponse("jwt-token", 1L, "admin_a", 101L,
                        Set.of("ADMIN"), Set.of("PRODUCT_CREATE", "BILLING_CREATE")));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.orgId").value(101))
                .andExpect(jsonPath("$.roles[0]").value("ADMIN"));
    }

    @Test
    void loginRejectsInvalidPayload() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}

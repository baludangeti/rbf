package com.rbf.product.console.controller;

import com.rbf.product.console.client.AuthClient;
import com.rbf.product.console.client.dto.LoginResponse;
import com.rbf.product.console.session.SessionKeys;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LoginControllerTest {

    @Test
    void successfulLoginStoresJwtOrgUserRolesAndPermissionsInSession() throws Exception {
        AuthClient authClient = mock(AuthClient.class);
        LoginResponse response = new LoginResponse();
        response.setToken("jwt-token");
        response.setOrgId(101L);
        response.setOrganizationName("Org A");
        response.setUserId(1L);
        response.setUsername("admin_a");
        response.setRoles(Set.of("ADMIN"));
        response.setPermissions(Set.of("PRODUCT_CREATE"));
        when(authClient.login(101L, "admin_a", "Admin@123")).thenReturn(response);
        MockHttpSession session = new MockHttpSession();
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(new LoginController(authClient)).build();

        mockMvc.perform(post("/login")
                        .session(session)
                        .param("orgId", "101")
                        .param("username", "admin_a")
                        .param("password", "Admin@123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/console/dashboard"));

        org.assertj.core.api.Assertions.assertThat(session.getAttribute(SessionKeys.JWT_TOKEN)).isEqualTo("jwt-token");
        org.assertj.core.api.Assertions.assertThat(session.getAttribute(SessionKeys.ORG_ID)).isEqualTo(101L);
        org.assertj.core.api.Assertions.assertThat(session.getAttribute(SessionKeys.USERNAME)).isEqualTo("admin_a");
        org.assertj.core.api.Assertions.assertThat(session.getAttribute(SessionKeys.PERMISSIONS)).isEqualTo(Set.of("PRODUCT_CREATE"));
    }
}

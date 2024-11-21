package org.dci.theratrack.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.dci.theratrack.config.TestSecurityConfig;
import org.dci.theratrack.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ContextConfiguration(classes = {TestSecurityConfig.class, AuthController.class})
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Test
    public void testLogin_Success() throws Exception {
        // Mock AuthService behavior
        when(authService.authenticate(anyString(), anyString())).thenReturn("mock-jwt-token");

        // Perform POST request
        mockMvc.perform(post("/api/auth/login")
                        .param("username", "admin")
                        .param("password", "admin123"))
                .andExpect(status().isOk()) // Expect 200 OK
                .andExpect(content().string("mock-jwt-token")); // Verify token value
    }

    @Test
    public void testLogin_InvalidCredentials() throws Exception {
        // Mock AuthService behavior for invalid credentials
        Mockito.doThrow(new RuntimeException("Invalid credentials"))
                .when(authService).authenticate(anyString(), anyString());



        // Perform POST request
        mockMvc.perform(post("/api/auth/login")
                        .param("username", "admin")
                        .param("password", "admin123"))
                .andExpect(status().isUnauthorized()); // Verify 401 Unauthorized status
    }

    // DTO for request payload
    private static class LoginRequest {
        private String username;
        private String password;

        public LoginRequest(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }
    }
}

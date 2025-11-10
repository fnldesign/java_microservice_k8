package com.example.microservice.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@DisplayName("ApiController Integration Tests")
class ApiControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Value("${api.key}")
  private String API_KEY;

  @Test
  @DisplayName("Should access health endpoint without authentication")
  void shouldAccessHealthEndpointWithoutAuth() throws Exception {
    mockMvc.perform(get("/api/health"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value("UP"));
  }

  @Test
  @DisplayName("Should access hello endpoint with authentication")
  void shouldAccessHelloEndpointWithAuth() throws Exception {
    mockMvc.perform(get("/api/hello")
        .header("X-API-Key", API_KEY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Hello from java-microservice-k8"))
        .andExpect(jsonPath("$.ts").exists());
  }

  @Test
  @DisplayName("Should reject hello endpoint without authentication")
  void shouldRejectHelloEndpointWithoutAuth() throws Exception {
    mockMvc.perform(get("/api/hello"))
        .andExpect(status().isForbidden());
  }

  @Test
  @DisplayName("Should reject hello endpoint with invalid API key")
  void shouldRejectHelloEndpointWithInvalidApiKey() throws Exception {
    mockMvc.perform(get("/api/hello")
        .header("X-API-Key", "invalid-key"))
        .andExpect(status().isForbidden());
  }
}

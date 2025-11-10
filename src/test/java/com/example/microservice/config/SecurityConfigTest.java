package com.example.microservice.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.SecurityFilterChain;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("SecurityConfig Tests")
class SecurityConfigTest {

  @Autowired
  private SecurityFilterChain securityFilterChain;

  @Test
  @DisplayName("Should load security filter chain")
  void shouldLoadSecurityFilterChain() {
    assertNotNull(securityFilterChain);
  }
}

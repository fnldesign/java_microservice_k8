package com.example.microservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("Application Context Tests")
class JavaMicroserviceK8ApplicationTests {

  @Test
  @DisplayName("Should load application context")
  void contextLoads() {
    // This test will fail if the application context cannot start
  }
}

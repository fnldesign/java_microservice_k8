package com.example.microservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "General", description = "General API endpoints")
public class ApiController {

  private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

  @GetMapping("/hello")
  @Operation(summary = "Hello endpoint", description = "Returns a simple greeting message with timestamp")
  @SecurityRequirement(name = "apiKey")
  public ResponseEntity<Map<String, Object>> hello() {
    Map<String, Object> payload = Map.of(
        "message", "Hello from java-microservice-k8",
        "ts", Instant.now().toString());
    logger.info("hello endpoint called", payload);
    return ResponseEntity.ok(payload);
  }

  @GetMapping("/health")
  @Operation(summary = "Health check", description = "Returns the health status of the application (public endpoint)")
  public ResponseEntity<Map<String, String>> health() {
    logger.debug("health check called");
    return ResponseEntity.ok(Map.of("status", "UP"));
  }
}

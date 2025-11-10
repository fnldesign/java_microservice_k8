package com.example.microservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Java Microservice K8s API")
            .version("1.0.0")
            .description("Simple microservice boilerplate with authentication, persistence and Kubernetes deployment")
            .contact(new Contact()
                .name("API Support")
                .email("support@example.com")))
        .components(new Components()
            .addSecuritySchemes("apiKey", new SecurityScheme()
                .type(SecurityScheme.Type.APIKEY)
                .in(SecurityScheme.In.HEADER)
                .name("X-API-Key")
                .description("API Key authentication via X-API-Key header")));
  }
}

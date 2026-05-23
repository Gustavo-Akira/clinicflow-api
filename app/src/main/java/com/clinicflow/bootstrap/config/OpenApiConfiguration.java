package com.clinicflow.bootstrap.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    OpenAPI clinicflowOpenApi() {
        return new OpenAPI().info(
            new Info()
                .title("ClinicFlow API")
                .description("Bootstrap API for the modular monolith backend.")
                .version("v0.1.0")
                .contact(new Contact().name("ClinicFlow"))
        );
    }
}

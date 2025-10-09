package com.hotel.reservas.reactivo.shared.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    String securitySchemaName = "bearerAuth";
    @Bean
    public OpenAPI hotelApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Reservas API")
                        .description("backend reactivo para gestión de reservas de hotel")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Cristian Orizano")))
                        .addSecurityItem(
                                new SecurityRequirement()
                                        .addList(securitySchemaName)// Agregar el requisito de seguridad
                ) .components(
                        new Components().addSecuritySchemes(
                                securitySchemaName,
                                new SecurityScheme()
                                        .name(securitySchemaName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT") // Definir el esquema Bearer para el token JWT
                        )
                );
    }
}

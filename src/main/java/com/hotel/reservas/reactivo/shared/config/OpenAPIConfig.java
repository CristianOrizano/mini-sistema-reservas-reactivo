package com.hotel.reservas.reactivo.shared.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @Bean
    public OpenAPI hotelApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Reservas API")
                        .description("API reactiva para gestión de habitaciones y categorías")
                        .version("1.0.0"));
    }
}

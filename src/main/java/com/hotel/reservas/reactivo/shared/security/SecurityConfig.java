package com.hotel.reservas.reactivo.shared.security;

import com.hotel.reservas.reactivo.shared.security.jwt.JwtAuthenticationEntryPoint;
import com.hotel.reservas.reactivo.shared.security.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    // Codificador de contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Authentication Manager reactivo
    /*@Bean
    public ReactiveAuthenticationManager authenticationManager() {
        UserDetailsRepositoryReactiveAuthenticationManager authManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authManager.setPasswordEncoder(passwordEncoder());
        return authManager;
    } */

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeExchange(ex -> ex
                        // Swagger / OpenAPI (UI + docs)
                        .pathMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-ui/index.html",
                                "/webjars/**"
                        ).permitAll()
                        // Endpoints de autenticación públicos
                        .pathMatchers("/api/usuario/**", "/api/auth/login").permitAll()
                        // GET: accesible por ADMIN y USER
                        .pathMatchers(HttpMethod.GET, "/api/habitacion/**").hasAnyAuthority("ADMIN", "USER")
                        // POST, PUT, DELETE: solo ADMIN
                        .pathMatchers(HttpMethod.POST, "/api/habitacion/**").hasAuthority("ADMIN")
                        .pathMatchers(HttpMethod.PUT, "/api/habitacion/**").hasAuthority("ADMIN")
                        .pathMatchers(HttpMethod.DELETE, "/api/habitacion/**").hasAuthority("ADMIN")
                        // CORS preflight
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        // El resto requiere JWT
                        .anyExchange().authenticated()
                )
                // Registrar el filtro JWT
                .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}

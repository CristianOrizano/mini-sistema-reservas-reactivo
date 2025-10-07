package com.hotel.reservas.reactivo.shared.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;                              // tu util JWT
    private final ReactiveUserDetailsService userDetailsService; // tu CustomUserDetailsService
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        // Si no hay header o no es Bearer, seguir sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println(">>>>> Token vacio o inválido");
            return chain.filter(exchange);
        }

        String token = authHeader.substring(7);

        // Validamos el token (validateToken debe atrapar excepciones y devolver boolean)
        if (!jwtUtil.validateToken(token)) {
            return chain.filter(exchange);
        }

        // Extraemos el username del token (subject)
        String username = jwtUtil.extractUsername(token);

        // Buscamos el UserDetails (reactivo) y, si existe, inyectamos el SecurityContext
        return userDetailsService.findByUsername(username)
                .flatMap(userDetails -> {
                    System.out.println("[JwtFilter] Usuario encontrado en BD: " + userDetails.getUsername());
                    System.out.println(" [JwtFilter] Roles: " + userDetails.getAuthorities());
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextImpl securityContext = new SecurityContextImpl(authentication);

                    // Ejecutamos la cadena de filtros con el SecurityContext reactivo
                    return chain.filter(exchange)
                            .contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                })
                // Si no se encuentra el usuario en BD, seguimos sin autenticar (o  devolver error)
                .switchIfEmpty(chain.filter(exchange));
    }
}

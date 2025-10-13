package com.hotel.reservas.reactivo.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleCustomException(CustomException ex, ServerWebExchange exchange) {
        ErrorResponse error = new ErrorResponse(
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(), // Ejemplo: "Not Found"
                ex.getMessage(),
                exchange.getRequest().getPath().toString(),     // URI del request
                LocalDateTime.now()
        );

        return Mono.just(ResponseEntity.status(ex.getStatus()).body(error));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationException(WebExchangeBindException ex, ServerWebExchange exchange) {
        String errors = ex.getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                errors,
                exchange.getRequest().getPath().toString(),
                LocalDateTime.now()
        );

        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

    // Manejo de cualquier excepción no controlada
    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneralException(Exception ex, ServerWebExchange exchange) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                ex.getMessage(),
                exchange.getRequest().getPath().toString(),
                LocalDateTime.now()
        );
        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error));
    }

}

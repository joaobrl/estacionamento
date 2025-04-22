package com.estacionamento.api.infra.exceptions;

import com.estacionamento.api.domain.exceptions.GenericException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleEntityNotFoundException() {
        return buildErrorResponse(HttpStatus.NOT_FOUND, "Recurso não encontrado");
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationException(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(ValidacaoErroDto::new).toList());
    }

    @ExceptionHandler(GenericException.class)
    public ResponseEntity<?> handleGenericException(GenericException ex) {
        return buildErrorResponse(HttpStatus.valueOf(ex.getStatusCode()), ex.getMessage());
    }

    private record ValidacaoErroDto(String campo, String mensagem) {
        public ValidacaoErroDto(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }

    private record ErrorDto(String detalhe) {}

    private ResponseEntity<ErrorDto> buildErrorResponse(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorDto(message));
    }
}
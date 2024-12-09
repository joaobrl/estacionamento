package com.estacionamento.api.infra.exceptions;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity NotFoundException404 () {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity BadRequest400 (MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(ValidacaoErroDto::new).toList());
    }

    public class EstacionamentoNaoEncontradoException extends RuntimeException {
        public EstacionamentoNaoEncontradoException(String message) {
            super(message);
        }
    }

    public class ErroAoCadastrarEstacionamentoException extends RuntimeException {
        public ErroAoCadastrarEstacionamentoException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private record ValidacaoErroDto(String campo, String mensagem) {
        public ValidacaoErroDto(FieldError erro) {
            this(erro.getField(), erro.getDefaultMessage());
        }
    }
}

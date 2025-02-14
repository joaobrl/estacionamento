package com.estacionamento.api.infra.exceptions;

import com.estacionamento.api.domain.exceptions.*;
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

    // Exception para quando falta algum campo no body
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleValidationException(MethodArgumentNotValidException ex) {
        var erros = ex.getFieldErrors();
        return ResponseEntity.badRequest().body(erros.stream().map(ValidacaoErroDto::new).toList());
    }
    // Exception para quando o recurso (id) não é encontrado
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<?> handleRecursoNaoEncontradoException(RecursoNaoEncontradoException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }
    // Exception para quando o estacionamento estiver com a capacidade maxima de vagas
    @ExceptionHandler(EstacionamentoLotadoException.class)
    public ResponseEntity<?> handleEstacionamentoLotadoException(EstacionamentoLotadoException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // Exception para quando o veiculo já tem um ticket em aberto
    @ExceptionHandler(VeiculoComTicketEmAberto.class)
    public ResponseEntity<?> handleVeiculoComTicketEmAberto(VeiculoComTicketEmAberto ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // Exception para quando o estacionamento estiver com a capacidade maxima de vagas
    @ExceptionHandler(PlanoMensalObrigatorioException.class)
    public ResponseEntity<?> handlePlanoMensalObrigatorioException(PlanoMensalObrigatorioException ex) {
        return buildErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    // Exception para quando a vaga está ocupada
    @ExceptionHandler(VagaNaoDisponivelException.class)
    public ResponseEntity<?> handleVagaNaoDisponivelException(VagaNaoDisponivelException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // Exception para quando o nome da vaga já está sendo usado nesse estacionamento
    @ExceptionHandler(VagaComNumeroJaExistenteException.class)
    public ResponseEntity<?> handleVagaComNomeJaExistenteException(VagaComNumeroJaExistenteException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // Exception para quando o ticket já foi fechado
    @ExceptionHandler(TicketJaFechadoException.class)
    public ResponseEntity<?> handleTicketJaFechadoException(TicketJaFechadoException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    // Exception para quando o ticket já foi pago.
    @ExceptionHandler(TicketJaPagoException.class)
    public ResponseEntity<?> handleTicketJaPagoException(TicketJaPagoException ex) {
        return buildErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
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

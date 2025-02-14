package com.estacionamento.api.domain.exceptions;

public class RecursoNaoEncontradoException extends RuntimeException {
    public RecursoNaoEncontradoException(String recurso, Long id) {
        super(recurso + " com ID '" + id + "' não foi encontrado.");
    }
}

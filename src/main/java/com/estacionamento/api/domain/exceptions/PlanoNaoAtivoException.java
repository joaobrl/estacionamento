package com.estacionamento.api.domain.exceptions;

public class PlanoNaoAtivoException extends RuntimeException {
    public PlanoNaoAtivoException(String recurso) {
        super(recurso);
    }
}

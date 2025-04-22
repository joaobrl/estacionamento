package com.estacionamento.api.domain.exceptions;

public class VeiculoEstacionadoException extends RuntimeException {
    public VeiculoEstacionadoException(String recurso) {
        super(recurso);
    }
}

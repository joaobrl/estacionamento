package com.estacionamento.api.domain.exceptions;

public class HorarioNaoCadastradoException extends RuntimeException{
    public HorarioNaoCadastradoException(String message) {
        super(message);
    }
}

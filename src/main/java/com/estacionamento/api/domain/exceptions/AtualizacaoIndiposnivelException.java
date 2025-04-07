package com.estacionamento.api.domain.exceptions;

public class AtualizacaoIndiposnivelException extends RuntimeException{
    public AtualizacaoIndiposnivelException() {
        super("Atualização não está disponivel para essa vaga.");
    }
}

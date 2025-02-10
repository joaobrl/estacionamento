package com.estacionamento.api.domain.exceptions;

public class VagaNaoDisponivelException extends RuntimeException{
    public VagaNaoDisponivelException() {
        super("Não há vagas disponíveis para esse tipo de veículo.");
    }
}

package com.estacionamento.api.domain.exceptions;

public class PlanoMensalObrigatorioException extends RuntimeException{
    public PlanoMensalObrigatorioException() {
        super("Plano mensal obrigatório para estacionamento.");
    }
}

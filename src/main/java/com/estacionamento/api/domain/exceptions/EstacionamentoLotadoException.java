package com.estacionamento.api.domain.exceptions;

public class EstacionamentoLotadoException extends RuntimeException {
    public EstacionamentoLotadoException(Long id) {
        super("Estacionamento com ID '" + id + "' está com capacidade máxima atingida.");
    }
}

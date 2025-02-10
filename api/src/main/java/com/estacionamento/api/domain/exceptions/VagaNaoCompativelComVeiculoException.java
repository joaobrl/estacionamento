package com.estacionamento.api.domain.exceptions;

public class VagaNaoCompativelComVeiculoException extends RuntimeException{
    public VagaNaoCompativelComVeiculoException(Long id) {
        super("Veiculo com ID '" + id + "' não é compatível com a vaga.");
    }
}

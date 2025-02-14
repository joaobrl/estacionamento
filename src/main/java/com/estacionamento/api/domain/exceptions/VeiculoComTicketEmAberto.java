package com.estacionamento.api.domain.exceptions;

public class VeiculoComTicketEmAberto extends RuntimeException  {
    public VeiculoComTicketEmAberto(Long id) {
        super("Veículo com ticket em aberto encontrado com ID '" + id + "'.");
    }
}

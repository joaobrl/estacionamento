package com.estacionamento.api.domain.exceptions;

public class TicketJaFechadoException extends RuntimeException{
    public TicketJaFechadoException(Long id) {
        super("Ticket '" + id + "' já foi fechado");
    }
}

package com.estacionamento.api.domain.exceptions;

public class TicketJaPagoException extends RuntimeException{
    public TicketJaPagoException(Long id) {
        super("Ticket '" + id + "' já foi pago");
    }
}

package com.estacionamento.api.domain.ticket.validation;

import com.estacionamento.api.domain.ticket.Ticket;

public interface TicketValidationStrategy {
    void validate(Ticket ticket);

    boolean supports(String context);

    void validate(Ticket ticket, Long ticketId);
}

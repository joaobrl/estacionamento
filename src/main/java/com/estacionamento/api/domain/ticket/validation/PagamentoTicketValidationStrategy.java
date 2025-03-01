package com.estacionamento.api.domain.ticket.validation;

import com.estacionamento.api.domain.exceptions.TicketJaPagoException;
import com.estacionamento.api.domain.ticket.Ticket;
import org.springframework.stereotype.Component;

@Component
public class PagamentoTicketValidationStrategy implements TicketValidationStrategy {

    @Override
    public void validate(Ticket ticket) {
        if (ticket.getPago()) {
            throw new TicketJaPagoException(ticket.getId());
        }
    }

    @Override
    public boolean supports(String context) {
        return "pagamentoTicket".equals(context);
    }

    @Override
    public void validate(Ticket ticket, Long ticketId) {

    }
}
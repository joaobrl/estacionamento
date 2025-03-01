package com.estacionamento.api.domain.ticket.validation;

import com.estacionamento.api.domain.exceptions.TicketJaFechadoException;
import com.estacionamento.api.domain.ticket.Ticket;
import org.springframework.stereotype.Component;

@Component
public class FecharTicketValidationStrategy implements TicketValidationStrategy {

    @Override
    public void validate(Ticket ticket) {
        if (ticket.getHoraSaida() != null) {
            throw new TicketJaFechadoException(ticket.getId());
        }
    }

    @Override
    public boolean supports(String context) {
        return "fecharTicket".equals(context);
    }

    @Override
    public void validate(Ticket ticket, Long ticketId) {

    }
}
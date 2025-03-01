package com.estacionamento.api.domain.ticket.validation;

import com.estacionamento.api.domain.exceptions.VeiculoComTicketEmAberto;
import com.estacionamento.api.domain.ticket.Ticket;
import com.estacionamento.api.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CriarTicketValidationStrategy implements TicketValidationStrategy {

    private final TicketRepository ticketRepository;

    @Override
    public void validate(Ticket ticket) {
        List<Ticket> openTickets = ticketRepository.findByHoraSaidaIsNull();
        boolean exists = openTickets.stream()
                .anyMatch(t -> t.getVeiculo().getPlaca().equals(ticket.getVeiculo().getPlaca()));
        if (exists) {
            throw new VeiculoComTicketEmAberto(ticket.getId());
        }
    }

    @Override
    public boolean supports(String context) {
        return "criarTicket".equals(context);
    }

    @Override
    public void validate(Ticket ticket, Long ticketId) {

    }
}
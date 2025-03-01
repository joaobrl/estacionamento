package com.estacionamento.api.service;

import com.estacionamento.api.domain.ticket.Ticket;
import com.estacionamento.api.domain.ticket.validation.TicketValidationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketValidationService {

    private final List<TicketValidationStrategy> validationStrategies;

    public void validateCriarTicket(Ticket ticket) {
        for (TicketValidationStrategy strategy : validationStrategies) {
            if (strategy.supports("criarTicket")) {
                strategy.validate(ticket);
            }
        }
    }

    public void validateFecharTicket(Ticket ticket) {
        for (TicketValidationStrategy strategy : validationStrategies) {
            if (strategy.supports("fecharTicket")) {
                strategy.validate(ticket);
            }
        }
    }

    public void validatePagamentoTicket(Ticket ticket) {
        for (TicketValidationStrategy strategy : validationStrategies) {
            if (strategy.supports("pagamentoTicket")) {
                strategy.validate(ticket);
            }
        }
    }
}
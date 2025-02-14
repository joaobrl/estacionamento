package com.estacionamento.api.domain.ticket.dto;

import com.estacionamento.api.domain.ticket.Ticket;
import com.estacionamento.api.domain.veiculo.Veiculo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TicketListDto(
        Long id,
        Veiculo veiculo,
        Long estacionamentoId,
        String numeroVaga,
        LocalDateTime horaEntrada,
        LocalDateTime horaSaida,
        BigDecimal valor,
        Boolean pago
        ) {
    public TicketListDto(Ticket ticket) {
        this(
                ticket.getId(),
                ticket.getVeiculo(),
                ticket.getEstacionamento().getId(),
                ticket.getNumeroVaga(),
                ticket.getHoraEntrada(),
                ticket.getHoraSaida(),
                ticket.getValor(),
                ticket.getPago());
    }
}

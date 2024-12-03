package com.estacionamento.api.domain.ticket;

import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.veiculo.Veiculo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TicketListDto(
        Long id,
        Veiculo veiculo,
        Vaga vaga,
        LocalDateTime horaEntrada,
        LocalDateTime horaSaida,
        BigDecimal valor
) {
    public TicketListDto(Ticket ticket) {
        this(ticket.getId(), ticket.getVeiculo(), ticket.getVaga(), ticket.getHoraEntrada(), ticket.getHoraSaida(), ticket.getValor());
    }
}

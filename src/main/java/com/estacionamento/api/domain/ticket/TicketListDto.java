package com.estacionamento.api.domain.ticket;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TicketListDto(
        Long id,
        Long veiculoId,
        Long vagaId,
        LocalDateTime horaEntrada,
        LocalDateTime horaSaida,
        BigDecimal valor
) {
    public TicketListDto(Ticket ticket) {
        this(
                ticket.getId(),
                ticket.getVeiculo().getId(),
                ticket.getVaga().getId(),
                ticket.getHoraEntrada(),
                ticket.getHoraSaida(),
                ticket.getValor());
    }
}

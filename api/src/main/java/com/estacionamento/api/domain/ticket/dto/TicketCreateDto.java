package com.estacionamento.api.domain.ticket.dto;

import com.estacionamento.api.domain.veiculo.Veiculo;
import jakarta.validation.constraints.NotNull;

public record TicketCreateDto(
        @NotNull
        Long estacionamentoId,
        @NotNull
        Veiculo veiculo
) {
}

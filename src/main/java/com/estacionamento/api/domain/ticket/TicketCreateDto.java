package com.estacionamento.api.domain.ticket;

import jakarta.validation.constraints.NotNull;

public record TicketCreateDto(
        @NotNull
        Long veiculoId,
        @NotNull
        Long vagaId
) {
}

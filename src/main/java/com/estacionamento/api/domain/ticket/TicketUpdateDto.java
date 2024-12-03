package com.estacionamento.api.domain.ticket;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TicketUpdateDto(
        @NotNull
        Long id,
        @NotNull
        LocalDateTime horaSaida
) {
}

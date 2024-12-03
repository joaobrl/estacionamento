package com.estacionamento.api.domain.ticket;

import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.veiculo.Veiculo;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TicketCreateDto(
        @NotNull
        Long veiculoId,
        @NotNull
        Long vagaId,
        @NotNull
        LocalDateTime horaEntrada
) {
}

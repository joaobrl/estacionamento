package com.estacionamento.api.domain.historico.dto;

import com.estacionamento.api.domain.veiculo.Veiculo;
import jakarta.validation.constraints.NotNull;

public record EntradaClienteDto(
        @NotNull
        Long estacionamentoId,
        @NotNull
        Veiculo veiculo,
        String matricula

) {
}

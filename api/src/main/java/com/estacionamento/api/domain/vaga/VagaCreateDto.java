package com.estacionamento.api.domain.vaga;

import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VagaCreateDto(
        @NotBlank
        String numero,
        @NotNull
        VeiculoTipo veiculoTipo,

        @NotNull
        Boolean disponibilidade
) {
}

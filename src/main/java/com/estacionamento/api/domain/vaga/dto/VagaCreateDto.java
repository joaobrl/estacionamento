package com.estacionamento.api.domain.vaga.dto;

import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VagaCreateDto(
        @NotBlank
        String numeroVaga,
        @NotNull
        VeiculoTipo veiculoTipo
) {
}

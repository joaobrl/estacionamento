package com.estacionamento.api.domain.veiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VeiculoUpdateDto(
        @NotNull
        Long id,
        @NotBlank
        String placa
) {
}

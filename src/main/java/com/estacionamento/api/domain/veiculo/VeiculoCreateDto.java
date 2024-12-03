package com.estacionamento.api.domain.veiculo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VeiculoCreateDto(
        @NotNull
        String placa,
        @NotBlank
        String modelo,
        @NotBlank
        String marca,
        @NotNull
        VeiculoTipo veiculoTipo
) {
}

package com.estacionamento.api.domain.veiculo.dto;

import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record VeiculoCreateDto(
        @NotBlank
        String placa,

        @NotNull
        VeiculoTipo veiculoTipo,
        String modelo,

        String marca
) {
}

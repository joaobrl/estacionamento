package com.estacionamento.api.domain.vaga.dto;

import com.estacionamento.api.domain.vaga.TipoVaga;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import jakarta.validation.constraints.NotBlank;

public record VagaUpdateDto(
        @NotBlank
        String numeroVaga,
        VeiculoTipo veiculoTipo,
        TipoVaga tipoVaga,
        Boolean disponibilidade
) {
}

package com.estacionamento.api.domain.vaga;

import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import jakarta.validation.constraints.NotNull;

public record VagaUpdateDto(
        @NotNull Long id,
        Boolean disponibilidade
) {
    public VagaUpdateDto(Vaga vaga) {
        this(
                vaga.getId(),
                vaga.getDisponibilidade());
    }
}

package com.estacionamento.api.domain.vaga;

import com.estacionamento.api.domain.veiculo.VeiculoTipo;

public record VagaDto(
        Long id,
        String numero,
        VeiculoTipo veiculoTipo,
        Boolean disponibilidade
) {
    public VagaDto(Vaga vaga) {
        this(
                vaga.getId(),
                vaga.getNumero(),
                vaga.getVeiculoTipo(),
                vaga.getDisponibilidade());
    }
}

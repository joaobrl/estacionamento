package com.estacionamento.api.domain.vaga;

import com.estacionamento.api.domain.veiculo.VeiculoTipo;

public record VagaListDto(
        Long id,
        String numero,
        VeiculoTipo veiculoTipo,
        Boolean disponibilidade
) {
    public VagaListDto(Vaga vaga) {
        this(
                vaga.getId(),
                vaga.getNumero(),
                vaga.getVeiculoTipo(),
                vaga.isDisponibilidade());
    }
}

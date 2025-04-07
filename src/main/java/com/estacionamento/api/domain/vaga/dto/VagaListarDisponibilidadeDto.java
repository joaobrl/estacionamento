package com.estacionamento.api.domain.vaga.dto;

import com.estacionamento.api.domain.vaga.TipoVaga;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;

public record VagaListarDisponibilidadeDto(
        Boolean disponibilidade,
        TipoVaga tipoVaga,
        VeiculoTipo veiculoTipo
) {
}

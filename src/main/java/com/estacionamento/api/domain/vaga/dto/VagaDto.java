package com.estacionamento.api.domain.vaga.dto;

import com.estacionamento.api.domain.vaga.TipoVaga;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VagaDto(
        @NotBlank
        String numeroVaga,
        @NotNull
        VeiculoTipo veiculoTipo,
        @NotNull
        TipoVaga tipoVaga,
        @NotNull
        Boolean disponibilidade) {

        public VagaDto(Vaga vaga) {
            this(
                    vaga.getNumeroVaga(),
                    vaga.getVeiculoTipo(),
                    vaga.getTipoVaga(),
                    vaga.getDisponibilidade()
            );
        }
}

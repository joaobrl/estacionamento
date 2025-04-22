package com.estacionamento.api.domain.historico.dto;

import com.estacionamento.api.domain.historico.MovimentacaoClientePlano;
import com.estacionamento.api.domain.veiculo.Veiculo;

import java.time.LocalDateTime;

public record ClienteMensalistaDto(

    Veiculo veiculo,
    Long estacionamentoId,
    String numeroVaga,
    LocalDateTime horaEntrada,
    LocalDateTime horaSaida
        ) {
    public ClienteMensalistaDto(MovimentacaoClientePlano movimentacaoClientePlano) {
            this(   movimentacaoClientePlano.getVeiculo(),
                    movimentacaoClientePlano.getEstacionamentoId(),
                    movimentacaoClientePlano.getNumeroVaga(),
                    movimentacaoClientePlano.getHoraEntrada(),
                    movimentacaoClientePlano.getHoraSaida());
        }
    }

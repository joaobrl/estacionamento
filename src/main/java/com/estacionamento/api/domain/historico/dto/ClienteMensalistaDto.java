package com.estacionamento.api.domain.historico.dto;

import com.estacionamento.api.domain.historico.MovimentacaoClientePlano;
import com.estacionamento.api.domain.ticket.Ticket;
import com.estacionamento.api.domain.veiculo.Veiculo;

import java.time.LocalDateTime;

public record ClienteMensalistaDto(
    String matricula,
    Veiculo veiculo,
    Long estacionamentoId,
    String numeroVaga,
    LocalDateTime horaEntrada,
    LocalDateTime horaSaida
        ) {
    public ClienteMensalistaDto(MovimentacaoClientePlano movimentacaoClientePlano) {
            this(   movimentacaoClientePlano.getCliente().getMatricula(),
                    movimentacaoClientePlano.getVeiculo(),
                    movimentacaoClientePlano.getEstacionamento().getId(),
                    movimentacaoClientePlano.getNumeroVaga(),
                    movimentacaoClientePlano.getHoraEntrada(),
                    movimentacaoClientePlano.getHoraSaida());
        }
    }

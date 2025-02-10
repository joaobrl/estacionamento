package com.estacionamento.api.domain.estacionamento.dto;

import com.estacionamento.api.domain.estacionamento.Estacionamento;

public record EstacionamentoListDto(
        Long id,
        String nome,
        Integer capacidade

) {
    public EstacionamentoListDto(Estacionamento estacionamento) {
        this(
                estacionamento.getId(),
                estacionamento.getNome(),
                estacionamento.getCapacidade()
        );
    }

}

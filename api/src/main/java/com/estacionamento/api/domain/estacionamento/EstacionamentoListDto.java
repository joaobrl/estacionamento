package com.estacionamento.api.domain.estacionamento;

import org.springframework.http.ResponseEntity;

import java.util.List;

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

    public EstacionamentoListDto(ResponseEntity<List<Estacionamento>> estacionamento) {
        this(
                estacionamento.getBody().get(0).getId(),
                estacionamento.getBody().get(0).getNome(),
                estacionamento.getBody().get(0).getCapacidade()
        );
    }
}

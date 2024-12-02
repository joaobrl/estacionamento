package com.estacionamento.api.domain.estacionamento;

import com.estacionamento.api.domain.endereco.Endereco;
import com.estacionamento.api.domain.vaga.Vaga;

import java.util.List;

public record EstacionamentoListDto(
        Long id,
        String nome,
        Endereco endereco,
        Integer capacidade,
        List<Vaga> vagas
) {
    public EstacionamentoListDto(Estacionamento estacionamento) {
        this(estacionamento.getId(),
                estacionamento.getNome(),
                estacionamento.getEndereco(),
                estacionamento.getCapacidade(),
                estacionamento.getVagas());
    }
}

package com.estacionamento.api.domain.estacionamento.dto;

import com.estacionamento.api.domain.endereco.Endereco;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.vaga.dto.VagaDto;

import java.util.List;
import java.util.stream.Collectors;

public record EstacionamentoDetalharDto(
        Long id,
        String nome,
        Endereco endereco,
        Integer capacidade,
        List<VagaDto> vagas
) {
    public EstacionamentoDetalharDto(Estacionamento estacionamento) {
        this(
                estacionamento.getId(),
                estacionamento.getNome(),
                estacionamento.getEndereco(),
                estacionamento.getCapacidade(),
                estacionamento.getVagas().stream()
                        .map(vaga -> new VagaDto(vaga.getNumeroVaga(), vaga.getVeiculoTipo(), vaga.getDisponibilidade()))
                        .collect(Collectors.toList())
        );
    }
}

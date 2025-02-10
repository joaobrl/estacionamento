package com.estacionamento.api.domain.estacionamento.dto;

import com.estacionamento.api.domain.endereco.EnderecoDto;
import jakarta.validation.constraints.NotNull;

public record EstacionamentoUpdateDto(
        @NotNull
        Long id,
        String nome,
        EnderecoDto endereco,
        Integer capacidade
) {
}

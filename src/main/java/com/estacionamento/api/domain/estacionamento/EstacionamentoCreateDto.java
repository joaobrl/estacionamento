package com.estacionamento.api.domain.estacionamento;

import com.estacionamento.api.domain.endereco.EnderecoDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EstacionamentoCreateDto(
        @NotBlank
        String nome,
        @Valid
        EnderecoDto endereco,
        @NotNull
        Integer capacidade
) {
}

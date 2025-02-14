package com.estacionamento.api.domain.cliente.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteCreateDto(
        @NotBlank
        String nome,

        @NotBlank
        String cpf,

        String email,

        String telefone
) {
}

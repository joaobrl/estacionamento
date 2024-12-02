package com.estacionamento.api.domain.endereco;

import jakarta.validation.constraints.NotBlank;

public record EnderecoDto(
        @NotBlank
        String rua,
        String numero,
        String bairro,
        @NotBlank
        String cidade,
        @NotBlank
        String estado,
        @NotBlank
        String cep
) {
}

package com.estacionamento.api.domain.cliente.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ClienteCreateDto(
        @NotBlank
        String nome,

        @Pattern(regexp = "\\d{11}", message = "O CPF deve conter exatamente 11 dígitos numéricos")
        @NotBlank(message = "O CPF é obrigatório")
        String cpf,

        String email,

        String telefone
) {
}

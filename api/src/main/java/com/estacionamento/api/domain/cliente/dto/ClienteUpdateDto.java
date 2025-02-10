package com.estacionamento.api.domain.cliente.dto;

import com.estacionamento.api.domain.cliente.TipoPlano;

public record ClienteUpdateDto(
        String nome,
        String cpf,
        String email,
        String telefone,
        Boolean planoMensalAtivo,
        TipoPlano tipoPlano
) {
}

package com.estacionamento.api.domain.cliente.dto;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.cliente.TipoPlano;
import com.estacionamento.api.domain.veiculo.dto.VeiculoListDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ClienteListDto(
        Long id,
        String matricula,
        String nome,
        String cpf,
        String email,
        String telefone,
        Boolean planoMensalAtivo,
        TipoPlano tipoPlano,
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime validadePlanoMensal
) {
    public ClienteListDto(Cliente cliente) {
        this(
                cliente.getId(),
                cliente.getMatricula(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getPlanoMensalAtivo(),
                cliente.getTipoPlano(),
                cliente.getValidadePlanoMensal());
    }
}

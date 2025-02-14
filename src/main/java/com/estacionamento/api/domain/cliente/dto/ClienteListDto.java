package com.estacionamento.api.domain.cliente.dto;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.cliente.TipoPlano;
import com.estacionamento.api.domain.veiculo.dto.VeiculoListDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ClienteListDto(
        Long id,
        String nome,
        String cpf,
        String email,
        String telefone,
        List<VeiculoListDto> veiculos,
        Boolean planoMensalAtivo,
        TipoPlano tipoPlano,
        Boolean renavacaoAutomaticaPlano,
        LocalDateTime validadePlanoMensal
) {
    public ClienteListDto(Cliente cliente) {
        this(
                cliente.getId(),
                cliente.getNome(),
                cliente.getCpf(),
                cliente.getEmail(),
                cliente.getTelefone(),
                cliente.getVeiculos().stream()
                        .map(veiculo -> new VeiculoListDto (
                                veiculo.getPlaca(),
                                veiculo.getVeiculoTipo(),
                                veiculo.getMarca(),
                                veiculo.getModelo()))
                        .collect(Collectors.toList()),
                cliente.getPlanoMensalAtivo(),
                cliente.getTipoPlano(),
                cliente.getRenovacaoAutomaticaPlano(),
                cliente.getValidadePlanoMensal());
    }
}

package com.estacionamento.api.service;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.cliente.TipoPlano;
import com.estacionamento.api.domain.exceptions.PlanoMensalObrigatorioException;
import com.estacionamento.api.domain.historico.dto.EntradaClienteDto;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteValidationService {

    private final ClienteService clienteService;

    public void validarPlanoMensalParaCaminhao(EntradaClienteDto entradaClienteDto, Cliente cliente) {
        if (entradaClienteDto.veiculo().getVeiculoTipo() == VeiculoTipo.CAMINHAO) {
            if (cliente == null || cliente.getTipoPlano() == null || !cliente.getTipoPlano().equals(TipoPlano.ESPECIAL)) {
                throw new PlanoMensalObrigatorioException();
            }
        }
    }

    public boolean isPlanoAtivo(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Cliente não pode ser nulo.");
        }
        return cliente.getPlanoMensalAtivo()
                && cliente.getValidadePlanoMensal() != null
                && cliente.getValidadePlanoMensal().isAfter(java.time.LocalDateTime.now());
    }

    public boolean isClienteComVeiculoNoEstacionamento(String matricula) {
        return clienteService.isClienteComVeiculoNoEstacionamento(matricula);
    }

}
package com.estacionamento.api.domain.cliente.validadores;

import com.estacionamento.api.domain.cliente.Cliente;

import java.time.LocalDateTime;

public class PlanoValidacaoStrategy implements Validacao {

    @Override
    public void validar(Cliente cliente) {
        // Verificação 1: Cliente com plano mensal ativo deve ter data de validade
        if (cliente.getPlanoMensalAtivo() && cliente.getValidadePlanoMensal() == null) {
            throw new IllegalArgumentException("Cliente com plano mensal ativo deve ter data de validade do plano");
        }

        // Verificação 2: Cliente sem plano mensal ativo e com data de validade do plano
        if (!cliente.getPlanoMensalAtivo() && cliente.getValidadePlanoMensal() != null) {
            throw new IllegalArgumentException("Cliente sem plano mensal ativo não deve ter data de validade do plano");
        }

        // Verificação 3: Cliente sem plano mensal ativo e com renovação automática configurada
        if (!cliente.getPlanoMensalAtivo() && cliente.getRenovacaoAutomaticaPlano()) {
            throw new IllegalArgumentException("Cliente sem plano mensal ativo não deve ter renovação automática do plano");
        }

        // Verificação 4: Verificar se a data de validade do plano já venceu
        if (cliente.getPlanoMensalAtivo() && cliente.getValidadePlanoMensal() != null) {
            if (cliente.getValidadePlanoMensal().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("O plano mensal está vencido");
            }
        }
    }

}

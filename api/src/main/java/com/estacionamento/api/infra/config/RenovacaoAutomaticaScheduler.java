package com.estacionamento.api.infra.config;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.cliente.ClienteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
public class RenovacaoAutomaticaScheduler {

    @Autowired
    private ClienteRepository clienteRepository;

    @Scheduled(cron = "0 0 0 * * *", zone = "America/Sao_Paulo")
    public void renovarPlanosVencidos() {
        log.info("Iniciando a verificação de planos vencidos para renovação automática.");

        // Buscar clientes com plano ativo e validade expirada
        List<Cliente> clientes = clienteRepository.findClientesComRenovacaoAutomaticaEPlanoExpirado(LocalDateTime.now());

        for (Cliente cliente : clientes) {
            try {
                // Renovar o plano
                renovarPlano(cliente);
            } catch (Exception e) {
                // Logar qualquer erro ocorrido durante a renovação
                log.error("Erro ao renovar plano do cliente com ID {}: {}", cliente.getId(), e.getMessage(), e);
            }
        }
        log.info("Processo de renovação automática concluído.");
    }

    private void renovarPlano(Cliente cliente) {
        // Renovar a validade do plano
        cliente.setValidadePlanoMensal(cliente.getValidadePlanoMensal().plusMonths(1));

        // Persistir as mudanças no banco de dados
        clienteRepository.save(cliente);

        log.info("Plano do cliente com ID {} renovado até {}", cliente.getId(), cliente.getValidadePlanoMensal());
    }
}


package com.estacionamento.api.service;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.exceptions.PlanoNaoAtivoException;
import com.estacionamento.api.domain.exceptions.VeiculoEstacionadoException;
import com.estacionamento.api.domain.historico.MovimentacaoClientePlano;
import com.estacionamento.api.domain.historico.dto.EntradaClienteDto;
import com.estacionamento.api.domain.historico.dto.SaidaClienteDto;
import com.estacionamento.api.domain.ticket.Ticket;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class MovimentacaoService {

    private final EstacionamentoService estacionamentoService;
    private final ClienteService clienteService;
    private final TicketValidationService ticketValidationService;
    private final ClienteValidationService clienteValidationService;
    private final TicketService ticketService;
    private final ClienteRepository clienteRepository;
    private final VagaService vagaService;

    @Transactional
    public Object processarEntradaOuCriarTicket(EntradaClienteDto entradaClienteDto) {
        if (entradaClienteDto.matricula() != null && !entradaClienteDto.matricula().isBlank()) {
            var mensalista = registrarEntrada(entradaClienteDto);

            if (mensalista == null) {
                return ticketService.criarTicket(entradaClienteDto);
            }

            return mensalista;
        } else {
            return ticketService.criarTicket(entradaClienteDto);
        }
    }

    @Transactional
    public MovimentacaoClientePlano registrarEntrada(EntradaClienteDto entradaClienteDto) {
        log.info("Registrando entrada para cliente com matrícula: {}", entradaClienteDto.matricula());

        // Buscar o cliente pela matrícula
        var cliente = clienteService.buscarPorMatricula(entradaClienteDto.matricula());

        // Validar se o plano ativo do cliente é compatível com o veículo
        clienteValidationService.validarPlanoMensalParaCaminhao(entradaClienteDto, cliente);

        // Verificar se o veículo já possui um ticket em aberto
        Ticket novoticket = new Ticket();
        novoticket.setVeiculo(entradaClienteDto.veiculo());
        ticketValidationService.validateCriarTicket(novoticket);

        if (!clienteValidationService.isPlanoAtivo(cliente)) {
            throw new PlanoNaoAtivoException("O plano do cliente não está ativo.");
        }

        if (clienteValidationService.isClienteComVeiculoNoEstacionamento(entradaClienteDto.matricula())) {
            throw new VeiculoEstacionadoException("O cliente já possui veículo no estacionamento.");
        }

        // Caso todas as validações sejam atendidas, registrar a entrada
        return processarEntradaComMatricula(entradaClienteDto);
    }

    @Transactional
    public MovimentacaoClientePlano processarEntradaComMatricula(EntradaClienteDto entradaClienteDto) {
        log.info("Processando entrada com matrícula: {}", entradaClienteDto.matricula());

        var cliente = clienteService.buscarPorMatricula(entradaClienteDto.matricula());

        var estacionamento = estacionamentoService.findEstacionamentoById(entradaClienteDto.estacionamentoId());
        Vaga vagaDisponivel = estacionamentoService.verificarDisponibilidadeVaga(estacionamento, entradaClienteDto.veiculo());

        MovimentacaoClientePlano movimentacao = criarMovimentacao(cliente, estacionamento, vagaDisponivel.getNumeroVaga(), entradaClienteDto.veiculo());
        cliente.getMovimentacaoClientePlano().add(movimentacao);

        vagaService.ocuparVaga(entradaClienteDto, vagaDisponivel.getNumeroVaga());
        estacionamentoService.saveEstacionamento(estacionamento);
        clienteService.salvarCliente(cliente);
        log.info("Movimentação registrada com sucesso para o cliente {}", entradaClienteDto.matricula());
        return movimentacao;
    }

    private MovimentacaoClientePlano criarMovimentacao(Cliente cliente, Estacionamento estacionamento, String numeroVaga, Veiculo veiculo) {
        MovimentacaoClientePlano movimentacao = new MovimentacaoClientePlano();

        movimentacao.setEstacionamentoId(estacionamento.getId());
        movimentacao.setVeiculo(veiculo);
        movimentacao.setNumeroVaga(numeroVaga);
        movimentacao.setHoraEntrada(LocalDateTime.now());
        return movimentacao;
    }

    @Transactional
    public MovimentacaoClientePlano registrarSaida(SaidaClienteDto saidaClienteDto) {
        log.info("Registrando saída para cliente com matrícula: {}", saidaClienteDto.matricula());

        // Buscar o cliente
        var cliente = clienteService.buscarPorMatricula(saidaClienteDto.matricula());

        // Filtrar a movimentação ativa
        var movimentacaoAtiva = cliente.getMovimentacaoClientePlano().stream()
                .filter(m -> m.getHoraSaida() == null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nenhuma movimentação ativa encontrada para o cliente."));

        // Atualizar o horário de saída
        movimentacaoAtiva.setHoraSaida(LocalDateTime.now());
        log.info("Hora de saída registrada: {}", movimentacaoAtiva.getHoraSaida());

        // Liberar a vaga e salvar o cliente
        var estacionamento = estacionamentoService.findEstacionamentoById(movimentacaoAtiva.getEstacionamentoId());
        vagaService.liberarVaga(estacionamento, movimentacaoAtiva.getNumeroVaga());
        clienteService.salvarCliente(cliente);
        log.info("Movimentação atualizada com sucesso para o cliente {}", saidaClienteDto.matricula());

        return movimentacaoAtiva;
    }


}

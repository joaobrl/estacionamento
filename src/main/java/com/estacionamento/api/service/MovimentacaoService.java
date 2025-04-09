package com.estacionamento.api.service;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.historico.MovimentacaoClientePlano;
import com.estacionamento.api.domain.historico.dto.EntradaClienteDto;
import com.estacionamento.api.domain.historico.dto.SaidaClienteDto;
import com.estacionamento.api.domain.ticket.Ticket;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class MovimentacaoService {


    private final EstacionamentoService estacionamentoService;
    private final ClienteService clienteService;
    private final TicketValidationService ticketValidationService;
    private final ClienteValidationService clienteValidationService;
    private final TicketService ticketService;
    private final ClienteRepository clienteRepository;


    @Transactional
    public MovimentacaoClientePlano registrarEntrada(EntradaClienteDto entradaClienteDto) {
        var cliente = clienteService.buscarPorMatricula(entradaClienteDto.matricula());

        // Verificar se o plano ativo do cliente é compatível com o veículo
        clienteValidationService.validarPlanoMensalParaCaminhao(entradaClienteDto, cliente);

        // Verificar se o veículo já possui um ticket em aberto
        Ticket novoticket = new Ticket();
        novoticket.setVeiculo(entradaClienteDto.veiculo());
        if (ticketValidationService.validateCriarTicket(novoticket)) {
            throw new IllegalArgumentException("O veículo já possui um ticket em aberto.");
        }

        // Abrir um ticket caso o cliente não possua plano ativo ou já tenha um veículo no estacionamento
        if (!clienteValidationService.isPlanoAtivo(cliente) || clienteValidationService.isClienteComVeiculoNoEstacionamento(entradaClienteDto.matricula())) {
            ticketService.criarTicket(entradaClienteDto);
        }

        // Caso todas as validações sejam atendidas, registrar a entrada
        return processarEntradaComMatricula(entradaClienteDto);
    }

    @Transactional
    private MovimentacaoClientePlano processarEntradaComMatricula(EntradaClienteDto entradaClienteDto) {
        // Buscar o cliente pela matrícula
        var cliente = clienteService.buscarPorMatricula(entradaClienteDto.matricula());


        MovimentacaoClientePlano novaMovimentacao = new MovimentacaoClientePlano();
        novaMovimentacao.setVeiculo(entradaClienteDto.veiculo());

        var estacionamento = estacionamentoService.findEstacionamentoById(entradaClienteDto.estacionamentoId());
        Vaga vagaDisponivel = estacionamentoService.verificarDisponibilidadeVaga(estacionamento, entradaClienteDto.veiculo());

        // Criar uma nova movimentação
        MovimentacaoClientePlano movimentacao = new MovimentacaoClientePlano();
        movimentacao.setCliente(cliente);
        movimentacao.setEstacionamento(estacionamento);
        movimentacao.setVeiculo(entradaClienteDto.veiculo());
        movimentacao.setNumeroVaga(vagaDisponivel.getNumeroVaga());
        movimentacao.setHoraEntrada(LocalDateTime.now());

        // Adicionar a movimentação à lista do cliente
        cliente.getMovimentacaoClientePlano().add(movimentacao);

        // Ocupar a vaga e salvar o estado do estacionamento
        vagaDisponivel.ocuparVaga();
        estacionamentoService.saveEstacionamento(estacionamento);

        clienteRepository.save(cliente);
        return movimentacao;

    }

    @Transactional
    public MovimentacaoClientePlano registrarSaida(SaidaClienteDto saidaClienteDto) {
        var movimentacaoAtiva = clienteRepository.findMovimentacaoAtivaByMatricula(saidaClienteDto.matricula())
                .orElseThrow(() -> new IllegalArgumentException("Nenhuma movimentação ativa encontrada para o cliente."));

        movimentacaoAtiva.setHoraSaida(LocalDateTime.now());
        clienteRepository.save(movimentacaoAtiva.getCliente());
        return movimentacaoAtiva;
    }
}
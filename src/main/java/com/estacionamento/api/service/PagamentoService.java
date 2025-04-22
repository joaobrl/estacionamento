package com.estacionamento.api.service;

import com.estacionamento.api.domain.pagamento.Pagamento;
import com.estacionamento.api.domain.pagamento.dto.PagamentoPlanoMensalDto;
import com.estacionamento.api.domain.pagamento.dto.PagamentoTicketDto;
import com.estacionamento.api.repository.PagamentoRepository;
import com.estacionamento.api.util.Util;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.estacionamento.api.domain.pagamento.Pagamento.criarPagamentoPlanoMensal;
import static com.estacionamento.api.domain.pagamento.Pagamento.criarPagamentoTicket;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final TicketService ticketService;
    private final EstacionamentoService estacionamentoService;
    private final ClienteService clienteService;
    private final VagaService vagaService;
    private final TicketValidationService ticketValidationService;

    @Transactional
    public Pagamento pagamentoTicket(PagamentoTicketDto pagamentoTicketDto) {
        var ticket = ticketService.findTicketById(pagamentoTicketDto.ticketId());

        ticketValidationService.validatePagamentoTicket(ticket);

        var estacionamento = estacionamentoService.findEstacionamentoById(ticket.getEstacionamento().getId());
        ticket.setHoraSaida(LocalDateTime.now());
        BigDecimal valorTicket = ticket.calcularValor();

        Pagamento novoPagamento = criarPagamentoTicket(pagamentoTicketDto, ticket, estacionamento, valorTicket);
        ticket.setPago(true);
        ticket.setHoraSaida(LocalDateTime.now());

        ticket.atualizar();
        ticket.calcularValor();
        ticket.setValor(ticket.getValor());
        var numeroVaga = ticket.getNumeroVaga();
        vagaService.liberarVaga(estacionamento, numeroVaga);
        ticketService.saveTicket(ticket);

        return pagamentoRepository.save(novoPagamento);
    }

    @Transactional
    public Pagamento pagamentoPlanoMensal(PagamentoPlanoMensalDto pagamentoPlanoMensalDto) {
        var cliente = clienteService.findClienteById(pagamentoPlanoMensalDto.clienteId());

        if (cliente.getMatricula() == null) {
            cliente.setMatricula(Util.gerarMatricula(cliente));
            clienteService.salvarCliente(cliente);
        }

        double valorMensalidade = Util.calcularValorMensalidade(
                cliente.getTipoPlano(),
                pagamentoPlanoMensalDto.tipoPlano(),
                cliente.getValidadePlanoMensal(),
                pagamentoPlanoMensalDto.tipoContrato()
        );

        Pagamento pagamento = criarPagamentoPlanoMensal(pagamentoPlanoMensalDto, cliente, valorMensalidade);

        clienteService.atualizarPlanoMensal(pagamentoPlanoMensalDto);

        return pagamentoRepository.save(pagamento);
    }

    public List<Pagamento> listarPagamentos() {
        return pagamentoRepository.findAll();
    }
}
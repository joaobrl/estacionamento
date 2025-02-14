package com.estacionamento.api.domain.pagamento;

import com.estacionamento.api.domain.cliente.ClienteRepository;
import com.estacionamento.api.domain.cliente.TipoPlano;
import com.estacionamento.api.domain.estacionamento.EstacionamentoRepository;
import com.estacionamento.api.domain.exceptions.RecursoNaoEncontradoException;
import com.estacionamento.api.domain.exceptions.TicketJaPagoException;
import com.estacionamento.api.domain.pagamento.dto.PagamentoPlanoMensalDto;
import com.estacionamento.api.domain.pagamento.dto.PagamentoTicketDto;
import com.estacionamento.api.domain.ticket.TicketRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PagamentoService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private PagamentoRepository pagamentoRepository;
    @Autowired
    private EstacionamentoRepository estacionamentoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public Pagamento pagamentoTicket(PagamentoTicketDto pagamentoTicketDto) {
        // Verificar se o Ticket já foi pago
        var ticket = ticketRepository.findById(pagamentoTicketDto.ticketId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ticket", pagamentoTicketDto.ticketId()));

        if (ticket.getPago()) {
            throw new TicketJaPagoException(pagamentoTicketDto.ticketId());
        }

        var estacionamento = estacionamentoRepository.findById(ticket.getEstacionamento().getId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estacionamento", ticket.getEstacionamento().getId()));

        // Calcular o valor do ticket
        var valorTicket = ticket.getValor();
        Pagamento novoPagamento = new Pagamento(pagamentoTicketDto, ticket, estacionamento);
        ticket.setPago(true);

        // Libera a vaga
        estacionamento.getVagas()
                .stream()
                .filter(v -> v.getNumeroVaga().equals(ticket.getNumeroVaga()))
                .findFirst()
                .ifPresent(v -> v.liberarVaga(ticket.getNumeroVaga()));
        estacionamentoRepository.save(estacionamento);

        return pagamentoRepository.save(novoPagamento);
    }

    @Transactional
    public Pagamento pagamentoPlanoMensal(PagamentoPlanoMensalDto pagamentoPlanoMensalDto) {
        var cliente = clienteRepository.findById(pagamentoPlanoMensalDto.clienteId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", pagamentoPlanoMensalDto.clienteId()));

        // Obter o tipo de plano informado no DTO
        var tipoPlano = pagamentoPlanoMensalDto.tipoPlano();
        double valorMensalidade = 0;

        // Calcular o valor da mensalidade com base no plano escolhido
        if(cliente.getTipoPlano() == null || cliente.getTipoPlano() == tipoPlano){
            valorMensalidade = tipoPlano.getValorPlano();
        } else if(tipoPlano != cliente.getTipoPlano()){
            long diasRestantes = ChronoUnit.DAYS.between(LocalDateTime.now(), cliente.getValidadePlanoMensal());
            valorMensalidade = TipoPlano.calcularMigracaoDePlano(cliente.getTipoPlano(), tipoPlano, diasRestantes);
        }

        var pagamento = new Pagamento(pagamentoPlanoMensalDto, cliente, BigDecimal.valueOf(valorMensalidade));

        // Atualizar a validade do plano mensal
        if (cliente.getPlanoMensalAtivo() && cliente.getPlanoMensalAtivo() != null) {
            cliente.setValidadePlanoMensal(cliente.getValidadePlanoMensal().plusMonths(1));
        } else {
            cliente.setValidadePlanoMensal(LocalDateTime.now().plusMonths(1));
        }

        cliente.setPlanoMensalAtivo(true);
        cliente.setTipoPlano(pagamentoPlanoMensalDto.tipoPlano());

        // Renovação automatica para pagamento com cartão
        if (pagamentoPlanoMensalDto.metodoPagamento() == MetodoPagamento.CARTAO) {
            cliente.setRenovacaoAutomaticaPlano(true);
        }

        // Salvar as alterações
        clienteRepository.save(cliente);

        return pagamentoRepository.save(pagamento);
    }

    // Método para listar todos os pagamentos
    public List<Pagamento> listarPagamentos() {
        return pagamentoRepository.findAll();
    }
}




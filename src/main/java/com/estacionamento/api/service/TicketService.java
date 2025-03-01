package com.estacionamento.api.service;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.exceptions.RecursoNaoEncontradoException;
import com.estacionamento.api.domain.ticket.Ticket;
import com.estacionamento.api.domain.ticket.dto.TicketCreateDto;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EstacionamentoService estacionamentoService;
    private final ClienteService clienteService;
    private final TicketValidationService ticketValidationService;
    private final ClienteValidationService clienteValidationService;

    @Transactional
    public Ticket criarTicket(TicketCreateDto ticketCreateDto) {
        Cliente cliente = clienteService.findClienteByVeiculoPlaca(ticketCreateDto.veiculo().getPlaca()).orElse(null);

        Ticket novoTicket = new Ticket();
        novoTicket.setVeiculo(ticketCreateDto.veiculo());

        ticketValidationService.validateCriarTicket(novoTicket);

        clienteValidationService.validarPlanoMensalParaCaminhao(ticketCreateDto, cliente);

        Estacionamento estacionamento = estacionamentoService.findEstacionamentoById(ticketCreateDto.estacionamentoId());
        Vaga vagaDisponivel = estacionamentoService.verificarDisponibilidadeVaga(estacionamento, ticketCreateDto.veiculo());
        novoTicket = new Ticket(estacionamento, vagaDisponivel.getNumeroVaga(), ticketCreateDto.veiculo());

        vagaDisponivel.ocuparVaga();
        estacionamentoService.saveEstacionamento(estacionamento);

        return ticketRepository.save(novoTicket);
    }

    public Ticket buscarTicketPorId(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ticket", id));
    }

    public Ticket findTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ticket", id));
    }

    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }
}
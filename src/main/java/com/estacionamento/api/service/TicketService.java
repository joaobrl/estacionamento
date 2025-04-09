package com.estacionamento.api.service;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.exceptions.RecursoNaoEncontradoException;
import com.estacionamento.api.domain.ticket.Ticket;
import com.estacionamento.api.domain.historico.dto.EntradaClienteDto;
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
    public Ticket criarTicket(EntradaClienteDto entradaClienteDto) {
        Cliente cliente = clienteService.findClienteByVeiculoPlaca(entradaClienteDto.veiculo().getPlaca()).orElse(null);

        Ticket novoticket = new Ticket();
        novoticket.setVeiculo(entradaClienteDto.veiculo());

        ticketValidationService.validateCriarTicket(novoticket);

        clienteValidationService.validarPlanoMensalParaCaminhao(entradaClienteDto, cliente);

        var estacionamento = estacionamentoService.findEstacionamentoById(entradaClienteDto.estacionamentoId());
        Vaga vagaDisponivel = estacionamentoService.verificarDisponibilidadeVaga(estacionamento, entradaClienteDto.veiculo());
        novoticket = new Ticket(estacionamento, vagaDisponivel.getNumeroVaga(), entradaClienteDto.veiculo());

        vagaDisponivel.ocuparVaga();
        estacionamentoService.saveEstacionamento(estacionamento);

        return ticketRepository.save(novoticket);
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
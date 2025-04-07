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
public class MovimentacaoService {
    private final TicketRepository ticketRepository;
    private final EstacionamentoService estacionamentoService;
    private final ClienteService clienteService;
    private final TicketValidationService ticketValidationService;
    private final ClienteValidationService clienteValidationService;

    @Transactional
    public Ticket registrarEntrada(TicketCreateDto ticketCreateDto, String matricula) {
        Cliente cliente = clienteService.findClienteByVeiculoPlaca(ticketCreateDto.veiculo().getPlaca()).orElse(null);

        if (matricula != null ) {

            if (!cliente.getPlanoMensalAtivo()) {
                throw new IllegalArgumentException("Matrícula inválida para o cliente com assinatura de plano.");
            }

            // se o veiculo for caminhão, verificar se o cliente tem plano mensal compativel

            //validar hora de entrada e tipo de plano

            // validar se o veículo já está com o ticket aberto


        } else {
            // Cliente esporádico, gera ticket
            Ticket novoticket = new Ticket();
            novoticket.setVeiculo(ticketCreateDto.veiculo());

            ticketValidationService.validateCriarTicket(novoticket);

            clienteValidationService.validarPlanoMensalParaCaminhao(ticketCreateDto, cliente);

            Estacionamento estacionamento = estacionamentoService.findEstacionamentoById(ticketCreateDto.estacionamentoId());
            Vaga vagaDisponivel = estacionamentoService.verificarDisponibilidadeVaga(estacionamento, ticketCreateDto.veiculo());
            novoticket = new Ticket(estacionamento, vagaDisponivel.getNumeroVaga(), ticketCreateDto.veiculo());

            vagaDisponivel.ocuparVaga();
            estacionamentoService.saveEstacionamento(estacionamento);

            return ticketRepository.save(novoticket);
        }
    }

    @Transactional
    public void registrarSaida(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ticket", ticketId));
        ticket.setHoraSaida(LocalDateTime.now());
        ticket.setPago(true);
        ticketRepository.save(ticket);
    }
}
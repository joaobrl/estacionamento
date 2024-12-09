package com.estacionamento.api.domain.ticket;

import com.estacionamento.api.domain.pagamento.PagamentoService;
import com.estacionamento.api.domain.vaga.VagaRepository;
import com.estacionamento.api.domain.vaga.VagaUpdateDto;
import com.estacionamento.api.domain.veiculo.VeiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    @Autowired
    private VagaRepository vagaRepository;
    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PagamentoService pagamentoService;
    @Transactional
    public Ticket criarTicket(TicketCreateDto ticketCreateDto) {
        // Busca o veículo pelo id
        var veiculo = veiculoRepository.findById(ticketCreateDto.veiculoId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        // Busca a vaga pelo id
        var vaga = vagaRepository.findById(ticketCreateDto.vagaId())
                .orElseThrow(() -> new RuntimeException("Nenhuma vaga disponível para este tipo de veículo"));

        // Verifica se o tipo de veículo é compatível com a vaga e se a vaga está disponível
        if (!veiculo.getVeiculoTipo().equals(vaga.getVeiculoTipo()) || !vaga.getDisponibilidade()) {
            throw new RuntimeException("Tipo de veículo incompatível com a vaga ou vaga indisponível");
        }


        Ticket novoTicket = new Ticket(ticketCreateDto, veiculo, vaga);
        vaga.setDisponibilidade(false);
        vagaRepository.save(vaga);
        return ticketRepository.save(novoTicket);
    }

    public Ticket fecharTicket(TicketUpdateDto ticketUpdateDto) {
        var ticket = ticketRepository.findById(ticketUpdateDto.id())
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));

        ticket.atualizar(ticketUpdateDto);
        var valor = pagamentoService.calcularValorPagamento(ticket.getId());
        ticket.setValor(valor);
        return ticketRepository.save(ticket);

    }
}
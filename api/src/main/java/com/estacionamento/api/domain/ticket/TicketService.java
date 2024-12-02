package com.estacionamento.api.domain.ticket;

import com.estacionamento.api.domain.pagamento.PagamentoService;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.vaga.VagaListDto;
import com.estacionamento.api.domain.vaga.VagaRepository;
import com.estacionamento.api.domain.veiculo.VeiculoRepository;
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

    public Ticket criarTicket(TicketCreateDto ticketCreateDto) {

        var veiculo = veiculoRepository.findById(ticketCreateDto.veiculoId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));
        var vaga = vagaRepository.findById(ticketCreateDto.vagaId())
                .orElseThrow(() -> new RuntimeException("Nenhuma vaga disponível para este tipo de veículo"));

        if (veiculo.getVeiculoTipo().equals(vaga.getVeiculoTipo()) && vaga.isDisponibilidade()) {
            Ticket novoTicket = new Ticket();
            novoTicket.setVeiculo(veiculo);
            novoTicket.setVaga(vaga);
            novoTicket.setHoraEntrada(ticketCreateDto.horaEntrada());

            vaga.atualizar(new VagaListDto(vaga));
            vagaRepository.save(vaga);

            return ticketRepository.save(novoTicket);
        } else {
            throw new RuntimeException("Tipo de veículo incompatível com a vaga ou vaga indisponível");
        }
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
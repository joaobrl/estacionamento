package com.estacionamento.api.domain.ticket;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.cliente.ClienteRepository;
import com.estacionamento.api.domain.cliente.TipoPlano;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.estacionamento.EstacionamentoRepository;
import com.estacionamento.api.domain.exceptions.*;
import com.estacionamento.api.domain.pagamento.PagamentoService;
import com.estacionamento.api.domain.ticket.dto.TicketCreateDto;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private EstacionamentoRepository estacionamentoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PagamentoService pagamentoService;

    @Transactional
    public Ticket criarTicket(TicketCreateDto ticketCreateDto) {
        // Buscar o cliente completo pela placa do veículo
        Cliente cliente = clienteRepository.findClienteByVeiculoPlaca(ticketCreateDto.veiculo().getPlaca()).orElse(null);
        var ticketAberto = ticketRepository.findByHoraSaidaIsNull();

        var ticketEncontrado = ticketAberto.stream()
                .filter(t -> t.getVeiculo().getPlaca().equals(ticketCreateDto.veiculo().getPlaca()))
                .findFirst()
                .orElse(null);

        if (ticketEncontrado != null) {
            throw new VeiculoComTicketEmAberto(ticketEncontrado.getId());
        }

        // Se o veículo for um caminhão
        if (ticketCreateDto.veiculo().getVeiculoTipo() == VeiculoTipo.CAMINHAO) {
            if (cliente == null || cliente.getTipoPlano() == null || !cliente.getTipoPlano().equals(TipoPlano.CAMINHAO_ESPECIAL)) {
                throw new PlanoMensalObrigatorioException();
            }
        }

        // Buscar o estacionamento pelo id
        var estacionamento = estacionamentoRepository.findById(ticketCreateDto.estacionamentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estacionamento", ticketCreateDto.estacionamentoId()));

        // Verificar disponibilidade da vaga
        Vaga vagaDisponivel = verificarDisponibilidadeVaga(estacionamento, ticketCreateDto.veiculo());
        var vagaEscolhida = vagaDisponivel.getNumeroVaga();

        var veiculo = ticketCreateDto.veiculo();
        // Criar o novo ticket
        Ticket novoTicket = new Ticket(estacionamento, vagaEscolhida, veiculo);

        // Ocupar a vaga
        vagaDisponivel.ocuparVaga();
        estacionamentoRepository.save(estacionamento);

        // Salva o ticket
        return ticketRepository.save(novoTicket);
    }

    @Transactional
    public Ticket fecharTicket(Long id) {
        // Buscar o ticket pelo ID
        var ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ticket", id));

        // Verificar se o Ticket já foi fechado
        if (ticket.getHoraSaida() != null) {
            throw new TicketJaFechadoException(ticket.getId());
        }

        // Atualizar informações do ticket
        ticket.atualizar();

        // Buscar o cliente completo pela placa do veículo
        var cliente = clienteRepository.findClienteByVeiculoPlaca(ticket.getVeiculo().getPlaca()).orElse(null);

        // Verificar se o cliente possui plano ativo
        if (cliente != null) {
            if (cliente.getPlanoMensalAtivo()) {
                if (cliente.getTipoPlano().equals(TipoPlano.BASIC)) {
                    LocalTime horaEntrada = ticket.getHoraEntrada().toLocalTime();
                    LocalTime horaSaida = ticket.getHoraSaida().toLocalTime();

                    // Verificar se entrou antes das 8h ou saiu depois das 20:15h
                    if (horaEntrada.isBefore(LocalTime.of(8, 0))
                            || horaSaida.isAfter(LocalTime.of(20, 0).plusMinutes(15))) {
                        ticket.setValor(ticket.multaPorHorario());
                    } else {
                        ticket.setValor(BigDecimal.ZERO);
                        ticket.setPago(true);
                    }

                } else {
                    ticket.setValor(BigDecimal.ZERO);
                    ticket.setPago(true);
                }
            }
        } else {
            ticket.setValor(ticket.calcularValor());
        }

        // Salvar e retornar o ticket atualizado
        return ticketRepository.save(ticket);
    }

    private Vaga verificarDisponibilidadeVaga(Estacionamento estacionamento, Veiculo veiculo) {
        return estacionamento.getVagas()
                .stream()
                .filter(Vaga::getDisponibilidade)
                .filter(vaga -> vaga.getVeiculoTipo().equals(veiculo.getVeiculoTipo()))
                .findFirst()
                .orElseThrow(VagaNaoDisponivelException::new);
    }

    public Ticket buscarTicketPorId(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ticket", id));
    }
}
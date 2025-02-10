package com.estacionamento.api.domain.ticket;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.cliente.ClienteRepository;
import com.estacionamento.api.domain.cliente.TipoPlano;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.estacionamento.EstacionamentoRepository;
import com.estacionamento.api.domain.exceptions.PlanoMensalObrigatorioException;
import com.estacionamento.api.domain.exceptions.RecursoNaoEncontradoException;
import com.estacionamento.api.domain.exceptions.TicketJaFechadoException;
import com.estacionamento.api.domain.exceptions.VagaNaoDisponivelException;
import com.estacionamento.api.domain.pagamento.PagamentoService;
import com.estacionamento.api.domain.ticket.dto.TicketCreateDto;
import com.estacionamento.api.domain.ticket.dto.TicketUpdateDto;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.veiculo.dto.VeiculoCreateDto;
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

        // Se o veículo for um caminhão
        if (ticketCreateDto.veiculo().getVeiculoTipo() == VeiculoTipo.CAMINHAO) {
            // Verificar se o cliente existe e se o tipo de plano é CAMINHAO_ESPECIAL
            if (cliente == null || !cliente.getTipoPlano().equals(TipoPlano.CAMINHAO_ESPECIAL)) {
                throw new PlanoMensalObrigatorioException();
            }
        }

        // Buscar o estacionamento pelo id
        var estacionamento = estacionamentoRepository.findById(ticketCreateDto.estacionamentoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estacionamento", ticketCreateDto.estacionamentoId()));

        // Verificar disponibilidade da vaga
        Vaga vagaDisponivel = verificarDisponibilidadeVaga(estacionamento, ticketCreateDto.veiculo());
        var vagaEscolhida = vagaDisponivel.getNumeroVaga();

        // Criar o novo ticket
        Ticket novoTicket = new Ticket(estacionamento, vagaEscolhida);

        // Ocupar a vaga
        vagaDisponivel.ocuparVaga();
        estacionamentoRepository.save(estacionamento);

        // Salva o ticket
        return ticketRepository.save(novoTicket);
    }

    @Transactional
    public Ticket fecharTicket(TicketUpdateDto ticketUpdateDto) {
        // Buscar o ticket pelo ID
        var ticket = ticketRepository.findById(ticketUpdateDto.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ticket", ticketUpdateDto.id()));

        // Verificar se o Ticket já foi fechado
        if (ticket.getHoraSaida() != null) {
            throw new TicketJaFechadoException(ticket.getId());
        }

        // Atualizar informações do ticket
        ticket.atualizar(ticketUpdateDto);

        // Buscar o cliente completo pela placa do veículo
        Cliente cliente = clienteRepository.findClienteByVeiculoPlaca(ticket.getVeiculo().getPlaca()).orElse(null);

        // Verificar se o cliente possui plano ativo
        if (cliente.getPlanoMensalAtivo()) {
            // Verificar se o plano é BASIC
            if (cliente.getTipoPlano().equals(TipoPlano.BASIC)) {
                // Verificar entrada e saída
                LocalTime horaEntrada = ticket.getHoraEntrada().toLocalTime();
                LocalTime horaSaida = ticket.getHoraSaida().toLocalTime();

                // Verificar se entrou antes das 8h ou saiu depois das 20:15h
                if (horaEntrada.isBefore(LocalTime.of(8, 0)) || horaSaida.isAfter(LocalTime.of(20, 0).plusMinutes(15))) {
                    // Se o veículo ultrapassou o horário permitido, calcular o valor
                    ticket.setValor(ticket.multaPorHorario());

                } else {
                    // Se o veículo esteve dentro do horário permitido (8h - 20h + 15min), isento de cobrança
                    ticket.setValor(BigDecimal.ZERO);

                }
            } else {
                // Se o plano não for básico, isento de cobrança
                ticket.setValor(BigDecimal.ZERO);

            }
        } else {
            // Se não tiver plano ativo, calcular o valor do ticket
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
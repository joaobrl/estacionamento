package com.estacionamento.api.domain.pagamento;

import com.estacionamento.api.domain.ticket.TicketListDto;
import com.estacionamento.api.domain.ticket.TicketRepository;
import com.estacionamento.api.domain.vaga.VagaRepository;
import com.estacionamento.api.domain.vaga.VagaUpdateDto;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.veiculo.VeiculoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;

@Service
public class PagamentoService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private VeiculoRepository veiculoRepository;
    @Autowired
    private PagamentoRepository pagamentoRepository;
    @Autowired
    private VagaRepository vagaRepository;

    final Double VALOR_MOTO_HORA = 2.0;
    final Double VALOR_MOTO_DIA = 15.0;
    final Double VALOR_MOTO_MES = 200.0;

    final Double VALOR_CARRO_HORA = 5.0;
    final Double VALOR_CARRO_DIA = 30.0;
    final Double VALOR_CARRO_MES = 400.0;

    final Double VALOR_CAMINHAO_HORA = 10.0;
    final Double VALOR_CAMINHAO_DIA = 60.0;
    final Double VALOR_CAMINHAO_MES = 800.0;

    public BigDecimal calcularValorPagamento(Long ticketId) {
        var ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));
        var veiculo = veiculoRepository.findById(ticket.getVeiculo().getId())
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        if (ticket.getHoraEntrada() != null && ticket.getHoraSaida() != null) {
            Duration duration = Duration.between(ticket.getHoraEntrada(), ticket.getHoraSaida());
            long horas = duration.toHours();
            if (veiculo.getPlanoMensal()) {
                return calcularValorPlanoMensal(veiculo);
            }
            LocalDate dataEntrada = ticket.getHoraEntrada().toLocalDate();
            LocalDate dataSaida = ticket.getHoraSaida().toLocalDate();

            if (!dataEntrada.equals(dataSaida)) {
                return calcularValorDiaria(veiculo, duration);
            } else {
                return calcularValorHora(veiculo, horas);
            }
        } else {
            throw new RuntimeException("Horário de entrada ou saída não registrado");
        }
    };

    private BigDecimal calcularValorPlanoMensal(Veiculo veiculo) {
        if (veiculo.getPlanoMensal()) {
            return switch (veiculo.getVeiculoTipo()) {
                case CARRO -> BigDecimal.valueOf(VALOR_CARRO_MES);
                case MOTO -> BigDecimal.valueOf(VALOR_MOTO_MES);
                case CAMINHAO -> BigDecimal.valueOf(VALOR_CAMINHAO_MES);
                default -> throw new RuntimeException("Tipo de veículo desconhecido");
            };
        } else {
            throw new RuntimeException("Veículo não possui plano mensal");}
    }
    private BigDecimal calcularValorDiaria(Veiculo veiculo, Duration duration) {
        long dias = duration.toDays() + 1;
        return switch (veiculo.getVeiculoTipo()) {
            case CARRO -> BigDecimal.valueOf(dias * VALOR_CARRO_DIA);
            case MOTO -> BigDecimal.valueOf(dias * VALOR_MOTO_DIA);
            case CAMINHAO -> BigDecimal.valueOf(dias * VALOR_CAMINHAO_DIA);
            default -> throw new RuntimeException("Tipo de veículo desconhecido");
        };
    }
    private BigDecimal calcularValorHora(Veiculo veiculo, long horas) {
        return switch (veiculo.getVeiculoTipo()) {
        case CARRO -> BigDecimal.valueOf(horas * VALOR_CARRO_HORA);
        case MOTO -> BigDecimal.valueOf(horas * VALOR_MOTO_HORA);
        case CAMINHAO -> BigDecimal.valueOf(horas * VALOR_CAMINHAO_HORA);
        default -> throw new RuntimeException("Tipo de veículo desconhecido");
        };
    }

    @Transactional
    public Pagamento pagamentoTicket(PagamentoDto pagamentoDto){
        var ticket = ticketRepository.findById(pagamentoDto.ticketId())
                .orElseThrow(() -> new RuntimeException("Ticket não encontrado"));
        var vaga = vagaRepository.findById(ticket.getVaga().getId())
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));

        Pagamento novoPagamento = new Pagamento(pagamentoDto, ticket);
        vaga.setDisponibilidade(true);
        vagaRepository.save(vaga);
        return pagamentoRepository.save(novoPagamento);
    }
}


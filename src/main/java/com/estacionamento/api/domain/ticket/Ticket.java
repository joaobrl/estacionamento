package com.estacionamento.api.domain.ticket;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.infra.config.ValoresTarifas;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @Embedded
    private Veiculo veiculo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "estacionamento_id")
    private Estacionamento estacionamento;

    private String numeroVaga;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime horaEntrada;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime horaSaida;

    private Boolean pago = false;

    private BigDecimal valor;

    public Ticket(Estacionamento estacionamento, String numeroVaga, Veiculo veiculo) {
        this.veiculo = veiculo;
        this.estacionamento = estacionamento;
        this.numeroVaga = numeroVaga;
        this.horaEntrada = LocalDateTime.now();
        this.valor = BigDecimal.ZERO;
        this.pago = false;
    }

    // Método para atualizar o ticket com a hora de saída
    public void atualizar() {
        this.horaSaida = LocalDateTime.now();
    }

    // Método para calcular o valor do ticket
    public BigDecimal calcularValor() {
        // Calcular duração da permanência
        Duration duracao = Duration.between(horaEntrada, horaSaida);
        long minutosTotais = duracao.toMinutes();
        long horasCompletas = duracao.toHours();
        long minutosExcedentes = minutosTotais % 60;

        // Verificar mudança de dia (se entrar num dia e sair em outro, é cobrado diária)
        boolean mudouDeDia = !horaEntrada.toLocalDate().equals(horaSaida.toLocalDate());

        // Determinar valores por tipo de veículo
        double valorHora;
        double valorDia;
        switch (veiculo.getVeiculoTipo()) {
            case MOTO -> {
                valorHora = ValoresTarifas.MOTO_HORA;
                valorDia = ValoresTarifas.MOTO_DIA;
            }
            case CARRO -> {
                valorHora = ValoresTarifas.CARRO_HORA;
                valorDia = ValoresTarifas.CARRO_DIA;
            }
            default -> throw new IllegalArgumentException("Tipo de veículo inválido");
        }

        // Verificar se houve mudança de dia ou se o tempo de permanencia ultrapassou 6h
        if (mudouDeDia || horasCompletas >= 6) {
            return BigDecimal.valueOf(calcularValorDiario(duracao, valorDia));
        }

        // Arredondamento do valor por hora com base na tolerância de 15 minutos
        if (minutosExcedentes > 15) {
            horasCompletas += 1;
        }

        // Cobrança minima de 1 hora
        if (horasCompletas < 1) {
            horasCompletas = 1; // Cobrança mínima de 1 hora
        }

        return BigDecimal.valueOf(horasCompletas * valorHora); // Cálculo do valor total com base no número de horas
    }

    private double calcularValorDiario(Duration duracao, double valorDia) {
        // Se o tempo de permanência ultrapassar 24h, será cobrado mais uma diária
        long diasCompletos = duracao.toDays();
        long minutosRestantes = duracao.toMinutes() % (24 * 60);

        // Tolerância de 15 minutos para cobrar uma diária adicional
        if (minutosRestantes >= 15) {
            diasCompletos += 1;
        }

        // Retorna o valor da diária baseado no número de dias completos (incluindo a tolerância de 15 minutos)
        return diasCompletos * valorDia;
    }

    public BigDecimal multaPorHorario() {
        return BigDecimal.valueOf(50.0);
    }
}

package com.estacionamento.api.domain.ticket;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.cliente.TipoPlano;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.infra.config.ValoresTarifas;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
        // Calcular a duração da permanência
        Duration duracao = Duration.between(horaEntrada, horaSaida);
        long minutosTotais = duracao.toMinutes();
        long horasCompletas = duracao.toHours();
        long minutosExcedentes = minutosTotais % 60;

        // Verificar se houve mudança de dia
        boolean mudouDeDia = !horaEntrada.toLocalDate().equals(horaSaida.toLocalDate());

        // Determinar os valores por tipo de veículo
        double valorHora;
        double valorDia;
        switch (veiculo.getVeiculoTipo()) {
            case MOTO:
                valorHora = ValoresTarifas.MOTO_HORA;
                valorDia = ValoresTarifas.MOTO_DIA;
                break;
            case CARRO:
                valorHora = ValoresTarifas.CARRO_HORA;
                valorDia = ValoresTarifas.CARRO_DIA;
                break;
            default:
                throw new IllegalArgumentException("Tipo de veículo inválido");
        }

        // Verifica a permanência entre 6h e 24h para cobrar a diária
        if (horasCompletas >= 6 && horasCompletas < 24) {
            return BigDecimal.valueOf(valorDia);
        }

        // Se houver mudança de dia, cobra diária
        if (mudouDeDia) {
            return BigDecimal.valueOf(valorDia);
        }

        // Caso a permanência seja maior que 24h, calcula as diárias e horas extras
        if (horasCompletas >= 24) {
            // Calcular o número de dias completos
            long diasCompletos = duracao.toDays();
            long minutosRestantes = duracao.toMinutes() % (24 * 60);
            double valorTotal = diasCompletos * valorDia;

            // Se o tempo restante for superior a 6h, cobra mais uma diária
            if (minutosRestantes >= 6 * 60) {
                valorTotal += valorDia;
                minutosRestantes -= 6 * 60; // Reinicia a contagem de horas extras
            }

            // Se houver horas extras depois de completar o dia, cobra por elas
            if (minutosRestantes > 0) {
                valorTotal += (minutosRestantes / 60) * valorHora;
            }

            return BigDecimal.valueOf(valorTotal);
        }

        // Caso a permanência seja inferior a 6h, verifica se há cobrança mínima de 1 hora
        if (minutosExcedentes > 15) {
            horasCompletas += 1; // Adiciona 1 hora extra se o tempo exceder 15 minutos
        }

        if (horasCompletas < 1) {
            horasCompletas = 1; // Garantir cobrança mínima de 1 hora
        }

        return BigDecimal.valueOf(horasCompletas * valorHora); // Calcula o valor com base nas horas
    }

}

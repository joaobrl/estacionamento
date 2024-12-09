package com.estacionamento.api.domain.ticket;

import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "veiculo_id")
    private Veiculo veiculo;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vaga_id")
    private Vaga vaga;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime horaEntrada;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime horaSaida;

    private BigDecimal valor;

    public Ticket(TicketCreateDto dados, Veiculo veiculo, Vaga vaga) {
        this.veiculo = veiculo;
        this.vaga = vaga;
        this.horaEntrada = LocalDateTime.now();
    }

    public void atualizar(TicketUpdateDto dados) {
        if (dados.horaSaida().isAfter(this.horaEntrada)) {
            this.horaSaida = dados.horaSaida();
        }
    }
}

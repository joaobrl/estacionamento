package com.estacionamento.api.domain.ticket;

import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.ticket.TicketCreateDto;
import com.estacionamento.api.domain.ticket.TicketUpdateDto;
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

    public Ticket(TicketCreateDto dados) {
        this.veiculo = new Veiculo();
        this.vaga = new Vaga();
        this.horaEntrada = LocalDateTime.now();
    }

    public void atualizar(TicketUpdateDto dados) {
        this.horaSaida = dados.horaSaida();
    }
}

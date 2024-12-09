package com.estacionamento.api.domain.pagamento;

import com.estacionamento.api.domain.ticket.Ticket;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    private LocalDateTime dataPagamento = LocalDateTime.now();
    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    public Pagamento(PagamentoDto pagamentoDto, Ticket ticket) {
        this.ticket = ticket;
        this.metodoPagamento = pagamentoDto.metodoPagamento();
    }
}

package com.estacionamento.api.domain.pagamento;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.pagamento.dto.PagamentoPlanoMensalDto;
import com.estacionamento.api.domain.pagamento.dto.PagamentoTicketDto;
import com.estacionamento.api.domain.ticket.Ticket;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @OneToOne
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "estacionamento_id")
    private Estacionamento estacionamento;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;

    @Enumerated(EnumType.STRING)
    private TipoPagamento tipoPagamento;

    private BigDecimal valor;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime dataPagamento;

    // Construtor para pagamento de Ticket
    public Pagamento(PagamentoTicketDto pagamentoTicketDto, Ticket ticket, Estacionamento estacionamento) {
        this.ticket = ticket;
        this.estacionamento = estacionamento;
        this.metodoPagamento = pagamentoTicketDto.metodoPagamento();
        this.valor = ticket.getValor();
        this.tipoPagamento = TipoPagamento.TICKET;
        this.dataPagamento = LocalDateTime.now();
    }

    // Construtor para pagamento de Plano Mensal
    public Pagamento(PagamentoPlanoMensalDto pagamentoPlanoMensalDto, Cliente cliente, BigDecimal valor) {
        this.cliente = cliente;
        this.valor = valor;
        this.metodoPagamento = pagamentoPlanoMensalDto.metodoPagamento();
        this.tipoPagamento = TipoPagamento.PLANO_MENSAL;
        this.dataPagamento = LocalDateTime.now();
    }
}

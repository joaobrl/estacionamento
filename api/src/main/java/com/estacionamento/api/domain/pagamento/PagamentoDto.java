package com.estacionamento.api.domain.pagamento;

import com.estacionamento.api.domain.ticket.Ticket;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PagamentoDto(
        @NotNull
        Long ticketId,
        @NotNull
        MetodoPagamento metodoPagamento
) {
        public PagamentoDto(Pagamento pagamento) {
        this(
                pagamento.getTicket().getId(),
                pagamento.getMetodoPagamento());
        }
}

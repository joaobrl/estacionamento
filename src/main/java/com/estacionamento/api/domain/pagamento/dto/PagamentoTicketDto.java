package com.estacionamento.api.domain.pagamento.dto;

import com.estacionamento.api.domain.pagamento.MetodoPagamento;
import com.estacionamento.api.domain.pagamento.Pagamento;
import jakarta.validation.constraints.NotNull;

public record PagamentoTicketDto(
        @NotNull
        Long ticketId,
        @NotNull
        MetodoPagamento metodoPagamento
) {
        public PagamentoTicketDto(Pagamento pagamento) {
        this(
                pagamento.getTicket().getId(),
                pagamento.getMetodoPagamento());
        }
}

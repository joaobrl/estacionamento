package com.estacionamento.api.domain.pagamento.dto;

import com.estacionamento.api.domain.pagamento.MetodoPagamento;
import com.estacionamento.api.domain.pagamento.Pagamento;
import com.estacionamento.api.domain.pagamento.TipoPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoListDto(
        Long id,
        Long ticketId,
        Long estacionamentoId,
        MetodoPagamento metodoPagamento,
        TipoPagamento tipoPagamento,
        BigDecimal valor,
        LocalDateTime dataPagamento
) {
    public PagamentoListDto(Pagamento pagamento) {
        this(
                pagamento.getId(),
                pagamento.getTicket().getId(),
                pagamento.getEstacionamento().getId(),
                pagamento.getMetodoPagamento(),
                pagamento.getTipoPagamento(),
                pagamento.getValor(),
                pagamento.getDataPagamento()
                );
    }

}

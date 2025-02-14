package com.estacionamento.api.domain.pagamento.dto;

import com.estacionamento.api.domain.pagamento.MetodoPagamento;
import com.estacionamento.api.domain.pagamento.Pagamento;
import com.estacionamento.api.domain.pagamento.TipoPagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PagamentoReciboDto(
        Long id,
        Long clienteId,
        MetodoPagamento metodoPagamento,
        TipoPagamento tipoPagamento,
        BigDecimal valor,
        LocalDateTime dataPagamento
) {
    public PagamentoReciboDto(Pagamento pagamento) {
        this(
                pagamento.getId(),
                pagamento.getCliente().getId(),
                pagamento.getMetodoPagamento(),
                pagamento.getTipoPagamento(),
                pagamento.getValor(),
                pagamento.getDataPagamento()
        );
    }

}


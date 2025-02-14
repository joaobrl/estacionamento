package com.estacionamento.api.domain.pagamento.dto;

import com.estacionamento.api.domain.cliente.TipoPlano;
import com.estacionamento.api.domain.pagamento.MetodoPagamento;
import com.estacionamento.api.domain.pagamento.Pagamento;
import jakarta.validation.constraints.NotNull;

public record PagamentoPlanoMensalDto(
        @NotNull
        Long clienteId,

        @NotNull
        MetodoPagamento metodoPagamento,
        @NotNull
        TipoPlano tipoPlano){
        public PagamentoPlanoMensalDto(Pagamento pagamento) {
        this(   pagamento.getId(),
                pagamento.getMetodoPagamento(),
                pagamento.getCliente().getTipoPlano());
        }
}

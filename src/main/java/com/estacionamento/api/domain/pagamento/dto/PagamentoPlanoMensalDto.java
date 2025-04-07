package com.estacionamento.api.domain.pagamento.dto;

import com.estacionamento.api.domain.cliente.TipoContrato;
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
        TipoPlano tipoPlano,
        @NotNull
        TipoContrato tipoContrato
){
        public PagamentoPlanoMensalDto(Pagamento pagamento) {
        this(   pagamento.getId(),
                pagamento.getMetodoPagamento(),
                pagamento.getCliente().getTipoPlano(),
                pagamento.getTipoContrato()
        );
        }
}

package com.estacionamento.api.domain.pagamento.validacao;

import com.estacionamento.api.domain.pagamento.dto.PagamentoTicketDto;

public interface ValidarPagamentos {

    public void validar(PagamentoTicketDto pagamentoTicketDto);
}

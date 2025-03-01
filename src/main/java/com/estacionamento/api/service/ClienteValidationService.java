package com.estacionamento.api.service;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.cliente.TipoPlano;
import com.estacionamento.api.domain.exceptions.PlanoMensalObrigatorioException;
import com.estacionamento.api.domain.ticket.dto.TicketCreateDto;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import org.springframework.stereotype.Service;

@Service
public class ClienteValidationService {

    public void validarPlanoMensalParaCaminhao(TicketCreateDto ticketCreateDto, Cliente cliente) {
        if (ticketCreateDto.veiculo().getVeiculoTipo() == VeiculoTipo.CAMINHAO) {
            if (cliente == null || cliente.getTipoPlano() == null || !cliente.getTipoPlano().equals(TipoPlano.CAMINHAO_ESPECIAL)) {
                throw new PlanoMensalObrigatorioException();
            }
        }
    }
}
package com.estacionamento.api.service;

import com.estacionamento.api.domain.ticket.Ticket;
import com.estacionamento.api.repository.EstacionamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VagaService {

    @Autowired
    private EstacionamentoRepository estacionamentoRepository;

    public void liberarVaga(Ticket ticket) {
        var estacionamento = ticket.getEstacionamento();
        estacionamento.getVagas()
                .stream()
                .filter(v -> v.getNumeroVaga().equals(ticket.getNumeroVaga()))
                .findFirst()
                .ifPresent(v -> v.liberarVaga(ticket.getNumeroVaga()));
        estacionamentoRepository.save(estacionamento);
    }
}
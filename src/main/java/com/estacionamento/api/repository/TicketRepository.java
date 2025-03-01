package com.estacionamento.api.repository;

import com.estacionamento.api.domain.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByHoraSaidaIsNull();
}

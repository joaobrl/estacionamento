package com.estacionamento.api.domain.ticket;

import com.estacionamento.api.domain.ticket.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}

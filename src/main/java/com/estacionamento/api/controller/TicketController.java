package com.estacionamento.api.controller;

import com.estacionamento.api.service.TicketService;
import com.estacionamento.api.domain.historico.dto.EntradaClienteDto;
import com.estacionamento.api.domain.ticket.dto.TicketListDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;



    @GetMapping("/{id}")
    public ResponseEntity listarTicket(@PathVariable Long id) {
        var ticket = ticketService.buscarTicketPorId(id);
        return ResponseEntity.ok(new TicketListDto(ticket));
    }

}

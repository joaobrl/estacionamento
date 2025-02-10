package com.estacionamento.api.controller;

import com.estacionamento.api.domain.ticket.*;
import com.estacionamento.api.domain.ticket.dto.TicketCreateDto;
import com.estacionamento.api.domain.ticket.dto.TicketListDto;
import com.estacionamento.api.domain.ticket.dto.TicketUpdateDto;
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
    private TicketRepository repository;
    @Autowired
    private TicketService ticketService;

    @PostMapping
    @Transactional
    public ResponseEntity abrirTicket (@RequestBody @Valid TicketCreateDto dados, UriComponentsBuilder uriBuilder) {
        var ticket = ticketService.criarTicket(dados);
        var uri = uriBuilder.path("/tickets/{id}").buildAndExpand(ticket.getId()).toUri();
        return ResponseEntity.created(uri).body(new TicketListDto(ticket));
    }

    @GetMapping("/{id}")
    public ResponseEntity listar(@PathVariable Long id) {
        var ticket = ticketService.buscarTicketPorId(id);
        return ResponseEntity.ok(new TicketListDto(ticket));
    }

    @PutMapping("/saida")
    @Transactional
    public ResponseEntity fecharTicket (@RequestBody @Valid TicketUpdateDto dados) {
        var ticket = ticketService.fecharTicket(dados);
        return ResponseEntity.ok(new TicketListDto(ticket));
    }

}

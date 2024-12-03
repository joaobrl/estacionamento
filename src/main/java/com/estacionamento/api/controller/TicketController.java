package com.estacionamento.api.controller;

import com.estacionamento.api.domain.ticket.*;
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
    public ResponseEntity cadastrar (@RequestBody @Valid TicketCreateDto dados, UriComponentsBuilder uriBuilder) {
        var ticket = ticketService.criarTicket(dados);
        var uri = uriBuilder.path("/tickets/{id}").buildAndExpand(ticket.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity listar(@PathVariable Long id) {
        var ticket = repository.getReferenceById(id);
        return ResponseEntity.ok(new TicketListDto(ticket));
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar (@RequestBody @Valid TicketUpdateDto dados) {
        var ticket = ticketService.fecharTicket(dados);
        return ResponseEntity.ok(new TicketListDto(ticket));
    }

}

package com.estacionamento.api.controller;

import com.estacionamento.api.domain.ticket.dto.TicketCreateDto;
import com.estacionamento.api.domain.ticket.dto.TicketListDto;
import com.estacionamento.api.service.MovimentacaoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/movimentacao")
@RequiredArgsConstructor
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    @PostMapping("/entrada")
    @Transactional
    public ResponseEntity registrarEntrada(@RequestBody @Valid TicketCreateDto dados, @RequestParam(required = false) String matricula, UriComponentsBuilder uriBuilder) {
        var ticket = movimentacaoService.registrarEntrada(dados, matricula);
        if (ticket == null) {
            return ResponseEntity.ok("Cliente com assinatura de plano, entrada registrada sem gerar ticket.");
        }
        var uri = uriBuilder.path("/tickets/{id}").buildAndExpand(ticket.getId()).toUri();
        return ResponseEntity.created(uri).body(new TicketListDto(ticket));
    }

    @PostMapping("/saida/{ticketId}")
    @Transactional
    public ResponseEntity registrarSaida(@PathVariable Long ticketId) {
        movimentacaoService.registrarSaida(ticketId);
        return ResponseEntity.ok("Saída registrada com sucesso.");
    }

}
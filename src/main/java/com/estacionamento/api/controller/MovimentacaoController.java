package com.estacionamento.api.controller;

import com.estacionamento.api.domain.historico.MovimentacaoClientePlano;
import com.estacionamento.api.domain.historico.dto.ClienteMensalistaDto;
import com.estacionamento.api.domain.historico.dto.EntradaClienteDto;
import com.estacionamento.api.domain.historico.dto.SaidaClienteDto;
import com.estacionamento.api.domain.ticket.Ticket;
import com.estacionamento.api.domain.ticket.dto.TicketListDto;
import com.estacionamento.api.service.MovimentacaoService;
import com.estacionamento.api.service.TicketService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/movimentacao")
@RequiredArgsConstructor
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;
    private final TicketService ticketService;

    @PostMapping("/entrada")
    public ResponseEntity<?> registrarEntrada(@RequestBody @Valid EntradaClienteDto entradaClienteDto, UriComponentsBuilder uriBuilder) {
        var resultado = movimentacaoService.processarEntradaOuCriarTicket(entradaClienteDto);

        if (resultado instanceof MovimentacaoClientePlano mensalista) {
            var uri = uriBuilder.path("/mensalistas/{id}").buildAndExpand(mensalista).toUri();
            return ResponseEntity.created(uri).body(new ClienteMensalistaDto(mensalista));
        } else if (resultado instanceof Ticket ticket) {
            var uri = uriBuilder.path("/tickets/{id}").buildAndExpand(ticket.getId()).toUri();
            return ResponseEntity.created(uri).body(new TicketListDto(ticket));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @PatchMapping("/saida")
    @Transactional
    public ResponseEntity registrarSaida(@RequestBody @Valid SaidaClienteDto saidaClienteDto) {
        var saida = movimentacaoService.registrarSaida(saidaClienteDto);
        return ResponseEntity.ok(new ClienteMensalistaDto (saida));
    }

    @GetMapping("/ticket/{id}")
    public ResponseEntity buscarTicket(@PathVariable Long id) {
        var ticket = ticketService.buscarTicketPorId(id);
        return ResponseEntity.ok(new TicketListDto(ticket));
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketListDto>> listarTickets() {
        var tickets = ticketService.listarTickets()
                .stream()
                .map(TicketListDto::new)
                .toList();
        return ResponseEntity.ok(tickets);
    }

}
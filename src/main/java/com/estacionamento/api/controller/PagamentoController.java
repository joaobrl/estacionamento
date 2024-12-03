package com.estacionamento.api.controller;

import com.estacionamento.api.domain.pagamento.PagamentoDto;
import com.estacionamento.api.domain.pagamento.Pagamento;
import com.estacionamento.api.domain.pagamento.PagamentoRepository;
import com.estacionamento.api.domain.pagamento.PagamentoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoRepository repository;
    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping
    @Transactional
    public ResponseEntity registarPagamento(@RequestBody @Valid PagamentoDto dados, UriComponentsBuilder uriComponentsBuilder) {
        var pagamento = pagamentoService.pagamentoTicket(dados.ticketId(), dados.metodoPagamento());
        var uri = uriComponentsBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();
        return ResponseEntity.created(uri).body(pagamento);
    }

    @GetMapping("/listar")
    public ResponseEntity listarPagamentos() {
        var pagamentos = repository.findAll();
        return ResponseEntity.ok(pagamentos);
    }

    @GetMapping("/{id}")
    public ResponseEntity detalharPagamento(@PathVariable Long id) {
        var pagamento = repository.getReferenceById(id);
        return ResponseEntity.ok(new PagamentoDto(pagamento));
    }

}

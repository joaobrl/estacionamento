package com.estacionamento.api.controller;

import com.estacionamento.api.domain.estacionamento.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/estacionamentos")
public class EstacionamentoController {

    @Autowired
    private EstacionamentoRepository repository;
    @Autowired
    private EstacionamentoService estacionamentoService;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar (@RequestBody @Valid EstacionamentoCreateDto dados, UriComponentsBuilder uriBuilder) {
        var estacionamento = estacionamentoService.cadastrarEstacionamento(dados);
        var uri = uriBuilder.path("/estacionamentos/{id}").buildAndExpand(estacionamento.getId()).toUri();
        return ResponseEntity.created(uri).body(new EstacionamentoListDto(estacionamento));
    }

    @GetMapping("/listar")
    public ResponseEntity listar() {
        var estacionamento = estacionamentoService.listarEstacionamentos();
        return ResponseEntity.ok(estacionamento);
    }

    @GetMapping("{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var estacionamento = estacionamentoService.detalharEstacionamento(id);
        return ResponseEntity.ok(new EstacionamentoListDto(estacionamento));
    }

    @PutMapping("/atualizar")
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid EstacionamentoUpdateDto dados) {
        var estacionamento = estacionamentoService.atualizarEstacionamento(dados);
        return ResponseEntity.ok(new EstacionamentoListDto(estacionamento));
    }
}

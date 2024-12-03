package com.estacionamento.api.controller;

import com.estacionamento.api.domain.estacionamento.EstacionamentoCreateDto;
import com.estacionamento.api.domain.estacionamento.EstacionamentoListDto;
import com.estacionamento.api.domain.estacionamento.EstacionamentoUpdateDto;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.estacionamento.EstacionamentoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("estacionamentos")
public class EstacionamentoController {

    @Autowired
    private EstacionamentoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar (@RequestBody @Valid EstacionamentoCreateDto dados, UriComponentsBuilder uriBuilder) {
        var estacionamento = new Estacionamento(dados);
        repository.save(estacionamento);
        var uri = uriBuilder.path("/estacionamentos/{id}").buildAndExpand(estacionamento.getId()).toUri();
        return ResponseEntity.created(uri).body(estacionamento);
    }

    @GetMapping("listar")
    public ResponseEntity listar() {
        return ResponseEntity.ok(repository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity detalhar(@PathVariable Long id) {
        var estacionamento = repository.getReferenceById(id);
        return ResponseEntity.ok(new EstacionamentoListDto(estacionamento));
    }

    @PutMapping("/atualizar")
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid EstacionamentoUpdateDto dados) {
        var estacionamento = repository.getReferenceById(dados.id());
        estacionamento.atualizar(dados);
        return ResponseEntity.ok(new EstacionamentoListDto(estacionamento));
    }
}

package com.estacionamento.api.controller;

import com.estacionamento.api.domain.vaga.VagaCreateDto;
import com.estacionamento.api.domain.vaga.VagaDto;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.vaga.VagaRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("vagas")
public class VagaController {

    @Autowired
    private VagaRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid VagaCreateDto dados, UriComponentsBuilder uriBuilder) {
        var vaga = new Vaga(dados);
        repository.save(vaga);
        var uri = uriBuilder.path("/vagas/{id}").buildAndExpand(vaga.getId()).toUri();
        return ResponseEntity.created(uri).body(vaga);
    }

    @GetMapping("/{id}")
    public ResponseEntity buscarPorId(@PathVariable Long id) {
        var vaga = repository.getReferenceById(id);
        return ResponseEntity.ok(new VagaDto(vaga));
    }

    @PutMapping("/atualizar")
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid VagaDto dados) {
        var vaga = repository.getReferenceById(dados.id());
        vaga.atualizar(dados);
        return ResponseEntity.ok(new VagaDto(vaga));
    }
}

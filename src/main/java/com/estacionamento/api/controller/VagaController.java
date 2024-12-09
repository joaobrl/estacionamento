package com.estacionamento.api.controller;

import com.estacionamento.api.domain.vaga.*;
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
    @Autowired
    private VagaService vagaService;

    @PostMapping
    @Transactional
    public ResponseEntity<VagaListDto> cadastrar(@RequestBody @Valid VagaCreateDto dados, UriComponentsBuilder uriBuilder) {
        var vaga = vagaService.criarVaga(dados);
        var uri = uriBuilder.path("/vagas/{id}").buildAndExpand(vaga.getId()).toUri();
        return ResponseEntity.created(uri).body(new VagaListDto(vaga));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VagaListDto> buscarPorId(@PathVariable Long id) {
        var vaga = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
        return ResponseEntity.ok(new VagaListDto(vaga));
    }

    @PutMapping("/atualizar")
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid VagaUpdateDto dados) {
        var vaga = vagaService.atualizarVaga(dados);
        return ResponseEntity.ok(new VagaListDto(vaga));
    }
}

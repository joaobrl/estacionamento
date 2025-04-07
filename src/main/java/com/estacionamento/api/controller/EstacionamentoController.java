package com.estacionamento.api.controller;

import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.vaga.dto.VagaListarDisponibilidadeDto;
import com.estacionamento.api.domain.vaga.dto.VagaUpdateDto;
import com.estacionamento.api.service.EstacionamentoService;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoCreateDto;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoDetalharDto;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoListDto;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoUpdateDto;
import com.estacionamento.api.domain.vaga.dto.VagaCreateDto;
import com.estacionamento.api.domain.vaga.dto.VagaDto;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/estacionamentos")
public class EstacionamentoController {

    @Autowired
    private EstacionamentoService estacionamentoService;

    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity cadastrarEstacionamento (@RequestBody @Valid EstacionamentoCreateDto dados, UriComponentsBuilder uriBuilder) {
        var estacionamento = estacionamentoService.cadastrarEstacionamento(dados);
        var uri = uriBuilder.path("/estacionamentos/{id}").buildAndExpand(estacionamento.getId()).toUri();
        return ResponseEntity.created(uri).body(new EstacionamentoDetalharDto(estacionamento));
    }

    @PostMapping("/{id}/cadastrar/vaga")
    @Transactional
    public ResponseEntity adicionarVaga(@PathVariable Long id, @RequestBody @Valid VagaCreateDto vagaCreateDto) {
        var estacionamento = estacionamentoService.adicionarVaga(id, vagaCreateDto);
        return ResponseEntity.ok().body(new EstacionamentoDetalharDto(estacionamento));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<EstacionamentoListDto>> listarEstacionamentos() {
        var estacionamento = estacionamentoService.listarEstacionamentos()
                .stream()
                .map(EstacionamentoListDto::new)
                .toList();
        return ResponseEntity.ok(estacionamento);
    }

    @GetMapping("{id}")
    public ResponseEntity detalharEstacionamento(@PathVariable Long id) {
        var estacionamento = estacionamentoService.detalharEstacionamento(id);
        return ResponseEntity.ok(new EstacionamentoDetalharDto(estacionamento));
    }

    @GetMapping("/{id}/listar/vagas")
    public ResponseEntity<List<VagaDto>> listarVagas(@PathVariable Long id, @RequestBody VagaListarDisponibilidadeDto filtro) {
        var vagas = estacionamentoService.listarVagas(id, filtro);
        var vagasDto = vagas.stream()
                .map(VagaDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(vagasDto);
    }

    @PutMapping("{id}/atualizar")
    @Transactional
    public ResponseEntity atualizarEstacionamento(@PathVariable Long id, @RequestBody @Valid EstacionamentoUpdateDto dados) {
        var estacionamento = estacionamentoService.atualizarEstacionamento(id, dados);
        return ResponseEntity.ok(estacionamento);
    }

    @PutMapping("/{id}/atualizar/vaga")
    @Transactional
    public ResponseEntity<VagaDto> atualizarVaga(@PathVariable Long id, @RequestBody @Valid VagaUpdateDto vagaUpdateDto) {
        var vagaAtualizada = estacionamentoService.atualizarVaga(id, vagaUpdateDto);
        return ResponseEntity.ok(vagaAtualizada);
    }
}

package com.estacionamento.api.controller;

import com.estacionamento.api.domain.estacionamento.*;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoCreateDto;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoDetalharDto;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoListDto;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoUpdateDto;
import com.estacionamento.api.domain.vaga.dto.VagaCreateDto;
import com.estacionamento.api.domain.vaga.dto.VagaDto;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

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

    @GetMapping("/{id}/vagas/disponiveis")
    public ResponseEntity<List<VagaDto>> listarVagasDisponiveis(@PathVariable Long id) {
        var estacionamento = estacionamentoService.listarVagasDisponiveis(id);
        return ResponseEntity.ok(estacionamento);
    }

    @GetMapping("/{id}/vagas/disponiveis/{veiculoTipo}")
    public ResponseEntity<List<VagaDto>> listarVagasDisponiveisPorVeiculo(@PathVariable Long id, @PathVariable String veiculoTipo) {
        var estacionamneto = estacionamentoService.listarVagasDisponiveisPorVeiculo(id, veiculoTipo);
        return ResponseEntity.ok(estacionamneto);
    }

    @GetMapping("/{id}/vagas/ocupadas")
    public ResponseEntity<List<VagaDto>> listarVagasOcupadas(@PathVariable Long id) {
        var estacionamento = estacionamentoService.listarVagasOcupadas(id);
        return ResponseEntity.ok(estacionamento);
    }

    @GetMapping("/{id}/vagas/ocupadas/{veiculoTipo}")
    public ResponseEntity<List<VagaDto>> listarVagasOcupadasPorVeiculo(@PathVariable Long id, @PathVariable String veiculoTipo) {
        var estacionamento = estacionamentoService.listarVagasOcupadasPorVeiculo(id, veiculoTipo);
        return ResponseEntity.ok(estacionamento);
    }

    @PutMapping("/atualizar")
    @Transactional
    public ResponseEntity atualizarEstacionamento(@RequestBody @Valid EstacionamentoUpdateDto dados) {
        var estacionamento = estacionamentoService.atualizarEstacionamento(dados);
        return ResponseEntity.ok(estacionamento);
    }
}

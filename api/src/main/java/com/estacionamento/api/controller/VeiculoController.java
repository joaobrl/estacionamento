package com.estacionamento.api.controller;

import com.estacionamento.api.domain.veiculo.VeiculoCreateDto;
import com.estacionamento.api.domain.veiculo.VeiculoListDto;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.veiculo.VeiculoRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    @Autowired
    private VeiculoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrarVeiculo(@RequestBody @Valid VeiculoCreateDto veiculoCreateDto, UriComponentsBuilder uriBuilder) {
        var veiculo = new Veiculo(veiculoCreateDto);
        repository.save(veiculo);
        var uri = uriBuilder.path("/veiculo/{id}").buildAndExpand(veiculo.getId()).toUri();
        return ResponseEntity.created(uri).body(new VeiculoListDto(veiculo));
    }

    @GetMapping("/{id}")
    public ResponseEntity listarVeiculos(@PathVariable Long id) {
        var veiculo = repository.getReferenceById(id);
        return ResponseEntity.ok(new VeiculoListDto(veiculo));
    }
}

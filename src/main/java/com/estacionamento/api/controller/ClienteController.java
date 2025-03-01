package com.estacionamento.api.controller;

import com.estacionamento.api.domain.cliente.dto.ClienteCreateDto;
import com.estacionamento.api.domain.cliente.dto.ClienteListDto;
import com.estacionamento.api.domain.cliente.dto.ClienteUpdateDto;
import com.estacionamento.api.domain.veiculo.dto.VeiculoCreateDto;
import com.estacionamento.api.domain.veiculo.dto.VeiculoListDto;
import com.estacionamento.api.service.ClienteService;
import com.estacionamento.api.service.VeiculoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final VeiculoService veiculoService;

    @PostMapping("/cadastrar")
    @Transactional
    public ResponseEntity cadastrarCliente(@RequestBody @Valid ClienteCreateDto clienteCreateDto, UriComponentsBuilder uriBuilder) {
        var cliente = clienteService.cadastrarCliente(clienteCreateDto);
        var uri = uriBuilder.path("/clientes/{id}").buildAndExpand(cliente.getId()).toUri();
        return ResponseEntity.created(uri).body(new ClienteListDto(cliente));
    }

    @PostMapping("/{id}/cadastrar/veiculos")
    @Transactional
    public ResponseEntity cadastrarVeiculo(@RequestBody @Valid VeiculoCreateDto veiculoCreateDto,@PathVariable Long id) {
        var cliente = veiculoService.cadastrarVeiculo(veiculoCreateDto, id);
        return ResponseEntity.ok().body(new ClienteListDto(cliente));
    }

    @PutMapping("/{id}/remover/veiculos")
    @Transactional
    public ResponseEntity removerVeiculo(@RequestBody @Valid VeiculoCreateDto veiculoCreateDto, @PathVariable Long id) {
        var cliente = veiculoService.removerVeiculo(id, veiculoCreateDto.placa());
        return ResponseEntity.ok().body(new ClienteListDto(cliente));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ClienteListDto>> listarClientes() {
        var clientes = clienteService.findAllClientes()
                .stream()
                .map(ClienteListDto::new)
                .toList();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/listar/ativos")
    public ResponseEntity<List<ClienteListDto>> listarClientesComPlanosAtivos() {
        var clientes = clienteService.findAllClientesComPlanosAtivos()
                .stream()
                .map(ClienteListDto::new)
                .toList();
        return ResponseEntity.ok(clientes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteListDto> buscarClientePorId(@PathVariable Long id) {
        var cliente = clienteService.findClienteById(id);
        return ResponseEntity.ok(new ClienteListDto(cliente));
    }

    @GetMapping("/veiculos")
    public ResponseEntity<List<VeiculoListDto>> listarVeiculosCadastrados() {
        var veiculos = clienteService.findAllVeiculos()
                .stream()
                .map(VeiculoListDto::new)
                .toList();
        return ResponseEntity.ok(veiculos);
    }

    @PutMapping("/{id}/atualizar")
    public ResponseEntity atualizarCliente(@RequestBody @Valid ClienteUpdateDto clienteUpdateDto, Long id) {
        var cliente = clienteService.atualizarCliente(id, clienteUpdateDto);
        return ResponseEntity.ok(new ClienteListDto(cliente));
    }
}

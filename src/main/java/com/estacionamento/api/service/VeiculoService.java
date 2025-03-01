package com.estacionamento.api.service;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.exceptions.RecursoNaoEncontradoException;
import com.estacionamento.api.domain.veiculo.dto.VeiculoCreateDto;
import com.estacionamento.api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VeiculoService {


    private final ClienteRepository clienteRepository;

    public Cliente cadastrarVeiculo(VeiculoCreateDto veiculoCreateDto, Long clienteId) {
        var cliente = findClienteById(clienteId);
        Optional<Cliente> veiculoCadastrado = clienteRepository.findClienteByVeiculoPlaca(veiculoCreateDto.placa());
        if (veiculoCadastrado.isPresent()) {
            throw new IllegalArgumentException("Veículo já cadastrado.");
        }
        cliente.adicionarVeiculo(veiculoCreateDto);
        return clienteRepository.save(cliente);
    }

    public Cliente removerVeiculo(Long clienteId, String placa) {
        Cliente cliente = findClienteById(clienteId);
        cliente.removerVeiculo(placa);
        return clienteRepository.save(cliente);
    }

    private Cliente findClienteById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", id));
    }
}
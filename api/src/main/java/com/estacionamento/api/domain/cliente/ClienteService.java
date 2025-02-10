package com.estacionamento.api.domain.cliente;

import com.estacionamento.api.domain.cliente.dto.ClienteCreateDto;
import com.estacionamento.api.domain.cliente.dto.ClienteUpdateDto;
import com.estacionamento.api.domain.exceptions.RecursoNaoEncontradoException;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.veiculo.dto.VeiculoCreateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // cadastrar cliente
    public Cliente cadastrarCliente (ClienteCreateDto clienteCreateDto) {
        var cliente = new Cliente(clienteCreateDto);
        return clienteRepository.save(cliente);
    }

    // cadastrar veiculo
    public Cliente cadastrarVeiculo (VeiculoCreateDto veiculoCreateDto, Long id) {
        var cliente = findClienteById(id);
        cliente.adicionarVeiculo(veiculoCreateDto);
        return clienteRepository.save(cliente);
    }

    // listar clientes
    public List<Cliente> findAllClientes() {
        return clienteRepository.findAll();
    }

    // listar clientes com planos ativos
    public List<Cliente> findAllClientesComPlanosAtivos() {
        return clienteRepository.findAllByPlanoMensalAtivoTrue();
    }

    // buscar cliente por id
    public Cliente findClienteById(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente", id));
    }

    // Listar veiculos
    public List<Veiculo> findAllVeiculos() {
        List<Cliente> clientes = clienteRepository.findAll();
        List<Veiculo> veiculos = new ArrayList<>();

        for (Cliente cliente : clientes) {
            veiculos.addAll(cliente.getVeiculos());
        }
        return veiculos;
    }

    // Atualizar cliente
    public Cliente atualizarCliente(Long id, ClienteUpdateDto clienteUpdateDto) {
        var cliente = findClienteById(id);
        cliente.atualizar(clienteUpdateDto);
        return clienteRepository.save(cliente);
    }
}

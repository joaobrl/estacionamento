package com.estacionamento.api.service;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.cliente.TipoPlano;
import com.estacionamento.api.domain.cliente.dto.ClienteCreateDto;
import com.estacionamento.api.domain.cliente.dto.ClienteUpdateDto;
import com.estacionamento.api.domain.exceptions.RecursoNaoEncontradoException;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    // cadastrar cliente
    public Cliente cadastrarCliente(ClienteCreateDto clienteCreateDto) {
        Optional<Cliente> clienteExistente = clienteRepository.findByCpf(clienteCreateDto.cpf());
        if (clienteExistente.isPresent()) {
            throw new IllegalArgumentException("Cliente já cadastrado.");
        }
        var cliente = new Cliente(clienteCreateDto);
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

    public void atualizarPlanoMensal(Cliente cliente, TipoPlano tipoPlano, boolean renovacaoAutomatica) {
        if (cliente.getPlanoMensalAtivo() != null && cliente.getPlanoMensalAtivo()) {
            cliente.setValidadePlanoMensal(cliente.getValidadePlanoMensal().plusMonths(1));
        } else {
            cliente.setValidadePlanoMensal(LocalDateTime.now().plusMonths(1));
        }

        cliente.setPlanoMensalAtivo(true);
        cliente.setTipoPlano(tipoPlano);
        cliente.setRenovacaoAutomaticaPlano(renovacaoAutomatica);

        clienteRepository.save(cliente);
    }

    public Optional<Cliente> findClienteByVeiculoPlaca(String placa) {
        return clienteRepository.findClienteByVeiculoPlaca(placa);
    }
}
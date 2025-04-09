package com.estacionamento.api.service;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.cliente.dto.ClienteCreateDto;
import com.estacionamento.api.domain.cliente.dto.ClienteUpdateDto;
import com.estacionamento.api.domain.exceptions.RecursoNaoEncontradoException;
import com.estacionamento.api.domain.pagamento.dto.PagamentoPlanoMensalDto;
import com.estacionamento.api.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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


    // Atualizar cliente
    public Cliente atualizarCliente(Long id, ClienteUpdateDto clienteUpdateDto) {
        var cliente = findClienteById(id);
        cliente.atualizar(clienteUpdateDto);
        return clienteRepository.save(cliente);
    }

    public void atualizarPlanoMensal(PagamentoPlanoMensalDto pagamentoPlanoMensalDto) {
        var cliente = findClienteById(pagamentoPlanoMensalDto.clienteId());

        if (cliente.getPlanoMensalAtivo() != null && cliente.getPlanoMensalAtivo()) {
            cliente.setValidadePlanoMensal(cliente.getValidadePlanoMensal().plusMonths(pagamentoPlanoMensalDto.tipoContrato().getMeses()));
        } else {
            cliente.setValidadePlanoMensal(LocalDateTime.now().plusMonths(pagamentoPlanoMensalDto.tipoContrato().getMeses()));
        }

        cliente.setPlanoMensalAtivo(true);
        cliente.setTipoPlano(pagamentoPlanoMensalDto.tipoPlano());

        clienteRepository.save(cliente);
    }

    public Optional<Cliente> findClienteByVeiculoPlaca(String placa) {
        return clienteRepository.findClienteByVeiculoPlaca(placa);
    }

    public Cliente buscarPorMatricula(String matricula) {
        return clienteRepository.findByMatricula(matricula)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado para a matrícula: " + matricula));
    }

    public boolean isClienteComVeiculoNoEstacionamento(String matricula) {
        Cliente cliente = buscarPorMatricula(matricula);

        return cliente.getMovimentacaoClientePlano().stream()
                .anyMatch(mov -> mov.getHoraSaida() == null);
    }
}
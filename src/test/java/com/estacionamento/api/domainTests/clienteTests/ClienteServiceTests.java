package com.estacionamento.api.domainTests.clienteTests;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.cliente.dto.ClienteCreateDto;
import com.estacionamento.api.domain.cliente.dto.ClienteUpdateDto;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.veiculo.dto.VeiculoCreateDto;
import com.estacionamento.api.repository.ClienteRepository;
import com.estacionamento.api.service.ClienteService;
import com.estacionamento.api.service.VeiculoService;
import com.estacionamento.api.utils.UtilsMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTests {

    @Mock
    private ClienteRepository clienteRepository;
    @InjectMocks
    private ClienteService clienteService;
    @InjectMocks
    private VeiculoService veiculoService;
    private Cliente cliente;
    private ClienteCreateDto clienteCreateDto;
    private ClienteUpdateDto clienteUpdateDto;
    private VeiculoCreateDto veiculoCreateDto;

    @BeforeEach
    void setUp() {
      cliente = new Cliente();
      clienteCreateDto = UtilsMock.criarClienteCreateDto();
      clienteUpdateDto = UtilsMock.atualizarClienteUpdateDto();
      veiculoCreateDto = UtilsMock.criarVeiculoCreateDto();
    }

    @Test
    @DisplayName("Cadastrar cliente")
    public void testCadastrarCliente() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = clienteService.cadastrarCliente(clienteCreateDto);

        assertNotNull(result);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Atualizar cliente")
    public void testAtualizarCliente() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = clienteService.atualizarCliente(1L, clienteUpdateDto);

        assertNotNull(result);
        verify(clienteRepository, times(1)).findById(anyLong());
        verify(clienteRepository, times(1)).save(any(Cliente.class));

        assertEquals(cliente.getNome(), result.getNome());
        assertEquals(cliente.getCpf(), result.getCpf());
        assertEquals(cliente.getEmail(), result.getEmail());
        assertEquals(cliente.getTelefone(), result.getTelefone());
        assertEquals(cliente.getTipoPlano(), result.getTipoPlano());
        assertEquals(cliente.getPlanoMensalAtivo(), result.getPlanoMensalAtivo());
    }

    @Test
    @DisplayName("Listar clientes")
    public void testFindAllClientes() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        List<Cliente> result = clienteService.findAllClientes();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Listar clientes com planos ativos")
    public void testFindAllClientesComPlanosAtivos() {
        when(clienteRepository.findAllByPlanoMensalAtivoTrue()).thenReturn(List.of(cliente));

        List<Cliente> result = clienteService.findAllClientesComPlanosAtivos();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(clienteRepository, times(1)).findAllByPlanoMensalAtivoTrue();
    }

    @Test
    @DisplayName("Buscar cliente por id")
    public void testFindClienteById() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));

        Cliente result = clienteService.findClienteById(1L);

        assertNotNull(result);
        verify(clienteRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Cadastrar veículo")
    public void testCadastrarVeiculo() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente result = veiculoService.cadastrarVeiculo(veiculoCreateDto, 1L);

        assertNotNull(result);
        verify(clienteRepository, times(1)).save(any(Cliente.class));
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    @DisplayName("Listar veículos")
    public void testFindAllVeiculos() {
        when(clienteRepository.findAll()).thenReturn(List.of(cliente));

        List<Veiculo> result = clienteService.findAllVeiculos();

        assertNotNull(result);
        verify(clienteRepository, times(1)).findAll();
    }


}

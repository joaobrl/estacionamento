package com.estacionamento.api.domainTests.movimentacaoTests;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.cliente.TipoPlano;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.exceptions.PlanoMensalObrigatorioException;
import com.estacionamento.api.domain.exceptions.PlanoNaoAtivoException;
import com.estacionamento.api.domain.exceptions.VeiculoEstacionadoException;
import com.estacionamento.api.domain.historico.MovimentacaoClientePlano;
import com.estacionamento.api.domain.historico.dto.EntradaClienteDto;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import com.estacionamento.api.service.*;
import com.estacionamento.api.utils.UtilsMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MovimentacaoServiceTest {

    @InjectMocks
    private MovimentacaoService movimentacaoService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private ClienteValidationService clienteValidationService;

    @Mock
    private TicketValidationService ticketValidationService;

    @Mock
    private TicketService ticketService;

    @Mock
    private EstacionamentoService estacionamentoService;

    @Mock
    private Vaga vaga;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveRegistrarEntradaComPlanoAtivoSemVeiculoEstacionado() {
        // Mock do cliente com plano ativo
        Cliente clienteMock = new Cliente();
        clienteMock = UtilsMock.clienteMensalistaJSON();
        EntradaClienteDto entradaMock = UtilsMock.entradaClienteMensalistaJSON();
        Estacionamento estacionamentoMock = new Estacionamento();
        estacionamentoMock = UtilsMock.estacionamentoJSON();

        Vaga vagaMock = estacionamentoMock.getVagas().get(3);

        // Configurar mocks
        when(clienteService.buscarPorMatricula(entradaMock.matricula())).thenReturn(clienteMock);
        when(clienteValidationService.isPlanoAtivo(clienteMock)).thenReturn(true);
        when(clienteValidationService.isClienteComVeiculoNoEstacionamento(entradaMock.matricula())).thenReturn(false);
        when(ticketValidationService.validateCriarTicket(any())).thenReturn(false);

        // Executar o método
        MovimentacaoClientePlano result = movimentacaoService.registrarEntrada(entradaMock);

        // Verificar o resultado
        assertNotNull(result);
        verify(clienteService, times(1)).buscarPorMatricula(entradaMock.matricula());
        verify(clienteValidationService, times(1)).isPlanoAtivo(clienteMock);
    }

    @Test
    void deveLancarExcecaoQuandoVeiculoJaEstacionado() {
        // Mock do cliente
        Cliente clienteMock = new Cliente();
        clienteMock.setPlanoMensalAtivo(true);

        EntradaClienteDto entradaMock = UtilsMock.entradaClienteMensalistaJSON();

        // Configurar mocks
        when(clienteService.buscarPorMatricula(entradaMock.matricula())).thenReturn(clienteMock);
        when(clienteValidationService.isClienteComVeiculoNoEstacionamento(entradaMock.matricula())).thenReturn(true);

        // Executar e verificar exceção
        VeiculoEstacionadoException exception = assertThrows(VeiculoEstacionadoException.class, () ->
                movimentacaoService.registrarEntrada(entradaMock));

        assertEquals("O cliente já possui veículo no estacionamento.", exception.getMessage());
        verify(clienteValidationService, times(1)).isClienteComVeiculoNoEstacionamento(entradaMock.matricula());
    }

    @Test
    void deveLancarExcecaoQuandoPlanoInativo() {
        // Mock do cliente com plano inativo
        Cliente clienteMock = new Cliente();
        clienteMock.setPlanoMensalAtivo(false);

        EntradaClienteDto entradaMock = UtilsMock.entradaClienteMensalistaJSON();

        // Configurar mocks
        when(clienteService.buscarPorMatricula(entradaMock.matricula())).thenReturn(clienteMock);
        when(clienteValidationService.isPlanoAtivo(clienteMock)).thenReturn(false);

        // Executar e verificar exceção
        PlanoNaoAtivoException exception = assertThrows(PlanoNaoAtivoException.class, () ->
                movimentacaoService.registrarEntrada(entradaMock));

        assertEquals("O plano do cliente não está ativo.", exception.getMessage());
        verify(clienteValidationService, times(1)).isPlanoAtivo(clienteMock);
    }

    @Test
    void deveLancarExcecaoQuandoVeiculoPossuiTicketAberto() {
        // Mock do cliente
        Cliente clienteMock = new Cliente();
        clienteMock.setPlanoMensalAtivo(true);

        EntradaClienteDto entradaMock = UtilsMock.entradaClienteMensalistaJSON();

        // Configurar mocks
        when(clienteService.buscarPorMatricula(entradaMock.matricula())).thenReturn(clienteMock);
        when(ticketValidationService.validateCriarTicket(any())).thenReturn(true);

        // Executar e verificar exceção
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                movimentacaoService.registrarEntrada(entradaMock));

        assertEquals("O veículo já possui um ticket em aberto.", exception.getMessage());
        verify(ticketValidationService, times(1)).validateCriarTicket(any());
    }

    @Test
    void deveRegistrarEntradaParaCaminhaoComPlanoEspecial() {
        // Mock do cliente com plano especial
        Cliente clienteMock = new Cliente();
        clienteMock.setPlanoMensalAtivo(true);
        clienteMock.setTipoPlano(TipoPlano.ESPECIAL);

        EntradaClienteDto entradaMock = UtilsMock.entradaCaminhaoJSON();

        // Configurar mocks
        when(clienteService.buscarPorMatricula(entradaMock.matricula())).thenReturn(clienteMock);
        when(clienteValidationService.isPlanoAtivo(clienteMock)).thenReturn(true);
        when(clienteValidationService.isClienteComVeiculoNoEstacionamento(entradaMock.matricula())).thenReturn(false);
        when(ticketValidationService.validateCriarTicket(any())).thenReturn(false);

        // Executar o método
        MovimentacaoClientePlano result = movimentacaoService.registrarEntrada(entradaMock);

        // Verificar o resultado
        assertNotNull(result);
        verify(clienteValidationService, times(1)).validarPlanoMensalParaCaminhao(entradaMock, clienteMock);
    }

    @Test
    void deveLancarExcecaoParaCaminhaoSemPlanoEspecial() {
        // Mock do cliente sem plano especial
        Cliente clienteMock = new Cliente();
        clienteMock.setPlanoMensalAtivo(true);
        clienteMock.setTipoPlano(TipoPlano.BASIC);

        EntradaClienteDto entradaMock = UtilsMock.entradaCaminhaoJSON();

        // Configurar mocks
        when(clienteService.buscarPorMatricula(entradaMock.matricula())).thenReturn(clienteMock);

        // Executar e verificar exceção
        PlanoMensalObrigatorioException exception = assertThrows(PlanoMensalObrigatorioException.class, () ->
                movimentacaoService.registrarEntrada(entradaMock));

        assertNotNull(exception);
        verify(clienteValidationService, times(1)).validarPlanoMensalParaCaminhao(entradaMock, clienteMock);
    }
}
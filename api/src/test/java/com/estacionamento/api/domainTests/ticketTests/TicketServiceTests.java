package com.estacionamento.api.domainTests.ticketTests;

import com.estacionamento.api.domain.cliente.ClienteRepository;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.estacionamento.EstacionamentoRepository;
import com.estacionamento.api.domain.exceptions.PlanoMensalObrigatorioException;
import com.estacionamento.api.domain.exceptions.RecursoNaoEncontradoException;
import com.estacionamento.api.domain.exceptions.VagaNaoDisponivelException;
import com.estacionamento.api.domain.ticket.Ticket;
import com.estacionamento.api.domain.ticket.TicketRepository;
import com.estacionamento.api.domain.ticket.TicketService;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.utils.UtilsMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.estacionamento.api.utils.UtilsMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTests {

    @InjectMocks
    private TicketService ticketService;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private EstacionamentoRepository estacionamentoRepository;
    @Mock
    private ClienteRepository clienteRepository;
    private Ticket ticket;
    private Estacionamento estacionamento;

    @BeforeEach
    public void setUp() {
        ticket = UtilsMock.ticketMock();
        estacionamento = UtilsMock.estacionamentoMock();

    }

    @Test
    @DisplayName("Criar ticket - Estacionamento não encontrado")
    public void testCriarTicket_EstacionamentoNaoEncontrado() {
        when(estacionamentoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> ticketService.criarTicket(UtilsMock.ticketCreateDtoMock()));

        verify(estacionamentoRepository, times(1)).findById(anyLong());
    }


    @Test
    @DisplayName("Criar ticket - Caminhão com cliente sem cadastro")
    public void testCriarTicket_CaminhaoSemClienteCadastrado() {
        when(clienteRepository.findClienteByVeiculoPlaca(anyString())).thenReturn(Optional.empty());

        assertThrows(PlanoMensalObrigatorioException.class, () -> ticketService.criarTicket(UtilsMock.ticketCreateDtoMock_Cenario2()));

        verify(clienteRepository, times(1)).findClienteByVeiculoPlaca(anyString());
    }

    @Test
    @DisplayName("Criar ticket - Caminhão sem plano mensal")
    public void testCriarTicket_CaminhaoSemPlanoMensal() {
        when(clienteRepository.findClienteByVeiculoPlaca(anyString())).thenReturn(Optional.of(clienteMock_Cenario1()));

        assertThrows(PlanoMensalObrigatorioException.class, () -> ticketService.criarTicket(UtilsMock.ticketCreateDtoMock_Cenario2()));

        verify(clienteRepository, times(1)).findClienteByVeiculoPlaca(anyString());
    }


    @Test
    @DisplayName("Criar ticket - Sem vaga disponível")
    public void testCriarTicket_SemVagaDisponivel() {
        when(estacionamentoRepository.findById(anyLong())).thenReturn(Optional.of(estacionamento));
        var disponibilidade = UtilsMock.estacionamentoMock()
                .getVagas()
                .stream()
                .filter(Vaga::getDisponibilidade)
                .filter(vaga -> vaga.getVeiculoTipo().equals(veiculoMock().getVeiculoTipo()))
                .findFirst();

        assertThrows(VagaNaoDisponivelException.class, () -> ticketService.criarTicket(UtilsMock.ticketCreateDtoMock()));
        verify(estacionamentoRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Criar ticket - Ticket criado com sucesso")
    public void testCriarTicket() {
        when(estacionamentoRepository.findById(anyLong())).thenReturn(Optional.of(UtilsMock.estacionamentoDisponivelMock()));
        when(clienteRepository.findClienteByVeiculoPlaca(anyString())).thenReturn(Optional.of(clienteMock()));
        when(estacionamentoRepository.save(any(Estacionamento.class))).thenReturn(UtilsMock.estacionamentoDisponivelMock());
        when(ticketRepository.save(any(Ticket.class))).thenReturn(ticket);


        var vagaDisponivel = UtilsMock.estacionamentoDisponivelMock()
                .getVagas()
                .stream()
                .filter(Vaga::getDisponibilidade)
                .filter(vaga -> vaga.getVeiculoTipo().equals(veiculoMock().getVeiculoTipo()))
                .findFirst();

        assertTrue(vagaDisponivel.isPresent());

        var vagaEscolhida = vagaDisponivel.get().getNumeroVaga();
        estacionamentoDisponivelMock()
                .getVagas()
                .stream()
                .filter(v -> v.getNumeroVaga().equals(vagaEscolhida))
                .findFirst()
                .ifPresent(v -> v.setDisponibilidade(false));


        Ticket result = ticketService.criarTicket(UtilsMock.ticketCreateDtoMock());

        verify(ticketRepository, times(1)).save(any(Ticket.class));

        assertFalse(vagaEscolhida.isEmpty());
    }

    public void testFecharTicket_TicketJaFechado() {
        // Testa se o ticket é criado corretamente
    }

    public void testFechatTicket_VeiculoNaoCadastrado () {
        // Testa se o ticket é criado corretamente
    }

    public void testFecharTicket_ClienteSemPlano () {
        // Testa se o ticket é criado corretamente
    }

    public void testFecharTicket_MultaAtrasado () {
        // Testa se o ticket é criado corretamente
    }

    public void testFecharTicket() {

    }

    @Test
    @DisplayName("Buscar ticket por id - Ticket encontrado")
    public void testBuscarTicket_Encontrado() {
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(ticket));

        Ticket ticketEncontrado = ticketService.buscarTicketPorId(1L);

        assertEquals(ticket, ticketEncontrado);
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Buscar ticket por id - Ticket não encontrado")
    public void testBuscarTicket_NaoEncontrado() {
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> ticketService.buscarTicketPorId(1L));

        verify(ticketRepository, times(1)).findById(anyLong());
    }
}

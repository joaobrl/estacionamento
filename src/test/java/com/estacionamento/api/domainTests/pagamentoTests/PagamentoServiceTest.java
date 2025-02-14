package com.estacionamento.api.domainTests.pagamentoTests;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.cliente.ClienteRepository;
import com.estacionamento.api.domain.cliente.TipoPlano;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.estacionamento.EstacionamentoRepository;
import com.estacionamento.api.domain.exceptions.RecursoNaoEncontradoException;
import com.estacionamento.api.domain.exceptions.TicketJaPagoException;
import com.estacionamento.api.domain.pagamento.Pagamento;
import com.estacionamento.api.domain.pagamento.PagamentoRepository;
import com.estacionamento.api.domain.pagamento.PagamentoService;
import com.estacionamento.api.domain.pagamento.dto.PagamentoPlanoMensalDto;
import com.estacionamento.api.domain.pagamento.dto.PagamentoTicketDto;
import com.estacionamento.api.domain.ticket.Ticket;
import com.estacionamento.api.domain.ticket.TicketRepository;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.utils.UtilsMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PagamentoServiceTest {
    @InjectMocks
    private PagamentoService pagamentoService;
    private Pagamento pagamento;
    private Ticket ticket;
    private Vaga vaga;
    private Cliente cliente;
    private Estacionamento estacionamento;
    private PagamentoPlanoMensalDto pagamentoPlanoMensal;
    private PagamentoTicketDto pagamentoTicketDto;
    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private PagamentoRepository pagamentoRepository;
    @Mock
    private EstacionamentoRepository estacionamentoRepository;
    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    void setUp() {
        pagamento = new Pagamento();
        ticket = UtilsMock.ticketMock();
        estacionamento = UtilsMock.estacionamentoMock();
        vaga = UtilsMock.vagaMock();
        cliente = UtilsMock.clienteMock();
        pagamentoPlanoMensal = UtilsMock.pagamentoPlanoMensalMock();
        pagamentoTicketDto = UtilsMock.pagamentoTicketDtoMock();
    }

    @Test
    @DisplayName("Pagamento de ticket - Ticket não encontrado")
    public void testPagamentoTicket_TicketNaoEncontrado() {
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> pagamentoService.pagamentoTicket(pagamentoTicketDto));

        verify(ticketRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Pagamento de ticket - Estacionamento não encontrado")
    public void testPagamentoTicket_EstacionamentoNaoEncontrado() {
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(ticket));
        when(estacionamentoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> pagamentoService.pagamentoTicket(pagamentoTicketDto)
        );

        verify(ticketRepository, times(1)).findById(anyLong());
        verify(estacionamentoRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Pagamento de ticket - Ticket já pago")
    public void testPagamentoTicket_TicketJaPago() {
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(ticket));
        ticket.setPago(true);

        assertThrows(TicketJaPagoException.class, () -> pagamentoService.pagamentoTicket(pagamentoTicketDto));

        verify(ticketRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Pagamento de ticket - Deve realizar pagamento corretamente")
    public void testPagamentoTicket_RealizarPagamento() {
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(ticket));
        when(estacionamentoRepository.findById(anyLong())).thenReturn(Optional.of(estacionamento));
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(new Pagamento(pagamentoTicketDto, ticket, estacionamento));
        when(estacionamentoRepository.save(any(Estacionamento.class))).thenReturn(estacionamento);

        Pagamento pagamento = pagamentoService.pagamentoTicket(pagamentoTicketDto);
        vaga.setDisponibilidade(true);
        assertNotNull(pagamento);
        verify(ticketRepository, times(1)).findById(anyLong());
        verify(estacionamentoRepository, times(1)).findById(anyLong());
        verify(estacionamentoRepository, times(1)).save(estacionamento);
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));
        assertTrue(ticket.getPago());
        assertTrue(vaga.getDisponibilidade());
    }

    @Test
    @DisplayName("Pagamento de plano mensal - Cliente não encontrado")
    public void testPagamentoPlanoMensal_ClienteNaoEncontrado() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(RecursoNaoEncontradoException.class, () -> pagamentoService.pagamentoPlanoMensal(pagamentoPlanoMensal));

        verify(clienteRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Pagamento de plano mensal - ativando plano mensal")
    public void testPagamentoPlanoMensal_AtivandoPlanoMensal() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        double valor = TipoPlano.PREMIUM.getValorPlano();
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(new Pagamento(pagamentoPlanoMensal, cliente, BigDecimal.valueOf(valor)));

        cliente.setTipoPlano(TipoPlano.PREMIUM);
        cliente.setPlanoMensalAtivo(true);
        cliente.setValidadePlanoMensal(LocalDateTime.now().plusDays(30));
        Pagamento pagamento = pagamentoService.pagamentoPlanoMensal(pagamentoPlanoMensal);

        assertNotNull(pagamento);
        verify(clienteRepository, times(1)).findById(anyLong());
        verify(clienteRepository, times(1)).save(cliente);
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));

        assertTrue(cliente.getPlanoMensalAtivo());
        assertNotNull(cliente.getValidadePlanoMensal());
    }

    @Test
    @DisplayName("Pagamento de plano mensal - renovando plano mensal")
    public void testPagamentoPlanoMensal_RenovandoPlanoMensal() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(UtilsMock.clienteMock_PlanoPremium()));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(UtilsMock.clienteMock_PlanoPremium());
        double valor = TipoPlano.PREMIUM.getValorPlano();
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(new Pagamento(pagamentoPlanoMensal, UtilsMock.clienteMock_PlanoPremium(), BigDecimal.valueOf(valor)));

        cliente.setTipoPlano(TipoPlano.PREMIUM);
        cliente.setPlanoMensalAtivo(true);
        LocalDateTime validade = UtilsMock.clienteMock_PlanoPremium().getValidadePlanoMensal();
        cliente.setValidadePlanoMensal(validade.plusDays(30));
        Pagamento pagamento = pagamentoService.pagamentoPlanoMensal(pagamentoPlanoMensal);

        assertNotNull(pagamento);
        verify(clienteRepository, times(1)).findById(anyLong());
        verify(clienteRepository, times(1)).save(cliente);
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));

        assertTrue(cliente.getPlanoMensalAtivo());
        assertNotNull(cliente.getValidadePlanoMensal());
    }

    @Test
    @DisplayName("Pagamento de plano mensal - migrando plano mensal Downgrade")
    public void testPagamentoPlanoMensal_DowngradePlanoMensal() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(UtilsMock.clienteMock_PlanoPremium()));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(UtilsMock.clienteMock_PlanoPremium());
        double valor = TipoPlano.BASIC.getValorPlano();
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(new Pagamento(pagamentoPlanoMensal, UtilsMock.clienteMock_PlanoPremium(), BigDecimal.valueOf(valor)));

        cliente.setTipoPlano(TipoPlano.BASIC);
        cliente.setPlanoMensalAtivo(true);
        LocalDateTime validade = UtilsMock.clienteMock_PlanoPremium().getValidadePlanoMensal();
        cliente.setValidadePlanoMensal(validade.plusDays(30));
        Pagamento pagamento = pagamentoService.pagamentoPlanoMensal(pagamentoPlanoMensal);

        assertNotNull(pagamento);
        verify(clienteRepository, times(1)).findById(anyLong());
        verify(clienteRepository, times(1)).save(cliente);
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));

        assertTrue(cliente.getPlanoMensalAtivo());
        assertNotNull(cliente.getValidadePlanoMensal());
        assertEquals(valor, cliente.getTipoPlano().getValorPlano());
    }

    @Test
    @DisplayName("Pagamento de plano mensal - migrando plano mensal Upgrade")
    public void testPagamentoPlanoMensal_UpgradePlanoMensal() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.of(UtilsMock.clienteMock_PlanoBasic()));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(UtilsMock.clienteMock_PlanoBasic());
        long diasRestantes = ChronoUnit.DAYS.between(LocalDateTime.now(), UtilsMock.clienteMock_PlanoBasic().getValidadePlanoMensal());
        double valor = TipoPlano.calcularMigracaoDePlano(TipoPlano.BASIC, TipoPlano.PREMIUM, diasRestantes);
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(new Pagamento(pagamentoPlanoMensal, UtilsMock.clienteMock_PlanoBasic(), BigDecimal.valueOf(valor)));

        cliente.setTipoPlano(TipoPlano.PREMIUM);
        cliente.setPlanoMensalAtivo(true);
        LocalDateTime validade = UtilsMock.clienteMock_PlanoBasic().getValidadePlanoMensal();
        cliente.setValidadePlanoMensal(validade.plusDays(30));
        Pagamento pagamento = pagamentoService.pagamentoPlanoMensal(pagamentoPlanoMensal);

        assertNotNull(pagamento);
        verify(clienteRepository, times(1)).findById(anyLong());
        verify(clienteRepository, times(1)).save(cliente);
        verify(pagamentoRepository, times(1)).save(any(Pagamento.class));

        assertTrue(cliente.getPlanoMensalAtivo());
        assertNotNull(cliente.getValidadePlanoMensal());
        assertEquals(766.6666666666667, valor);
    }

    @Test
    @DisplayName("Listar pagamentos")
    public void testListarPagamentos() {
        when(pagamentoRepository.findAll()).thenReturn(List.of(pagamento));

        List<Pagamento> result = pagamentoService.listarPagamentos();

        assertNotNull(result);
        verify(pagamentoRepository, times(1)).findAll();
    }

}

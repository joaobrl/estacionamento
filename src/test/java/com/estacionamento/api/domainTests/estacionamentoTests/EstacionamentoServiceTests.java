package com.estacionamento.api.domainTests.estacionamentoTests;

import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.estacionamento.EstacionamentoRepository;
import com.estacionamento.api.domain.estacionamento.EstacionamentoService;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoCreateDto;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoUpdateDto;
import com.estacionamento.api.domain.exceptions.EstacionamentoLotadoException;
import com.estacionamento.api.domain.exceptions.VagaComNumeroJaExistenteException;
import com.estacionamento.api.domain.vaga.dto.VagaCreateDto;
import com.estacionamento.api.domain.vaga.dto.VagaDto;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;
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

import static com.estacionamento.api.domain.veiculo.VeiculoTipo.CARRO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EstacionamentoServiceTests {

    @InjectMocks
    private EstacionamentoService estacionamentoService;
    @Mock
    private EstacionamentoRepository estacionamentoRepository;
    private Estacionamento estacionamento;
    private EstacionamentoCreateDto estacionamentoCreateDto;
    private EstacionamentoUpdateDto estacionamentoUpdateDto;
    private VagaCreateDto vagaCreateDto;

    @BeforeEach
    void SetUp() {
        estacionamento = new Estacionamento();
        estacionamentoCreateDto = UtilsMock.criarEstacionamentoCreateDto();
        estacionamentoUpdateDto = UtilsMock.atualizarEstacionamentoUpdateDto();
        vagaCreateDto = UtilsMock.vagaDisponivel();
    }

    @Test
    @DisplayName("Cadastrar estacionamento")
    public void testCadastrarEstacionamento() {
        when(estacionamentoRepository.save(any(Estacionamento.class))).thenReturn(estacionamento);

        Estacionamento result = estacionamentoService.cadastrarEstacionamento(estacionamentoCreateDto);

        assertNotNull(result);
        verify(estacionamentoRepository, times(1)).save(any(Estacionamento.class));

        assertEquals(estacionamento.getNome(), result.getNome());
        assertEquals(estacionamento.getCapacidade(), result.getCapacidade());
    }

    @Test
    @DisplayName("Atualizar estacionamento")
    public void testAtualizarEstacionamento() {
        when(estacionamentoRepository.findById(anyLong())).thenReturn(Optional.of(UtilsMock.estacionamentoMock()));
        when(estacionamentoRepository.save(any(Estacionamento.class))).thenReturn(UtilsMock.estacionamentoMock());

        Estacionamento result = estacionamentoService.atualizarEstacionamento(estacionamentoUpdateDto);

        assertNotNull(result);
        verify(estacionamentoRepository, times(1)).save(any(Estacionamento.class));

        assertEquals(UtilsMock.estacionamentoMock().getNome(), result.getNome());
        assertEquals(UtilsMock.estacionamentoMock().getCapacidade(), result.getCapacidade());
    }

    @Test
    @DisplayName("Cadastrar vaga estacionamento")
    public void testCadastrarVagaEstacionamento() {
        when(estacionamentoRepository.findById(anyLong())).thenReturn(Optional.of(UtilsMock.estacionamentoMock()));
        when(estacionamentoRepository.save(any(Estacionamento.class))).thenReturn(UtilsMock.estacionamentoMock());

        Estacionamento result = estacionamentoService.adicionarVaga(1L, vagaCreateDto);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Cadastrar vaga estacionamento - Vaga com número já existente")
    public void testCadastrarVagaJaExistente() {
        when(estacionamentoRepository.findById(anyLong())).thenReturn(Optional.of(UtilsMock.estacionamentoMock()));

        assertThrows(VagaComNumeroJaExistenteException.class, () -> estacionamentoService.adicionarVaga(1L, UtilsMock.vagaJaCadastrada()));

        verify(estacionamentoRepository, times(1)).findById(anyLong());
    }

        @Test
    @DisplayName("Detalhar estacionamento")
    public void testDetalharEstacionamento() {
        when(estacionamentoRepository.findById(anyLong())).thenReturn(Optional.of(estacionamento));

        Estacionamento result = estacionamentoService.detalharEstacionamento(1L);

        assertNotNull(result);
        verify(estacionamentoRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Listar estacionamentos")
    public void testListarEstacionamentos() {
        when(estacionamentoRepository.findAll()).thenReturn(List.of(estacionamento));

        List<Estacionamento> result = estacionamentoService.listarEstacionamentos();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(estacionamentoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Listar vagas disponíveis")
    public void testListarVagasDisponiveis() {
        when(estacionamentoRepository.findEstacionamentoComVagasDisponiveis(anyLong())).thenReturn(List.of(UtilsMock.estacionamentoDisponivelMock()));

        List<VagaDto> result = estacionamentoService.listarVagasDisponiveis(1L);

        assertNotNull(result);
        verify(estacionamentoRepository, times(1)).findEstacionamentoComVagasDisponiveis(anyLong());
        assertEquals(3, result.size());
    }

//    @Test
//    @DisplayName("Listar vagas disponíveis - Estacionamento lotado")
//    public void testEstacionamentoLotado() {
//        when(estacionamentoRepository.findEstacionamentoComVagasDisponiveis(anyLong())).thenReturn(List.of(UtilsMock.estacionamentoMock()));
//
//        assertThrows(EstacionamentoLotadoException.class, () -> estacionamentoService.listarVagasDisponiveis(anyLong()));
//
//        verify(estacionamentoRepository, times(1)).findEstacionamentoComVagasDisponiveis(anyLong());
//
//    }

    @Test
    @DisplayName("Listar vagas disponíveis por tipo de veiculos")
    public void testListarVagasDisponiveisPorVeiculo() {
        when(estacionamentoRepository.findEstacionamentoComVagasDisponiveisPorTipoVeiculo(anyLong(), eq(VeiculoTipo.CARRO))).thenReturn(List.of(UtilsMock.estacionamentoDisponivelMock()));

        List<VagaDto> result = estacionamentoService.listarVagasDisponiveisPorVeiculo(1L, "CARRO");

        assertNotNull(result);
        verify(estacionamentoRepository, times(1)).findEstacionamentoComVagasDisponiveisPorTipoVeiculo(anyLong(), eq(CARRO));
        assertEquals(1, result.size());
    }


    @Test
    @DisplayName("Listar vagas ocupadas")
    public void testListarVagasOcupadas() {
        when(estacionamentoRepository.findEstacionamentoComVagasOcupadas(anyLong())).thenReturn(List.of(UtilsMock.estacionamentoMock()));

        List<VagaDto> result = estacionamentoService.listarVagasOcupadas(anyLong());

        verify(estacionamentoRepository, times(1)).findEstacionamentoComVagasOcupadas(anyLong());
        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Listar vagas ocupadas por tipo de veículos")
    public void testListarVagasOcupadasPorVeiculo() {
        when(estacionamentoRepository.findEstacionamentoComVagasOcupadasPorTipoVeiculo(anyLong(), eq(VeiculoTipo.CARRO))).thenReturn(List.of(UtilsMock.estacionamentoMock()));

        List<VagaDto> result = estacionamentoService.listarVagasOcupadasPorVeiculo(1L, "CARRO");

        assertNotNull(result);
        verify(estacionamentoRepository, times(1)).findEstacionamentoComVagasOcupadasPorTipoVeiculo(anyLong(), eq(CARRO));
        assertEquals(1, result.size());
    }
}

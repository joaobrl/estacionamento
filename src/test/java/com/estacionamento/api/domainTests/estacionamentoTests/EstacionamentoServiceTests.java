package com.estacionamento.api.domainTests.estacionamentoTests;

import com.estacionamento.api.domain.endereco.Endereco;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoCreateDto;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoUpdateDto;
import com.estacionamento.api.domain.exceptions.VagaComNumeroJaExistenteException;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.vaga.dto.VagaCreateDto;
import com.estacionamento.api.repository.EstacionamentoRepository;
import com.estacionamento.api.service.EstacionamentoService;
import com.estacionamento.api.utils.UtilsMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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
        estacionamentoCreateDto = UtilsMock.criarEstacionamentoJSON();
        estacionamento = new Estacionamento(estacionamentoCreateDto);
        estacionamentoUpdateDto = UtilsMock.atualizarEstacionamentoJSON();
        vagaCreateDto = UtilsMock.cadastrarVagaLivreJSON();
    }


    @Test
    @DisplayName("Cadastrar estacionamento")
    public void testCadastrarEstacionamento() {
        EstacionamentoCreateDto dto = UtilsMock.criarEstacionamentoJSON();

        when(estacionamentoRepository.save(any(Estacionamento.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Estacionamento result = estacionamentoService.cadastrarEstacionamento(dto);

        assertNotNull(result);
        ArgumentCaptor<Estacionamento> captor = ArgumentCaptor.forClass(Estacionamento.class);
        verify(estacionamentoRepository, times(1)).save(captor.capture());

        Estacionamento estacionamentoSalvo = captor.getValue();

        assertEquals(dto.nome(), estacionamentoSalvo.getNome());
        assertEquals(dto.capacidade(), estacionamentoSalvo.getCapacidade());
    }


    @Test
    @DisplayName("Atualizar estacionamento")
    public void testAtualizarEstacionamento() {
        when(estacionamentoRepository.findById(anyLong())).thenReturn(Optional.of(estacionamento));
        when(estacionamentoRepository.save(any(Estacionamento.class))).thenReturn(estacionamento);

        Estacionamento result = estacionamentoService.atualizarEstacionamento(1L, estacionamentoUpdateDto);

        ArgumentCaptor<Estacionamento> captor = ArgumentCaptor.forClass(Estacionamento.class);
        verify(estacionamentoRepository).save(captor.capture());
        Estacionamento salvo = captor.getValue();

        assertNotNull(result);
        assertEquals(estacionamentoUpdateDto.nome(), salvo.getNome());
        assertEquals(estacionamentoUpdateDto.capacidade(), salvo.getCapacidade());
        assertEquals(estacionamentoUpdateDto.nome(), result.getNome());
    }

    @Test
    @DisplayName("Cadastrar vaga estacionamento")
    public void testCadastrarVagaEstacionamento() {
        when(estacionamentoRepository.findById(anyLong())).thenReturn(Optional.of(estacionamento));
        when(estacionamentoRepository.save(any(Estacionamento.class))).thenReturn(estacionamento);

        Estacionamento result = estacionamentoService.adicionarVaga(1L, vagaCreateDto);

        ArgumentCaptor<Estacionamento> captor = ArgumentCaptor.forClass(Estacionamento.class);
        verify(estacionamentoRepository).save(captor.capture());
        Estacionamento salvo = captor.getValue();

        assertTrue(
                salvo.getVagas().stream()
                        .anyMatch(v -> v.getNumeroVaga().equals(vagaCreateDto.numeroVaga()))
        );
        assertTrue(result.getVagas().stream()
                .anyMatch(v -> v.getNumeroVaga().equals(vagaCreateDto.numeroVaga())));


        assertNotNull(result);
    }


    @Test
    @DisplayName("Cadastrar vaga estacionamento - Vaga com número já existente")
    public void testCadastrarVagaJaExistente() {
        var vagaJaCadastrada = UtilsMock.cadastrarVagaExistenteJSON();
        estacionamento.getVagas().add(new Vaga(vagaJaCadastrada));

        when(estacionamentoRepository.findById(anyLong())).thenReturn(Optional.of(estacionamento));

        assertThrows(VagaComNumeroJaExistenteException.class, () ->
                estacionamentoService.adicionarVaga(1L, vagaJaCadastrada)
        );

        verify(estacionamentoRepository, times(1)).findById(anyLong());
    }


    @Test
    @DisplayName("Detalhar estacionamento")
    public void testDetalharEstacionamento() {
        when(estacionamentoRepository.findById(anyLong())).thenReturn(Optional.of(estacionamento));

        Estacionamento result = estacionamentoService.detalharEstacionamento(1L);

        assertNotNull(result);
        assertEquals(estacionamento.getNome(), result.getNome());
        verify(estacionamentoRepository, times(1)).findById(anyLong());
    }


    @Test
    @DisplayName("Listar estacionamentos")
    public void testListarEstacionamentos() {
        when(estacionamentoRepository.findAll()).thenReturn(List.of(estacionamento));

        List<Estacionamento> result = estacionamentoService.listarEstacionamentos();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(estacionamento.getNome(), result.get(0).getNome());
        verify(estacionamentoRepository, times(1)).findAll();
    }

}

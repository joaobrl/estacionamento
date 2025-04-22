package com.estacionamento.api.service;

import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoCreateDto;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoUpdateDto;
import com.estacionamento.api.domain.exceptions.GenericException;
import com.estacionamento.api.domain.exceptions.RecursoNaoEncontradoException;
import com.estacionamento.api.domain.exceptions.VagaNaoDisponivelException;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.vaga.dto.VagaCreateDto;
import com.estacionamento.api.domain.vaga.dto.VagaDto;
import com.estacionamento.api.domain.vaga.dto.VagaListarDisponibilidadeDto;
import com.estacionamento.api.domain.vaga.dto.VagaUpdateDto;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import com.estacionamento.api.repository.EstacionamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstacionamentoService {

    private final EstacionamentoRepository estacionamentoRepository;

    @Transactional
    public Estacionamento cadastrarEstacionamento(EstacionamentoCreateDto dados)  {
            var estacionamento = new Estacionamento(dados);
            return estacionamentoRepository.save(estacionamento);
    }

    public Estacionamento adicionarVaga(Long estacionamentoId, VagaCreateDto vagaCreateDto) {
        Estacionamento estacionamento = estacionamentoRepository.findById(estacionamentoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estacionamento", estacionamentoId));

        estacionamento.adicionarVaga(vagaCreateDto);
        return estacionamentoRepository.save(estacionamento);
    }

    public List<Estacionamento> listarEstacionamentos() {
        return estacionamentoRepository.findAll();
    }

    public Estacionamento detalharEstacionamento(Long id) {
        return estacionamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estacionamento", id));
    }

    public List<Vaga> listarVagas(Long estacionamentoId, VagaListarDisponibilidadeDto filtro) {
        return estacionamentoRepository.findVagasByFilters(estacionamentoId,
                filtro.disponibilidade(),
                filtro.tipoVaga(),
                filtro.veiculoTipo());
    }

    @Transactional
    public Estacionamento atualizarEstacionamento(Long estacionamentoId, EstacionamentoUpdateDto dados) {
        var estacionamento = estacionamentoRepository.findById(estacionamentoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estacionamento", estacionamentoId));
        estacionamento.atualizar(dados);
        return estacionamentoRepository.save(estacionamento);
    }

    @Transactional
    public VagaDto atualizarVaga(Long estacionamentoId, VagaUpdateDto vagaUpdateDto) {
        var estacionamento = estacionamentoRepository.findById(estacionamentoId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estacionamento", estacionamentoId));

        var vaga = estacionamento.getVagas().stream()
                .filter(v -> v.getNumeroVaga().equals(vagaUpdateDto.numeroVaga()))
                .findFirst()
                .orElseThrow(() -> new RecursoNaoEncontradoException("Vaga", vagaUpdateDto.numeroVaga()));

        vaga.atualizar(vagaUpdateDto);

        estacionamentoRepository.save(estacionamento);

        return new VagaDto(vaga);
    }

    public Estacionamento findEstacionamentoById(Long id) {
        return estacionamentoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estacionamento", id));
    }

    public Estacionamento saveEstacionamento(Estacionamento estacionamento) {
        return estacionamentoRepository.save(estacionamento);
    }

    public Vaga verificarDisponibilidadeVaga(Estacionamento estacionamento, Veiculo veiculo) {
        return estacionamento.getVagas()
                .stream()
                .filter(Vaga::getDisponibilidade)
                .filter(vaga -> vaga.getVeiculoTipo().equals(veiculo.getVeiculoTipo()))
                .findFirst()
                .orElseThrow(VagaNaoDisponivelException::new);
    }

    public Vaga verificarDisponibilidadeVagaLivre(Long estacionamentoId, VeiculoTipo veiculoTipo) {

        List<Vaga> vagasDisponiveis = estacionamentoRepository.findVagaLivreDisponivelPorVeiculo(estacionamentoId, veiculoTipo);

        return vagasDisponiveis.stream()
                .findFirst()
                .orElseThrow(() -> new GenericException("Estacionamento com ID '" + estacionamentoId + "' está com capacidade máxima atingida.", HttpStatus.CONFLICT.value()));
    }
}

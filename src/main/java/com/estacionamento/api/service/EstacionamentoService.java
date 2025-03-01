package com.estacionamento.api.service;

import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoCreateDto;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoUpdateDto;
import com.estacionamento.api.domain.exceptions.RecursoNaoEncontradoException;
import com.estacionamento.api.domain.exceptions.VagaNaoDisponivelException;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.vaga.dto.VagaCreateDto;
import com.estacionamento.api.domain.vaga.dto.VagaDto;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import com.estacionamento.api.repository.EstacionamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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


    @Transactional
    public Estacionamento atualizarEstacionamento(EstacionamentoUpdateDto dados) {
        var estacionamento = estacionamentoRepository.findById(dados.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estacionamento", dados.id()));
        estacionamento.atualizar(dados);
        return estacionamentoRepository.save(estacionamento);
    }

    public List<VagaDto> listarVagasDisponiveis(Long id) {

        Estacionamento estacionamento = estacionamentoRepository.findEstacionamentoComVagasDisponiveis(id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estacionamento", id));

        return estacionamento.getVagas()
                .stream()
                .filter(Vaga::getDisponibilidade)
                .map(vaga -> new VagaDto(vaga.getNumeroVaga(), vaga.getVeiculoTipo(), vaga.getDisponibilidade()))
                .collect(Collectors.toList());
    }

    public List<VagaDto> listarVagasOcupadas(Long id) {
        Estacionamento estacionamento = estacionamentoRepository.findEstacionamentoComVagasOcupadas(id)
                .stream()
                .findFirst()
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estacionamento", id));

        return estacionamento.getVagas().stream()
                .filter(vaga -> !vaga.getDisponibilidade())
                .map(vaga -> new VagaDto(vaga.getNumeroVaga(), vaga.getVeiculoTipo(), vaga.getDisponibilidade()))
                .collect(Collectors.toList());
    }

    public List<VagaDto> listarVagasDisponiveisPorVeiculo(Long id, String veiculoTipo) {
        VeiculoTipo tipoVeiculoEnum = VeiculoTipo.valueOf(veiculoTipo.toUpperCase());

        List<Estacionamento> estacionamentos = estacionamentoRepository.findEstacionamentoComVagasDisponiveisPorTipoVeiculo(id, tipoVeiculoEnum);

        Estacionamento estacionamento = estacionamentos
                .stream()
                .findFirst()
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estacionamento", id));

        return estacionamento.getVagas().stream()
                .filter(vaga -> vaga.getDisponibilidade() && vaga.getVeiculoTipo() == tipoVeiculoEnum)
                .map(vaga -> new VagaDto(vaga.getNumeroVaga(), vaga.getVeiculoTipo(), vaga.getDisponibilidade()))
                .collect(Collectors.toList());
    }

    public List<VagaDto> listarVagasOcupadasPorVeiculo(Long id, String veiculoTipo) {
        VeiculoTipo tipoVeiculoEnum = VeiculoTipo.valueOf(veiculoTipo.toUpperCase());

        List<Estacionamento> estacionamentos = estacionamentoRepository.findEstacionamentoComVagasOcupadasPorTipoVeiculo(id, tipoVeiculoEnum);

        Estacionamento estacionamento = estacionamentos
                .stream()
                .findFirst()
                .orElseThrow(() -> new RecursoNaoEncontradoException("Estacionamento", id));

        return estacionamento.getVagas().stream()
                .filter(vaga -> !vaga.getDisponibilidade() && vaga.getVeiculoTipo() == tipoVeiculoEnum)
                .map(vaga -> new VagaDto(vaga.getNumeroVaga(), vaga.getVeiculoTipo(), vaga.getDisponibilidade()))
                .collect(Collectors.toList());
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
}

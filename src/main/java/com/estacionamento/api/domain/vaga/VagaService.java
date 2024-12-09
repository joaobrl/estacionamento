package com.estacionamento.api.domain.vaga;

import com.estacionamento.api.domain.estacionamento.EstacionamentoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VagaService {
    @Autowired
    private VagaRepository vagaRepository;
    @Autowired
    private EstacionamentoRepository estacionamentoRepository;
    @Transactional
    public Vaga criarVaga(VagaCreateDto vagaCreateDto) {

        // Verificar se o estacionamento existe
        var estacionamento = estacionamentoRepository.findById(vagaCreateDto.estacionamentoId())
                .orElseThrow(() -> new RuntimeException("Estacionamento não encontrado"));

        // Verificar se já existe uma vaga com o número informado
        boolean numeroVagaExistente = vagaRepository.existsByEstacionamentoIdAndNumero(
                estacionamento.getId(), vagaCreateDto.numero());
        if (numeroVagaExistente) {
            throw new RuntimeException("Já existe uma vaga com este número neste estacionamento");
        }

        // Verificar capacidade
        long vagasAtuais = vagaRepository.countByEstacionamentoId(estacionamento.getId());
        if (vagasAtuais >= estacionamento.getCapacidade()) {
            throw new RuntimeException("Estacionamento lotado");
        }

        Vaga novaVaga = new Vaga(vagaCreateDto, estacionamento);
        return vagaRepository.save(novaVaga);

    }

    public Vaga atualizarVaga (VagaUpdateDto vagaUpdateDto) {
        var vaga = vagaRepository.findById(vagaUpdateDto.id())
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));
        if (vagaUpdateDto.disponibilidade()) {
            vaga.setDisponibilidade(true);
        } else {
            vaga.setDisponibilidade(false);
        }
        return vagaRepository.save(vaga);
    }
}

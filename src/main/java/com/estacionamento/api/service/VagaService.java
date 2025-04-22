package com.estacionamento.api.service;

import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.historico.dto.EntradaClienteDto;
import com.estacionamento.api.repository.EstacionamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VagaService {

    @Autowired
    private EstacionamentoRepository estacionamentoRepository;

    public void liberarVaga(Estacionamento estacionamento, String numeroVaga) {

        estacionamento.getVagas()
                .stream()
                .filter(v -> v.getNumeroVaga().equals(numeroVaga))
                .findFirst()
                .ifPresent(v -> v.liberarVaga(numeroVaga));
        estacionamentoRepository.save(estacionamento);
    }

    public void ocuparVaga(EntradaClienteDto entradaClienteDto, String numeroVaga) {
        var estacionamento = estacionamentoRepository.findById(entradaClienteDto.estacionamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Estacionamento não encontrado para o ID: " + entradaClienteDto.estacionamentoId()));

        estacionamento.getVagas()
                .stream()
                .filter(v -> v.getVeiculoTipo().equals(entradaClienteDto.veiculo().getVeiculoTipo()) && v.getDisponibilidade())
                .findFirst()
                .ifPresentOrElse(
                        v -> {
                            v.ocuparVaga(numeroVaga);
                        },
                        () -> {
                            throw new IllegalStateException("Nenhuma vaga disponível para o tipo de veículo: " + entradaClienteDto.veiculo().getVeiculoTipo());
                        }
                );

        estacionamentoRepository.save(estacionamento);
    }
}
package com.estacionamento.api.domain.estacionamento;

import com.estacionamento.api.infra.exceptions.GlobalExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EstacionamentoService extends GlobalExceptionHandler {

    @Autowired
    private EstacionamentoRepository estacionamentoRepository;

    @Transactional
    public Estacionamento cadastrarEstacionamento(EstacionamentoCreateDto dados) {
        try {
            var estacionamento = new Estacionamento(dados);
            return estacionamentoRepository.save(estacionamento);
        } catch (Exception e) {
            throw new ErroAoCadastrarEstacionamentoException("Erro ao cadastrar estacionamento", e);
        }
    }

    public ResponseEntity listarEstacionamentos() {
        var estacionamentos = estacionamentoRepository.findAll();
        if (estacionamentos.isEmpty()) {
            throw new EstacionamentoNaoEncontradoException("Nenhum estacionamento encontrado.");
        }
        return ResponseEntity.ok(estacionamentos);
    }

    public Estacionamento detalharEstacionamento(Long id) {
        return estacionamentoRepository.findById(id)
                .orElseThrow(() -> new EstacionamentoNaoEncontradoException("Estacionamento não encontrado com id: " + id));
    }

    @Transactional
    public Estacionamento atualizarEstacionamento(EstacionamentoUpdateDto dados) {
        Estacionamento estacionamento = estacionamentoRepository.findById(dados.id())
                .orElseThrow(() -> new EstacionamentoNaoEncontradoException("Estacionamento não encontrado com id: " + dados.id()));
        estacionamento.atualizar(dados);
        return estacionamentoRepository.save(estacionamento);
    }
}

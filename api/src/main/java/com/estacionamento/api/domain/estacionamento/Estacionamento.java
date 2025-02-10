package com.estacionamento.api.domain.estacionamento;

import com.estacionamento.api.domain.endereco.Endereco;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoCreateDto;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoUpdateDto;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.vaga.dto.VagaDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "estacionamentos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Estacionamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;
    @Column(name = "nome", unique = true, nullable = false)
    private String nome;
    @Embedded
    private Endereco endereco;
    @Column(name = "capacidade", nullable = false)
    private Integer capacidade;
    @ElementCollection
    @CollectionTable(name = "vagas", joinColumns = @JoinColumn(name = "estacionamento_id"))
    private List<Vaga> vagas = new ArrayList<>();

    public Estacionamento(EstacionamentoCreateDto dados) {
        this.nome = dados.nome();
        this.endereco = new Endereco(dados.endereco());
        this.capacidade = dados.capacidade();
        this.vagas = new ArrayList<>();
    }

    public void atualizar(EstacionamentoUpdateDto dados) {
        if (dados.nome() != null) {
            this.nome = dados.nome();
        }
        if (dados.endereco() != null) {
            this.endereco.atualizar(dados.endereco());
        }
        if (dados.capacidade() != null) {
            this.capacidade = dados.capacidade();
        }
    }

    public void adicionarVaga(VagaDto vagaDto) {
        boolean vagaExistente = vagas.stream()
                .anyMatch(v -> v.getNumeroVaga().equals(vagaDto.numeroVaga()));
        if (vagaExistente) {
            throw new IllegalArgumentException("Número da vaga já existe.");
        }

        if (vagas.size() >= capacidade) {
            throw new IllegalStateException("Capacidade máxima do estacionamento atingida.");
        }

        Vaga novaVaga = new Vaga(vagaDto.numeroVaga(), vagaDto.veiculoTipo(), vagaDto.disponibilidade());
        this.vagas.add(novaVaga);
    }

    public VagaDto buscarVagaPorNumero(String numero) {
        Vaga vaga = vagas.stream()
                .filter(v -> v.getNumeroVaga().equals(numero))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Vaga não encontrada."));

        return new VagaDto(vaga.getNumeroVaga(), vaga.getVeiculoTipo(), vaga.getDisponibilidade());
    }

    public List<VagaDto> listarVagas() {
        return vagas.stream()
                .map(vaga -> new VagaDto(vaga.getNumeroVaga(), vaga.getVeiculoTipo(), vaga.getDisponibilidade()))
                .collect(Collectors.toList());
    }

}

package com.estacionamento.api.domain.estacionamento;

import com.estacionamento.api.domain.endereco.Endereco;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoCreateDto;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoUpdateDto;
import com.estacionamento.api.domain.exceptions.GenericException;
import com.estacionamento.api.domain.exceptions.VagaComNumeroJaExistenteException;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.vaga.dto.VagaCreateDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;

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
    @AttributeOverrides({
            @AttributeOverride(name = "numeroVaga", column = @Column(name = "numero_vaga")),
            @AttributeOverride(name = "veiculoTipo", column = @Column(name = "veiculo_tipo")),
            @AttributeOverride(name = "disponibilidade", column = @Column(name = "disponibilidade")),
            @AttributeOverride(name = "tipoVaga", column = @Column(name = "tipo_vaga"))
    })
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

    public void adicionarVaga(VagaCreateDto vagaCreateDto) {
        boolean vagaExistente = vagas.stream()
                .anyMatch(v -> v.getNumeroVaga().equals(vagaCreateDto.numeroVaga()));
        if (vagaExistente) {
            throw new VagaComNumeroJaExistenteException(vagaCreateDto.numeroVaga());
        }

        if (vagas.size() >= capacidade) {
            throw new GenericException("Estacionamento com ID '" + id + "' está com capacidade máxima atingida.", HttpStatus.CONFLICT.value());
        }

        Vaga novaVaga = new Vaga(vagaCreateDto);
        this.vagas.add(novaVaga);
    }

}

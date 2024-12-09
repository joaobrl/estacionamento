package com.estacionamento.api.domain.estacionamento;

import com.estacionamento.api.domain.endereco.Endereco;
import com.estacionamento.api.domain.vaga.Vaga;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "estacionamentos")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
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

    @OneToMany(mappedBy = "estacionamento", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Vaga> vagas;

    public Estacionamento(EstacionamentoCreateDto dados) {
        this.nome = dados.nome();
        this.endereco = new Endereco(dados.endereco());
        this.capacidade = dados.capacidade();
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
}

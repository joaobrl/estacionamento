package com.estacionamento.api.domain.vaga;

import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.pagamento.PagamentoRepository;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vagas")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "estacionamento_id", nullable = false)
    @JsonBackReference
    private Estacionamento estacionamento;
    @Column(nullable = false)
    private String numero;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VeiculoTipo veiculoTipo;

    @Column(nullable = false)
    private Boolean disponibilidade;


    public Vaga(VagaCreateDto dados, Estacionamento estacionamento) {
        this.numero = dados.numero();
        this.veiculoTipo = dados.veiculoTipo();
        this.disponibilidade = true;
        this.estacionamento = estacionamento;
    }

    public void ocuparVaga() {
        if (!this.disponibilidade) {
            throw new IllegalStateException("A vaga já está ocupada.");
        }
        this.disponibilidade = false;
    }

    public void liberarVaga() {
        if (this.disponibilidade) {
            throw new IllegalStateException("A vaga já está livre.");
        }
        this.disponibilidade = true;
    }
}

package com.estacionamento.api.domain.vaga;

import com.estacionamento.api.domain.veiculo.VeiculoTipo;
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

    private String numero;

    @Enumerated(EnumType.STRING)
    private VeiculoTipo veiculoTipo;

    private Boolean disponibilidade;


    public Vaga(VagaCreateDto dados) {
        this.numero = dados.numero();
        this.veiculoTipo = dados.veiculoTipo();
        this.disponibilidade = true;
    }

    public void atualizar(VagaDto dados) {
        this.disponibilidade = false;
    }
}

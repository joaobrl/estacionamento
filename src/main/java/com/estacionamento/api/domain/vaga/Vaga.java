package com.estacionamento.api.domain.vaga;

import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Vaga {

    private String numeroVaga;
    private VeiculoTipo veiculoTipo;
    private Boolean disponibilidade;

    public Vaga(String numeroVaga, VeiculoTipo veiculoTipo) {
        this.numeroVaga = numeroVaga;
        this.veiculoTipo = veiculoTipo;
        this.disponibilidade = true;
    }

    public void ocuparVaga() {
        if (!this.disponibilidade) {
            throw new IllegalStateException("A vaga já está ocupada.");
        }
        this.disponibilidade = false;
    }

    public void liberarVaga(String numeroVaga) {
        if (this.disponibilidade) {
            throw new IllegalStateException("A vaga já está livre.");
        }
        this.disponibilidade = true;
    }
}

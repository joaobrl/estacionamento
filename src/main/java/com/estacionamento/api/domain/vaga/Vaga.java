package com.estacionamento.api.domain.vaga;

import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoUpdateDto;
import com.estacionamento.api.domain.vaga.dto.VagaCreateDto;
import com.estacionamento.api.domain.vaga.dto.VagaUpdateDto;
import com.estacionamento.api.domain.veiculo.Veiculo;
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
    private TipoVaga tipoVaga;

    public Vaga(VagaCreateDto vagaCreateDto) {
        this.numeroVaga = vagaCreateDto.numeroVaga();
        this.veiculoTipo = vagaCreateDto.veiculoTipo();
        this.tipoVaga = vagaCreateDto.tipoVaga();
        this.disponibilidade = true;
    }

    public void atualizar(VagaUpdateDto vagaUpdateDto) {
        if(vagaUpdateDto.veiculoTipo() != null) {
            this.veiculoTipo = vagaUpdateDto.veiculoTipo();
        }
        if(vagaUpdateDto.tipoVaga() != null) {
            this.tipoVaga = vagaUpdateDto.tipoVaga();
        }
        if(vagaUpdateDto.disponibilidade() != null) {
            this.disponibilidade = vagaUpdateDto.disponibilidade();
        }
    }

    public void ocuparVaga(String numeroVaga) {
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

package com.estacionamento.api.domain.historico;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.veiculo.Veiculo;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class MovimentacaoClientePlano {

    @ManyToOne
    @JoinColumn(name = "cliente_matricula", referencedColumnName = "matricula", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "estacionamento_id", nullable = false)
    private Estacionamento estacionamento;

    @Embedded
    private Veiculo veiculo;

    private String numeroVaga;

    private LocalDateTime horaEntrada;

    private LocalDateTime horaSaida;

    public MovimentacaoClientePlano(Cliente cliente, Estacionamento estacionamento, String numeroVaga, Veiculo veiculo) {
        this.cliente = cliente;
        this.estacionamento = estacionamento;
        this.numeroVaga = numeroVaga;
        this.veiculo = veiculo;
        this.horaEntrada = LocalDateTime.now();
    }

}

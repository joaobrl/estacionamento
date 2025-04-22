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

    private Long estacionamentoId;

    @Embedded
    private Veiculo veiculo;

    private String numeroVaga;

    private LocalDateTime horaEntrada;

    private LocalDateTime horaSaida;

}

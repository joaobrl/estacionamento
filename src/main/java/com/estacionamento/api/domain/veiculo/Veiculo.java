package com.estacionamento.api.domain.veiculo;

import com.estacionamento.api.domain.veiculo.dto.VeiculoCreateDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Veiculo {

    private String placa;
    @Enumerated(EnumType.STRING)
    private VeiculoTipo veiculoTipo;
    private String modelo;
    private String marca;

    public Veiculo(VeiculoCreateDto veiculoCreateDto) {
        this.placa = veiculoCreateDto.placa();
        this.veiculoTipo = veiculoCreateDto.veiculoTipo();
        this.modelo = veiculoCreateDto.modelo();
        this.marca = veiculoCreateDto.marca();
    }
}

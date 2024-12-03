package com.estacionamento.api.domain.veiculo;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "veiculos")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of ="id")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private VeiculoTipo veiculoTipo;

    private String placa;

    private String modelo;

    private String marca;

    private Boolean planoMensal;

    public Veiculo(VeiculoCreateDto veiculoCreateDto) {
        this.veiculoTipo = veiculoCreateDto.veiculoTipo();
        this.placa = veiculoCreateDto.placa();
        this.modelo = veiculoCreateDto.modelo();
        this.marca = veiculoCreateDto.marca();
        this.planoMensal = veiculoCreateDto.planoMensal();
    }
}

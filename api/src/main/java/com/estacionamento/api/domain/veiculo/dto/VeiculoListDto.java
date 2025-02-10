package com.estacionamento.api.domain.veiculo.dto;

import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;

public record VeiculoListDto(
        VeiculoTipo veiculoTipo,
        String placa,
        String modelo,
        String marca)
{
    public VeiculoListDto(Veiculo veiculo) {
        this(
                veiculo.getVeiculoTipo(),
                veiculo.getPlaca(),
                veiculo.getModelo(),
                veiculo.getMarca());
    }
}

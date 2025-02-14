package com.estacionamento.api.domain.veiculo.dto;

import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;

public record VeiculoListDto(
        String placa,
        VeiculoTipo veiculoTipo,
        String modelo,
        String marca)
{
    public VeiculoListDto(Veiculo veiculo) {
        this(
                veiculo.getPlaca(),
                veiculo.getVeiculoTipo(),
                veiculo.getModelo(),
                veiculo.getMarca());
    }
}

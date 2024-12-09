package com.estacionamento.api.domain.veiculo;

public record VeiculoListDto(
        Long id,
        VeiculoTipo veiculoTipo,
        String placa,
        String modelo,
        String marca,
        Boolean planoMensal)
{
    public VeiculoListDto(Veiculo veiculo) {
        this(
                veiculo.getId(),
                veiculo.getVeiculoTipo(),
                veiculo.getPlaca(),
                veiculo.getModelo(),
                veiculo.getMarca(),
                veiculo.getPlanoMensal());
    }
}

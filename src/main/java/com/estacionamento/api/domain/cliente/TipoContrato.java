package com.estacionamento.api.domain.cliente;

public enum TipoContrato {
    MENSAL(1, 0.0),
    TRIMESTRAL(3, 0.05), // 5% de desconto
    ANUAL(12, 0.10); // 10% de desconto

    private final int meses;
    private final double desconto;

    TipoContrato(int meses, double desconto) {
        this.meses = meses;
        this.desconto = desconto;
    }

    public int getMeses() {
        return meses;
    }

    public double getDesconto() {
        return desconto;
    }
}
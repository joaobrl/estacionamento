package com.estacionamento.api.domain.cliente;

public enum TipoPlano {
    BASIC(350.00, "Plano Básico para Carros/motos que pode ser usado apenas das 8h as 22h de segunda a sexta"),
    PREMIUM(500.00, "Plano Premium para Carros/motos que pode ser usado 24h por dia, 7 dias por semana"),
    ESPECIAL(1000.00, "Plano Especial para Carros, motos e Caminhões que pode ser usado 24h por dia, 7 dias por semana"),;

    private final double valorMensal;
    private final String descricao;

    TipoPlano(double valorMensal, String descricao) {
        this.valorMensal = valorMensal;
        this.descricao = descricao;
    }

    public double getValorMensal() {
        return valorMensal;
    }

    public String getDescricao() {
        return descricao;
    }

    public double calcularValorComDesconto(TipoContrato tipoContrato) {
        double valorTotal = valorMensal * tipoContrato.getMeses();
        double desconto = valorTotal * tipoContrato.getDesconto();
        return valorTotal - desconto;
    }

    public static double calcularMigracaoDePlano(TipoPlano tipoPlanoAtual, TipoPlano tipoPlanoNovo, long diasRestantes) {
        double valorPlanoAtual = tipoPlanoAtual.getValorMensal();
        double valorPlanoNovo = tipoPlanoNovo.getValorMensal();

        double valorProporcional = (valorPlanoAtual / 30) * diasRestantes;

        return (valorPlanoNovo - valorProporcional) + valorPlanoNovo;
    }
}




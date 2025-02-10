package com.estacionamento.api.domain.cliente;

public enum TipoPlano {
    // Plano básico só pode utilizar o estacionamento das 8h as 20h de segunda a sexta
    BASIC(350.00, "Plano Básico para Carros/motos"),
    // Plano premium, pode utilizar o estacionamento 24h por dia, 7 dias por semana
    PREMIUM(500.00, "Plano Premium para Carros/motos"),
    // Plano especial para caminhões, pode utilizar o estacionamento 24h por dia, 7 dias por semana
    CAMINHAO_ESPECIAL(1000.00, "Plano Especial para Caminhões");

    private final double valorPlano;
    private final String descricao;

    TipoPlano(double valorPlano, String descricao) {
        this.valorPlano = valorPlano;
        this.descricao = descricao;
    }

    public double getValorPlano() {
        return valorPlano;
    }

    public String getDescricao() {
        return descricao;
    }

    public static double calcularMigracaoDePlano(TipoPlano tipoPlanoAtual, TipoPlano tipoPlanoNovo, long diasRestantes) {
        if (tipoPlanoAtual == TipoPlano.BASIC && tipoPlanoNovo == TipoPlano.PREMIUM) {
            double valorPlanoBasic = TipoPlano.BASIC.getValorPlano();
            double valorPlanoPremium = TipoPlano.PREMIUM.getValorPlano();

            double valorProporcional = (valorPlanoBasic / 30) * diasRestantes;

            return (valorPlanoPremium - valorProporcional) + TipoPlano.PREMIUM.getValorPlano();
        }
        if (tipoPlanoAtual == TipoPlano.PREMIUM && tipoPlanoNovo == TipoPlano.CAMINHAO_ESPECIAL) {
            double valorPlanoPremium = TipoPlano.PREMIUM.getValorPlano();
            double valorPlanoEspecial = TipoPlano.CAMINHAO_ESPECIAL.getValorPlano();

            double valorProporcional = (valorPlanoPremium / 30) * diasRestantes;

            return (valorPlanoEspecial - valorProporcional) + TipoPlano.CAMINHAO_ESPECIAL.getValorPlano();
        }

        return 0;
    }
}




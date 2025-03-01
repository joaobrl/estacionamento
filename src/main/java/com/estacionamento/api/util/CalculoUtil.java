package com.estacionamento.api.util;

import com.estacionamento.api.domain.cliente.TipoPlano;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CalculoUtil {

    public static double calcularValorMensalidade(TipoPlano tipoPlanoAtual, TipoPlano novoTipoPlano, LocalDateTime validadePlanoMensal) {
        if (tipoPlanoAtual == null || tipoPlanoAtual == novoTipoPlano) {
            return novoTipoPlano.getValorPlano();
        } else {
            long diasRestantes = ChronoUnit.DAYS.between(LocalDateTime.now(), validadePlanoMensal);
            return TipoPlano.calcularMigracaoDePlano(tipoPlanoAtual, novoTipoPlano, diasRestantes);
        }
    }
}
package com.estacionamento.api.util;

import com.estacionamento.api.domain.cliente.TipoContrato;
import com.estacionamento.api.domain.cliente.TipoPlano;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Util {

    public static double calcularValorMensalidade(TipoPlano tipoPlanoAtual, TipoPlano novoTipoPlano, LocalDateTime validadePlanoMensal, TipoContrato tipoContrato) {
        if (tipoPlanoAtual == null || tipoPlanoAtual == novoTipoPlano) {
            return novoTipoPlano.calcularValorComDesconto(tipoContrato);
        } else {
            long diasRestantes = ChronoUnit.DAYS.between(LocalDateTime.now(), validadePlanoMensal);
            double valorMigracao = TipoPlano.calcularMigracaoDePlano(tipoPlanoAtual, novoTipoPlano, diasRestantes);
            return valorMigracao * (1 - tipoContrato.getDesconto());
        }
    }

    public static String gerarMatricula(String cpf, Long id) {
        String ultimosCpf = cpf.replaceAll("\\D", ""); // remove qualquer caractere não numérico
        if (ultimosCpf.length() < 3) {
            throw new IllegalArgumentException("CPF inválido para gerar matrícula");
        }

        String ultimos3Cpf = ultimosCpf.substring(ultimosCpf.length() - 3);
        String idFormatado = String.format("%03d", id); // garante 3 dígitos

        return ultimos3Cpf + idFormatado.substring(idFormatado.length() - 3);
    }
}
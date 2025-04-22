package com.estacionamento.api.util;

import com.estacionamento.api.domain.cliente.Cliente;
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

    public static String gerarMatricula(Cliente cliente) {
        if (cliente.getCpf() == null || cliente.getId() == null) {
            throw new IllegalArgumentException("CPF e ID não podem ser nulos");
        }

        String numerosCpf = cliente.getCpf().replaceAll("\\D", "");

        if (numerosCpf.length() < 3) {
            throw new IllegalArgumentException("CPF inválido para gerar matrícula");
        }

        String ultimos3Cpf = numerosCpf.substring(numerosCpf.length() - 3);
        String idFormatado = String.format("%03d", cliente.getId());

        return ultimos3Cpf + idFormatado;
    }



}
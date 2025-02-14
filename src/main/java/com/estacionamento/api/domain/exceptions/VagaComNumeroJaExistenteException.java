package com.estacionamento.api.domain.exceptions;

public class VagaComNumeroJaExistenteException extends RuntimeException{
    public VagaComNumeroJaExistenteException(String numero) {
        super("Já existe uma vaga com o número '" + numero + "' neste estacionamento");
    }
}

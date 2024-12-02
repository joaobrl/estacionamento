package com.estacionamento.api.domain.endereco;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class Endereco {
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;

    public Endereco(EnderecoDto endereco) {
        this.rua = endereco.rua();
        this.numero = endereco.numero();
        this.bairro = endereco.bairro();
        this.cidade = endereco.cidade();
        this.estado = endereco.estado();
        this.cep = endereco.cep();
    }

    public void atualizar(EnderecoDto endereco) {
        if (endereco.rua() != null) {
            this.rua = endereco.rua();
        }
        if (endereco.numero() != null) {
            this.numero = endereco.numero();
        }
        if (endereco.bairro() != null) {
            this.bairro = endereco.bairro();
        }
        if (endereco.cidade() != null) {
            this.cidade = endereco.cidade();
        }
        if (endereco.estado() != null) {
            this.estado = endereco.estado();
        }
        if (endereco.cep() != null) {
            this.cep = endereco.cep();
        }
    }
}

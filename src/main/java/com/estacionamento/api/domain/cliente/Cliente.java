package com.estacionamento.api.domain.cliente;

import com.estacionamento.api.domain.cliente.dto.ClienteCreateDto;
import com.estacionamento.api.domain.cliente.dto.ClienteUpdateDto;
import com.estacionamento.api.domain.historico.MovimentacaoClientePlano;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.veiculo.dto.VeiculoCreateDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.estacionamento.api.util.Util;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Cliente {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(unique = true)
    private String cpf;

    @Column(unique = true, nullable = false)
    private String matricula;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String telefone;

    @Enumerated(EnumType.STRING)
    private TipoPlano tipoPlano;

    private Boolean planoMensalAtivo = false;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime validadePlanoMensal;

    @ElementCollection
    @CollectionTable(name = "movimentacaoClientePlano", joinColumns = @JoinColumn(name = "movimentacaoClientePlano_matricula"))
    private List<MovimentacaoClientePlano> movimentacaoClientePlano = new ArrayList<>();

    public Cliente(ClienteCreateDto clienteCreateDto) {
        this.nome = clienteCreateDto.nome();
        this.cpf = clienteCreateDto.cpf();
        this.email = clienteCreateDto.email();
        this.telefone = clienteCreateDto.telefone();
        this.matricula = Util.gerarMatricula(clienteCreateDto.cpf(), this.id);
        this.movimentacaoClientePlano = new ArrayList<>();
    }

    // Método para atualizar informações do cliente
    public void atualizar(ClienteUpdateDto clienteUpdateDto) {
        if (clienteUpdateDto.nome() != null) {
            this.nome = clienteUpdateDto.nome();
        }
        if (clienteUpdateDto.email() != null) {
            this.email = clienteUpdateDto.email();
        }
        if (clienteUpdateDto.telefone() != null) {
            this.telefone = clienteUpdateDto.telefone();
        }
        if (clienteUpdateDto.planoMensalAtivo() != null) {
            this.planoMensalAtivo = clienteUpdateDto.planoMensalAtivo();
        }
        if (clienteUpdateDto.tipoPlano() != null) {
            this.tipoPlano = clienteUpdateDto.tipoPlano();
        }
    }
}

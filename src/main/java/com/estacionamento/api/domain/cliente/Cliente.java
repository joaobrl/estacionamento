package com.estacionamento.api.domain.cliente;

import com.estacionamento.api.domain.cliente.dto.ClienteCreateDto;
import com.estacionamento.api.domain.cliente.dto.ClienteUpdateDto;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.veiculo.dto.VeiculoCreateDto;
import com.fasterxml.jackson.annotation.JsonFormat;
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

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String telefone;

    @ElementCollection
    @CollectionTable(name = "veiculos", joinColumns = @JoinColumn(name = "cliente_id"))
    private List<Veiculo> veiculos = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private TipoPlano tipoPlano;

    private Boolean planoMensalAtivo = false;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime validadePlanoMensal;

    private Boolean renovacaoAutomaticaPlano;

    public Cliente(ClienteCreateDto clienteCreateDto) {
        this.nome = clienteCreateDto.nome();
        this.cpf = clienteCreateDto.cpf();
        this.email = clienteCreateDto.email();
        this.telefone = clienteCreateDto.telefone();
    }

    // Método para adicionar veículo
    public void adicionarVeiculo (VeiculoCreateDto veiculoCreateDto) {
        boolean veiculoExistente = veiculos.stream()
                .anyMatch(v -> v.getPlaca().equals(veiculoCreateDto.placa()));
        if (veiculoExistente) {
            throw new IllegalArgumentException("Veiculo já cadastrado");
        }

        if (veiculos.size() >= 3) {
            throw new IllegalStateException("Capacidade máxima de veículos atingida");
        }

        Veiculo novoVeiculo = new Veiculo(veiculoCreateDto);
        this.veiculos.add(novoVeiculo);
    }

    public void removerVeiculo(String placa) {
        veiculos.removeIf(veiculo -> veiculo.getPlaca().equals(placa));
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

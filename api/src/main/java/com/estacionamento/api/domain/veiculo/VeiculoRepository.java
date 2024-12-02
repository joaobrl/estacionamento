package com.estacionamento.api.domain.veiculo;

import com.estacionamento.api.domain.veiculo.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
}

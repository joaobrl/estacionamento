package com.estacionamento.api.domain.vaga;

import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VagaRepository extends JpaRepository<Vaga, Long> {
}

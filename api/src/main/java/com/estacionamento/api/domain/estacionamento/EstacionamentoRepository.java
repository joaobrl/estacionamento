package com.estacionamento.api.domain.estacionamento;

import com.estacionamento.api.domain.estacionamento.Estacionamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstacionamentoRepository extends JpaRepository<Estacionamento, Long> {
}

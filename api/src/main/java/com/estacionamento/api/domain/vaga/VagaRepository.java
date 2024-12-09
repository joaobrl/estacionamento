package com.estacionamento.api.domain.vaga;

import org.springframework.data.jpa.repository.JpaRepository;

public interface VagaRepository extends JpaRepository<Vaga, Long> {
    long countByEstacionamentoId(Long estacionamentoId);

    boolean existsByEstacionamentoIdAndNumero(Long estacionamentoId, String numero);
}

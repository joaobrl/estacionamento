package com.estacionamento.api.repository;

import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.vaga.TipoVaga;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EstacionamentoRepository extends JpaRepository<Estacionamento, Long> {

    @Query("SELECT v FROM Estacionamento e JOIN e.vagas v WHERE e.id = :estacionamentoId " +
            "AND (:disponibilidade IS NULL OR v.disponibilidade = :disponibilidade) " +
            "AND (:tipoVaga IS NULL OR v.tipoVaga = :tipoVaga) " +
            "AND (:veiculoTipo IS NULL OR v.veiculoTipo = :veiculoTipo)")
    List<Vaga> findVagasByFilters(@Param("estacionamentoId") Long estacionamentoId,
                                  @Param("disponibilidade") Boolean disponibilidade,
                                  @Param("tipoVaga") TipoVaga tipoVaga,
                                  @Param("veiculoTipo") VeiculoTipo veiculoTipo);
   }

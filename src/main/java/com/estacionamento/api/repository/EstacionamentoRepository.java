package com.estacionamento.api.repository;

import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EstacionamentoRepository extends JpaRepository<Estacionamento, Long> {
    @Query("SELECT e FROM Estacionamento e " +
            "JOIN e.vagas v " +
            "WHERE e.id = :id " +
            "AND v.disponibilidade = true")
    List<Estacionamento> findEstacionamentoComVagasDisponiveis(@Param("id") Long id);

    @Query("SELECT e FROM Estacionamento e " +
            "JOIN e.vagas v " +
            "WHERE e.id = :id " +
            "AND v.veiculoTipo = :veiculoTipo " +
            "AND v.disponibilidade = true")
    List<Estacionamento> findEstacionamentoComVagasDisponiveisPorTipoVeiculo(@Param("id") Long id, @Param("veiculoTipo") VeiculoTipo veiculoTipo);

    @Query("SELECT e FROM Estacionamento e " +
            "JOIN e.vagas v " +
            "WHERE e.id = :id " +
            "AND v.disponibilidade = false")
    List<Estacionamento> findEstacionamentoComVagasOcupadas(@Param("id") Long id);

    @Query("SELECT e FROM Estacionamento e " +
            "JOIN e.vagas v " +
            "WHERE e.id = :id " +
            "AND v.veiculoTipo = :veiculoTipo " +
            "AND v.disponibilidade = false")
    List<Estacionamento> findEstacionamentoComVagasOcupadasPorTipoVeiculo(@Param ("id") Long id, @Param("veiculoTipo") VeiculoTipo veiculoTipo);
}

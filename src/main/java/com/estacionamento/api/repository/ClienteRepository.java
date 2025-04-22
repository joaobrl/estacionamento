package com.estacionamento.api.repository;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.historico.MovimentacaoClientePlano;
import com.estacionamento.api.domain.veiculo.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    List<Cliente> findAllByPlanoMensalAtivoTrue();

    Optional<Cliente> findByCpf(String cpf);

    @Query("SELECT c FROM Cliente c LEFT JOIN FETCH c.movimentacaoClientePlano WHERE c.matricula = :matricula")
    Optional<Cliente> findByMatricula(@Param("matricula") String matricula);

    @Query("SELECT m FROM Cliente c JOIN c.movimentacaoClientePlano m WHERE c.matricula = :matricula AND m.horaSaida IS NULL")
    Optional<MovimentacaoClientePlano> findMovimentacaoAtivaByMatricula(@Param("matricula") String matricula);
}

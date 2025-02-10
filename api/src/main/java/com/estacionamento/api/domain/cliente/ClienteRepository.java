package com.estacionamento.api.domain.cliente;

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

    @Query("SELECT v FROM Cliente c JOIN c.veiculos v")
    List<Veiculo> findAllVeiculos();

    @Query("SELECT c FROM Cliente c JOIN c.veiculos v WHERE v.placa = :placa")
    Optional<Cliente> findClienteByVeiculoPlaca(@Param("placa") String placa);

    @Query("SELECT c FROM Cliente c " +
            "WHERE c.planoMensalAtivo = true " +
            "AND c.renovacaoAutomaticaPlano = true " +
            "AND c.validadePlanoMensal < :dataAtual")
    List<Cliente> findClientesComRenovacaoAutomaticaEPlanoExpirado(@Param("dataAtual") LocalDateTime dataAtual);


}

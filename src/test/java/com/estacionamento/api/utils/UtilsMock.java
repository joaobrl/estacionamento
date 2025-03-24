package com.estacionamento.api.utils;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.cliente.dto.ClienteCreateDto;
import com.estacionamento.api.domain.cliente.dto.ClienteUpdateDto;
import com.estacionamento.api.domain.endereco.Endereco;
import com.estacionamento.api.domain.endereco.EnderecoDto;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoCreateDto;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoUpdateDto;
import com.estacionamento.api.domain.pagamento.dto.PagamentoPlanoMensalDto;
import com.estacionamento.api.domain.pagamento.dto.PagamentoTicketDto;
import com.estacionamento.api.domain.ticket.Ticket;
import com.estacionamento.api.domain.ticket.dto.TicketCreateDto;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.vaga.dto.VagaCreateDto;
import com.estacionamento.api.domain.veiculo.Veiculo;
import com.estacionamento.api.domain.veiculo.dto.VeiculoCreateDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.estacionamento.api.domain.cliente.TipoPlano.BASIC;
import static com.estacionamento.api.domain.cliente.TipoPlano.PREMIUM;
import static com.estacionamento.api.domain.pagamento.MetodoPagamento.DINHEIRO;
import static com.estacionamento.api.domain.veiculo.VeiculoTipo.*;

public class UtilsMock {

    // CLIENTES
    public static ClienteCreateDto criarClienteCreateDto() {
        return new ClienteCreateDto(
                "João",
                "12345678901",
                "joao@email.com",
                "11987654321");
    }

    public static VeiculoCreateDto criarVeiculoCreateDto() {
        return new VeiculoCreateDto(
                "ABC1234",
                CARRO,
                "Fiat",
                "Uno"
        );
    }

    public static ClienteUpdateDto atualizarClienteUpdateDto() {
        return new ClienteUpdateDto(
                "João",
                "12345678901",
                "joao@email.com",
                "11741852963",
                true,
                PREMIUM);
    }

    // ESTACIONAMENTOS

    public static EstacionamentoCreateDto criarEstacionamentoCreateDto() {
        return new EstacionamentoCreateDto(
                "Estacionamento Bela Vista",
                enderecoMock(),
                45);
    }

    public static Estacionamento estacionamentoMock() {
        return new Estacionamento(
                1L,
                "Estacionamento Bela Vista",
                enderecoMockEntity(),
                45,
                new ArrayList<>(List.of(
                        new Vaga("A01", CAMINHAO, false),
                        new Vaga("B02", CARRO, false),
                        new Vaga("C03", MOTO, false)
                )));
    }

    public static Estacionamento estacionamentoDisponivelMock() {
        return new Estacionamento(
                1L,
                "Estacionamento Bela Vista 2",
                enderecoMockEntity(),
                45,
                new ArrayList<>(List.of(
                        new Vaga("A01", CAMINHAO, true),
                        new Vaga("B02", CARRO, true),
                        new Vaga("C03", MOTO, true)
                )));
    }

    public static EstacionamentoUpdateDto atualizarEstacionamentoUpdateDto() {
        return new EstacionamentoUpdateDto(
                1L,
                "Estacionamento Bela Vista II",
                enderecoMock(),
                45);
    }

    public static EnderecoDto enderecoMock() {
        return new EnderecoDto(
                "Avenida Paulista",
                "1500",
                "Bela Vista",
                "São Paulo",
                "SP",
                "01310-000");
    }

    public static Endereco enderecoMockEntity() {
        return new Endereco(
                "Avenida Paulista",
                "1500",
                "Bela Vista",
                "São Paulo",
                "SP",
                "01310-000");
    }

    public static Vaga vagaMock() {
        return new Vaga(
                "A01",
                CARRO,
                false
        );
    }

    public static VagaCreateDto vagaDisponivel() {
        return new VagaCreateDto(
                "D05",
                CAMINHAO
                );
    }

    public static VagaCreateDto vagaJaCadastrada() {
        return new VagaCreateDto(
                "A01",
                CAMINHAO
        );
    }

    // PAGAMENTOS E TICKETS
    public static PagamentoTicketDto pagamentoTicketDtoMock() {
        return new PagamentoTicketDto(
                1234L,
                DINHEIRO
        );
    }

    public static Ticket ticketMock () {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime horaEntrada = LocalDateTime.parse("01/01/2025 08:00:00", formatter);
        LocalDateTime horaSaida = horaEntrada.plusHours(5);

        return new Ticket(
                1234L,
                clienteMock(),
                veiculoMock(),
                estacionamentoDisponivelMock(),
                "A01",
                horaEntrada,
                horaSaida,
                false,
                BigDecimal.valueOf(30.0)
        );
    }

    public static Ticket ticketMock_CenarioDeFechamentoDoTicket () {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        LocalDateTime horaEntrada = LocalDateTime.parse("01/01/2025 08:00:00", formatter);

        return new Ticket(
                12345L,
                clienteMock(),
                veiculoMock(),
                estacionamentoDisponivelMock(),
                "A01",
                horaEntrada,
                null,
                false,
                null
        );
    }

    public static TicketCreateDto ticketCreateDtoMock() {
        return new TicketCreateDto(
                1L,
                veiculoMock()
        );
    }

    public static TicketCreateDto ticketCreateDtoMock_Cenario2() {
        return new TicketCreateDto(
                1L,
                veiculoCaminhaoMock()
        );
    }

    public static PagamentoPlanoMensalDto pagamentoPlanoMensalMock() {
        return new PagamentoPlanoMensalDto(
                1L,
                DINHEIRO,
                PREMIUM
        );
    }

    public static Cliente clienteMock() {
        return new Cliente(
                1L,
                "Marcos",
                "12345678901",
                "marcos@email.com",
                "11987654321",
                new ArrayList<>(List.of(
                        new Veiculo("ABC1234", CARRO, "Uno", "Fiat"),
                        new Veiculo("DEF5678", MOTO, "CG 125", "Honda"),
                        new Veiculo("GHI9101", CAMINHAO, "FH 460", "Volvo")
                )),
                null,
                false,
                null,
                null);
    }

    public static Cliente clienteMock_PlanoPremium() {
        return new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com",
                "11987654321",
                new ArrayList<>(List.of(
                        new Veiculo("ABC1234", CARRO, "Uno", "Fiat"),
                        new Veiculo("DEF5678", MOTO, "CG 125", "Honda")
                )),
                PREMIUM,
                true,
                LocalDateTime.now().plusDays(20),
                null);
    }

    public static Cliente clienteMock_PlanoBasic() {
        return new Cliente(
                1L,
                "João",
                "12345678901",
                "joao@email.com",
                "11987654321",
                new ArrayList<>(List.of(
                        new Veiculo("ABC1234", CARRO, "Uno", "Fiat"),
                        new Veiculo("DEF5678", MOTO, "CG 125", "Honda"),
                        new Veiculo("GHI9101", CAMINHAO, "FH 460", "Volvo")
                )),
                BASIC,
                true,
                LocalDateTime.now().plusDays(20),
                null);
    }


    public static Veiculo veiculoMock() {
        return new Veiculo(
                "ABC1234",
                CARRO,
                "Uno",
                "Fiat"
        );
    }

    public static Veiculo veiculoCaminhaoMock() {
        return new Veiculo(
                "GHI9101",
                CAMINHAO,
                "FH 460",
                "Volvo"
        );
    }


}

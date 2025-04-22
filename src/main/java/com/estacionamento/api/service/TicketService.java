package com.estacionamento.api.service;

import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.exceptions.PlanoMensalObrigatorioException;
import com.estacionamento.api.domain.exceptions.RecursoNaoEncontradoException;
import com.estacionamento.api.domain.historico.dto.EntradaClienteDto;
import com.estacionamento.api.domain.ticket.Ticket;
import com.estacionamento.api.domain.vaga.Vaga;
import com.estacionamento.api.domain.veiculo.VeiculoTipo;
import com.estacionamento.api.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EstacionamentoService estacionamentoService;
    private final ClienteService clienteService;
    private final TicketValidationService ticketValidationService;
    private final ClienteValidationService clienteValidationService;
    private final VagaService vagaService;

    @Transactional
    public Ticket criarTicket(EntradaClienteDto entradaClienteDto) {
        log.info("### Iniciando criação de ticket para veículo: {}", entradaClienteDto.veiculo());

        // Verifica se o veículo é do tipo "CAMINHAO" e lança exceção caso necessário
        if (entradaClienteDto.veiculo().getVeiculoTipo().equals(VeiculoTipo.CAMINHAO)) {
            log.error("### Tentativa de criar ticket para veículo do tipo CAMINHAO sem plano mensal.");
            throw new PlanoMensalObrigatorioException();
        }

        // Inicializa o ticket
        Ticket novoticket = new Ticket();
        novoticket.setVeiculo(entradaClienteDto.veiculo());
        log.info("### Ticket inicializado para veículo: {}", entradaClienteDto.veiculo());

        // Validação do ticket
        ticketValidationService.validateCriarTicket(novoticket);
        log.info("### Validação do ticket realizada com sucesso.");

        // Busca o estacionamento
        log.info("### Buscando estacionamento com ID: {}", entradaClienteDto.estacionamentoId());
        var estacionamento = estacionamentoService.findEstacionamentoById(entradaClienteDto.estacionamentoId());
        log.info("### Estacionamento encontrado: {} com {} vagas.", estacionamento.getNome(), estacionamento.getVagas().size());

        // Busca uma vaga disponível
        log.info("### Buscando vaga disponível para veículo do tipo: {}", entradaClienteDto.veiculo().getVeiculoTipo());
        Vaga vagaDisponivel = estacionamentoService.verificarDisponibilidadeVagaLivre(
                estacionamento.getId(),
                entradaClienteDto.veiculo().getVeiculoTipo()
        );
        log.info("### Vaga disponível encontrada: {}", vagaDisponivel.getNumeroVaga());

        // Atualiza o ticket com informações da vaga e do estacionamento
        novoticket = new Ticket(estacionamento, vagaDisponivel.getNumeroVaga(), entradaClienteDto.veiculo());
        log.info("### Ticket criado para vaga: {} e veículo: {}", vagaDisponivel.getNumeroVaga(), entradaClienteDto.veiculo());

        // Marca a vaga como ocupada e persiste o estado do estacionamento
//        vagaDisponivel.ocuparVaga();
        vagaService.ocuparVaga(entradaClienteDto, vagaDisponivel.getNumeroVaga());
        estacionamentoService.saveEstacionamento(estacionamento);
        log.info("### Vaga {} marcada como ocupada e estado do estacionamento atualizado.", vagaDisponivel.getNumeroVaga());

        // Salva o ticket
        log.info("### Salvando ticket no repositório.");
        novoticket = ticketRepository.save(novoticket);
        log.info("### Ticket salvo com sucesso. ID do ticket: {}", novoticket.getId());

        return novoticket;
    }

    public Ticket buscarTicketPorId(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ticket", id));
    }

    public Ticket findTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Ticket", id));
    }

    public void saveTicket(Ticket ticket) {
        ticketRepository.save(ticket);
    }

    public List<Ticket> listarTickets() {
        return ticketRepository.findAll();
    }
}
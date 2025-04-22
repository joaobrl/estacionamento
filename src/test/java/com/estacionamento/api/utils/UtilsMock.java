package com.estacionamento.api.utils;

import com.estacionamento.api.domain.cliente.Cliente;
import com.estacionamento.api.domain.cliente.dto.ClienteCreateDto;
import com.estacionamento.api.domain.cliente.dto.ClienteUpdateDto;
import com.estacionamento.api.domain.estacionamento.Estacionamento;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoCreateDto;
import com.estacionamento.api.domain.estacionamento.dto.EstacionamentoUpdateDto;
import com.estacionamento.api.domain.historico.dto.EntradaClienteDto;
import com.estacionamento.api.domain.historico.dto.SaidaClienteDto;
import com.estacionamento.api.domain.ticket.Ticket;
import com.estacionamento.api.domain.vaga.dto.VagaCreateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UtilsMock {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static final String CLIENTE_CREATE_JSON = "src/test/resources/json/cliente/cliente_cadastrar.json";
    public static final String CLIENTE_UPDATE_JSON = "src/test/resources/json/cliente/cliente_atualizar.json";
    public static final String CLIENTE_MENSALISTA_JSON = "src/test/resources/json/cliente/cliente_mensalista.json";
    public static final String CLIENTE_TICKET_ABERTO_JSON = "src/test/resources/json/cliente/cliente_ticket_aberto.json";
    public static final String CLIENTE_AVULSO_JSON = "src/test/resources/json/cliente/cliente_avulso.json";
    public static final String ESTACIONAMENTO_JSON = "src/test/resources/json/estacionamento/estacionamento.json";
    public static final String ESTACIONAMENTO_CREATE_JSON = "src/test/resources/json/estacionamento/estacionamento_cadastrar.json";
    public static final String ESTACIONAMENTO_UPDATE_JSON = "src/test/resources/json/estacionamento/estacionamento_atualizar.json";
    public static final String ESTACIONAMENTO_VAGA_EXISTENTE_JSON = "src/test/resources/json/estacionamento/estacionamento_vaga_existente.json";
    public static final String ESTACIONAMENTO_CADASTRAR_VAGA_LIVRE_JSON = "src/test/resources/json/estacionamento/estacionamento_cadastrar_vaga_avulso.json";
    public static final String ESTACIONAMENTO_CADASTRAR_VAGA_MENSALISTA_JSON = "src/test/resources/json/estacionamento/estacionamento_cadastrar_vaga_mensalista.json";
    public static final String ESTACIONAMENTO_FILTRAR_VAGA_LIVRE_JSON = "src/test/resources/json/vaga/vaga_filtrar_avulso.json";
    public static final String ESTACIONAMENTO_FILTRAR_VAGA_MENSALISTA_JSON = "src/test/resources/json/vaga/vaga_filtrar_mensalista.json";
    public static final String ESTACIONAMENTO_FILTRAR_VAGA_DISPONIVEL_JSON = "src/test/resources/json/vaga/vaga_filtrar_disponivel.json";
    public static final String ESTACIONAMENTO_FILTRAR_VAGA_CARRO_DISPONIVEL_JSON = "src/test/resources/json/vaga/vaga_filtrar_carro_disponivel.json";
    public static final String ESTACIONAMENTO_FILTRAR_VAGA_CARRO_DISPONIVEL_MENSALISTA_JSON = "src/test/resources/json/vaga/vaga_filtrar_carro_disponivel_mensalista.json";
    public static final String ESTACIONAMENTO_FILTRAR_VAGA_INDISPONIVEL_JSON = "src/test/resources/json/vaga/vaga_filtrar_indisponivel.json";
    public static final String ESTACIONAMENTO_FILTRAR_VAGA_MOTO_INDISPONIVEL_JSON = "src/test/resources/json/vaga/vaga_filtrar_moto_indisponivel.json";
    public static final String ESTACIONAMENTO_FILTRAR_VAGA_MOTO_INDISPONIVEL_LIVRE_JSON = "src/test/resources/json/vaga/vaga_filtrar_moto_indisponivel_avulso.json";
    public static final String PAGAMENTO_TICKET_JSON = "src/test/resources/json/pagamento/pagamento_ticket.json";
    public static final String PAGAMENTO_PLANO_MENSAL_JSON = "src/test/resources/json/pagamento/pagamento_plano_mensal.json";
    public static final String PAGAMENTO_PLANO_TRIMESTRAL_JSON = "src/test/resources/json/pagamento/pagamento_plano_trimestral.json";
    public static final String PAGAMENTO_PLANO_ANUAL_JSON = "src/test/resources/json/pagamento/pagamento_plano_anual.json";
    public static final String MOVIMENTACAO_ENTRADA_AVULSO_JSON = "src/test/resources/json/movimentacao/movimentacao_entrada_avulso.json";
    public static final String MOVIMENTACAO_ENTRADA_MENSALISTA_JSON = "src/test/resources/json/movimentacao/movimentacao_entrada_mensalista.json";
    public static final String MOVIMENTACAO_SAIDA_JSON = "src/test/resources/json/movimentacao/movimentacao_saida.json";
    public static final String MOVIMENTACAO_ENTRADA_CAMINHAO_JSON = "src/test/resources/json/movimentacao/movimentacao_entrada_caminhao.json";
    public static final String TICKET_ABERTO_JSON = "src/test/resources/json/ticket/ticket_aberto.json";
    public static final String TICKET_FECHADO_JSON = "src/test/resources/json/ticket/ticket_fechado.json";

    public static <T> T carregarJsonComoObjeto(String caminho, Class<T> classe) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

            return mapper.readValue(new File(caminho), classe);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar ou converter o arquivo JSON: " + caminho, e);
        }
    }

    public static ClienteCreateDto cadastrarClienteJSON() {
        return carregarJsonComoObjeto(CLIENTE_CREATE_JSON, ClienteCreateDto.class);
    }

    public static ClienteUpdateDto atualizarClienteJSON() {
        return carregarJsonComoObjeto(CLIENTE_UPDATE_JSON, ClienteUpdateDto.class);
    }

    public static EstacionamentoCreateDto criarEstacionamentoJSON() {
        return carregarJsonComoObjeto(ESTACIONAMENTO_CREATE_JSON, EstacionamentoCreateDto.class);
    }

    public static EstacionamentoUpdateDto atualizarEstacionamentoJSON() {
        return carregarJsonComoObjeto(ESTACIONAMENTO_UPDATE_JSON, EstacionamentoUpdateDto.class);
    }

    public static VagaCreateDto cadastrarVagaLivreJSON() {
        return carregarJsonComoObjeto(ESTACIONAMENTO_CADASTRAR_VAGA_LIVRE_JSON, VagaCreateDto.class);
    }

    public static VagaCreateDto cadastrarVagaMensalistaJSON() {
        return carregarJsonComoObjeto(ESTACIONAMENTO_CADASTRAR_VAGA_MENSALISTA_JSON, VagaCreateDto.class);
    }

    public static Estacionamento estacionamentoJSON() {
        return carregarJsonComoObjeto(ESTACIONAMENTO_JSON, Estacionamento.class);
    }

    public static VagaCreateDto cadastrarVagaExistenteJSON() {
        return carregarJsonComoObjeto(ESTACIONAMENTO_VAGA_EXISTENTE_JSON, VagaCreateDto.class);
    }

    public static EntradaClienteDto entradaClienteAvulsoJSON() {
        return carregarJsonComoObjeto(MOVIMENTACAO_ENTRADA_AVULSO_JSON, EntradaClienteDto.class);
    }

    public static EntradaClienteDto entradaClienteMensalistaJSON() {
        return carregarJsonComoObjeto(MOVIMENTACAO_ENTRADA_MENSALISTA_JSON, EntradaClienteDto.class);
    }

    public static EntradaClienteDto entradaCaminhaoJSON() {
        return carregarJsonComoObjeto(MOVIMENTACAO_ENTRADA_CAMINHAO_JSON, EntradaClienteDto.class);
    }

    public static SaidaClienteDto saidaClienteJSON() {
        return carregarJsonComoObjeto(MOVIMENTACAO_SAIDA_JSON, SaidaClienteDto.class);
    }

    public static Cliente clienteMensalistaJSON() {
        return carregarJsonComoObjeto(CLIENTE_MENSALISTA_JSON, Cliente.class);
    }

    public static Cliente clienteAvulsoJSON() {
        return carregarJsonComoObjeto(CLIENTE_AVULSO_JSON, Cliente.class);
    }

    public static Cliente clienteTicketAbertoJSON() {
        return carregarJsonComoObjeto(CLIENTE_TICKET_ABERTO_JSON, Cliente.class);
    }

    public static Ticket ticketAbertoJSON() {
        return carregarJsonComoObjeto(TICKET_ABERTO_JSON, Ticket.class);
    }

    public static Ticket ticketFechadoJSON() {
        return carregarJsonComoObjeto(TICKET_FECHADO_JSON, Ticket.class);
    }

}

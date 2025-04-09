package com.estacionamento.api.utils;

import com.estacionamento.api.domain.cliente.dto.ClienteCreateDto;
import com.estacionamento.api.domain.cliente.dto.ClienteUpdateDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UtilsMock {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static final String CLIENTE_CREATE_JSON = "src/test/resources/json/cliente_cadastrar.json";
    public static final String CLIENTE_UPDATE_JSON = "src/test/resources/json/cliente_atualizar.json";


    private static <T> T carregarJsonComoObjeto(String caminho, Class<T> tipoClasse) {
        try {
            String json = new String(Files.readAllBytes(Paths.get(caminho)));
            return objectMapper.readValue(json, tipoClasse);
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
}

package com.estacionamento.api.domainTests.movimentacaoTests;

import com.estacionamento.api.repository.ClienteRepository;
import com.estacionamento.api.service.MovimentacaoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MovimentacaoServiceTests {

    @InjectMocks
    private MovimentacaoService movimentacaoService;
    @Mock
    private ClienteRepository clienteRepository;

    @BeforeEach
    void SetUp() {

    }

}

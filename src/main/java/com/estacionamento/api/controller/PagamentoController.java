package com.estacionamento.api.controller;

import com.estacionamento.api.domain.pagamento.dto.PagamentoListDto;
import com.estacionamento.api.domain.pagamento.dto.PagamentoPlanoMensalDto;
import com.estacionamento.api.domain.pagamento.dto.PagamentoReciboDto;
import com.estacionamento.api.domain.pagamento.dto.PagamentoTicketDto;
import com.estacionamento.api.domain.pagamento.PagamentoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping("/ticket")
    @Transactional
    public ResponseEntity pagamentoTicket(@RequestBody @Valid PagamentoTicketDto dados, UriComponentsBuilder uriComponentsBuilder) {
        var pagamento = pagamentoService.pagamentoTicket(dados);
        var uri = uriComponentsBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();
        return ResponseEntity.created(uri).body(new PagamentoListDto(pagamento));
    }

    @PostMapping("/plano-mensal")
    public ResponseEntity pagamentoPlanoMensal(@RequestBody @Valid PagamentoPlanoMensalDto dados, UriComponentsBuilder uriComponentsBuilder) {
        var pagamento = pagamentoService.pagamentoPlanoMensal(dados);
        var uri = uriComponentsBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();
        return ResponseEntity.created(uri).body(new PagamentoReciboDto(pagamento));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<PagamentoListDto>> listarPagamentos() {
        var pagamentos = pagamentoService.listarPagamentos()
                .stream()
                .map(PagamentoListDto::new)
                .toList();
        return ResponseEntity.ok(pagamentos);
    }


}

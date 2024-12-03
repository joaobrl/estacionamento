
# Documentação do Sistema de Estacionamento

## 1. Valores Cobrados por Tipo de Veículo
- **Motos**:  
  - R$2,00 por hora  
  - R$15,00 por dia  
  - R$200,00 por mês  
- **Carros**:  
  - R$5,00 por hora  
  - R$30,00 por dia  
  - R$400,00 por mês  
- **Caminhões**:  
  - R$10,00 por hora  
  - R$60,00 por dia  
  - R$800,00 por mês  

## 2. Preço por Hora, Dia ou Mês
- **Hora**: Valor cobrado com base na quantidade de horas estacionadas.
- **Dia**: Valor fixo cobrado por um dia inteiro de estacionamento.
- **Mês**: Plano mensal com desconto, cobrado mensalmente.

---

## 3. Entidades e Relacionamentos

### **Entidades**
1. **Veículo**: Representa os diferentes tipos de veículos que podem utilizar o estacionamento.  
   - **Atributos**: `id`, `tipo`, `placa`, `modelo`, `marca`, `planoMensal`
2. **Estacionamento**: Representa o estacionamento em si.  
   - **Atributos**: `id`, `nome`, `endereço`, `capacidade`
3. **Vaga**: Representa uma vaga específica no estacionamento.  
   - **Atributos**: `id`, `numero`, `tipoVaga` (moto, carro, caminhão), `disponibilidade`
4. **Ticket**: Representa o ticket gerado para um veículo estacionado.  
   - **Atributos**: `id`, `veiculoId`, `vagaId`, `horaEntrada`, `horaSaida`, `valor`
5. **Pagamento**: Representa o pagamento realizado pelo uso do estacionamento.  
   - **Atributos**: `id`, `ticketId`, `valor`, `dataPagamento`, `metodoPagamento`

### **Relacionamentos**
- **Veículo** tem muitos **Tickets** (um para cada entrada no estacionamento).
- **Estacionamento** tem muitas **Vagas**.
- **Vaga** pode ter muitos **Tickets** (mas apenas um ativo por vez).
- **Ticket** pertence a um **Veículo** e uma **Vaga**.
- **Ticket** tem um **Pagamento**.

---

## 4. Endpoints

### **Veículos**
- **Registrar Veículo**:  
  `POST /api/veiculos`  
  **Corpo**:  
  ```json
  {
    "tipo": "carro",
    "placa": "ABC1234",
    "modelo": "Gol",
    "marca": "Volkswagen",
    "planoMensal": "false"
  }
  ```
- **Obter Veículo**:  
  `GET /api/veiculos/{id}`
### **Estacionamento**
- **Registrar Estacionamento**:  
  `POST /api/estacionamentos`  
  **Corpo**:
  ```json
  {
    "nome": "Estacionamento do Zé",
    "endereço": {
        "rua": "Rua das Flores",
        "numero": 123,
        "bairro": "Centro",
        "cidade": "São Paulo",
        "estado": "SP",
        "cep": "12345-678"},
    "capacidade": 100
  }
  ```
- **Listar Estacionamentos**:  
  `GET /api/estacionamentos/listar`


- **Detalhar Estacionamento**:  
  `GET /api/estacionamentos/{id}


- **Atualizar Estacionamento**:  
  `POST /api/atualizar`

  **Corpo**:
  ```json
  {
    "id": "1",
    "nome": "Estacionamento do Zé",
    "endereço": {
        "rua": "Rua das Flores",
        "numero": 123,
        "bairro": "Centro",
        "cidade": "São Paulo",
        "estado": "SP",
        "cep": "12345-678"},
    "capacidade": 100
  }
  ```
---
### **Vaga**
- **Adicionar Vaga**:  
  `POST /api/estacionamentos/{id}/vagas`  
  **Corpo**:  
  ```json
  {
    "numero": 1,
    "tipoVaga": "carro",
    "disponibilidade": true
  }
  ```
- **Listar Vagas**:  
  `GET /api/estacionamentos/{id}/vagas`

### **Tickets**
- **Gerar Ticket**:  
  `POST /api/tickets`  
  **Corpo**:  
  ```json
  {
    "veiculoId": 1,
    "vagaId": 1,

  }
  ```
- **Obter Ticket**:  
  `GET /api/tickets/{id}`  


- **Fechar Ticket**:  
  `PUT /api/tickets/saida`  
  
- **Corpo**:  
  ```json
  {
    "horaSaida": "2024-11-21T20:00:00"
  }
  ```

### **Pagamentos**
- **Registrar Pagamento**:  
  `POST /api/pagamentos`  
  **Corpo**:  
  ```json
  {
    "ticketId": 1,
    "valor": 20.00,
    "dataPagamento": "2024-11-21T20:30:00",
    "metodoPagamento": "cartão"
  }
  ```
- **Listar todos pagamentos**:  
  `GET /api/tickets/{id}`
---

## 5. Regras de Negócios
1. **Calcular Valor do Ticket**:  
   O valor é calculado com base no tipo de veículo e o tempo de permanência.
2. **Disponibilidade de Vagas**:  
   Uma vaga só pode ser ocupada por um veículo de tipo correspondente e deve estar disponível.
3. **Pagamento Obrigatório**:  
   Um ticket deve ser pago antes de liberar a vaga para outro veículo.
4. **Planos Mensais**:  
   Um veículo registrado em um plano mensal terá acesso ilimitado durante o período contratado.

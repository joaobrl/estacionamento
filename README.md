# Documentação do Sistema de Estacionamento

## 1. Valores Cobrados por Tipo de Veículo

- **Motos**:
  - R\$3,00 por hora
  - R\$18,00 por dia
  - R\$350,00 (Plano Básico) / R\$500,00 (Plano Premium) por mês
- **Carros**:
  - R\$6,00 por hora
  - R\$35,00 por dia
  - R\$350,00 (Plano Básico) / R\$500,00 (Plano Premium) por mês
- **Caminhões**:
  - Obrigatório plano mensal: R\$1000,00 (Plano Especial)

## 2. Fluxo do Sistema

1. O estacionamento é cadastrado com sua capacidade de vagas.
2. Cada vaga é cadastrada individualmente, especificando o tipo de veículo que pode ocupá-la.
3. Um cliente pode ser cadastrado e associar até 3 veículos.
4. Tickets podem ser abertos para clientes esporádicos ou mensalistas.
5. Ao fechar o ticket, é calculado o valor conforme tempo de permanência e tipo de veículo.
6. O pagamento do ticket deve ser realizado antes de liberar a vaga.
7. Clientes podem aderir a planos mensais conforme os tipos disponíveis.

---

## 3. Endpoints Disponíveis

### **Cliente**

- **Cadastrar Cliente**: `POST /api/clientes`

  ```json
  {
    "nome": "João Silva",
    "cpf": "123.456.789-00",
    "email": "joao@email.com",
    "telefone": "11999999999"
  }
  ```

- **Cadastrar Veículo**: `POST /api/clientes/{id}/veiculos`

  **Moto:**

  ```json
  {
    "placa": "XYZ-9876",
    "veiculoTipo": "MOTO",
    "modelo": "CG 160",
    "marca": "Honda"
  }
  ```

  **Carro:**

  ```json
  {
    "placa": "ABC-1234",
    "veiculoTipo": "CARRO",
    "modelo": "Corolla",
    "marca": "Toyota"
  }
  ```

  **Caminhão:**

  ```json
  {
    "placa": "DEF-5678",
    "veiculoTipo": "CAMINHAO",
    "modelo": "Atego",
    "marca": "Mercedes"
  }
  ```

### **Estacionamento**

- **Cadastrar Estacionamento**: `POST /api/estacionamentos`

  ```json
  {
    "nome": "Estacionamento Centro",
    "endereco": {
      "rua": "Rua das Flores",
      "numero": 123,
      "bairro": "Centro",
      "cidade": "São Paulo",
      "estado": "SP",
      "cep": "12345-678"
    },
    "capacidade": 100
  }
  ```

- **Cadastrar Vaga**: `POST /api/estacionamentos/{id}/vagas`

  **Moto:**

  ```json
  {
    "numeroVaga": "A1",
    "veiculoTipo": "MOTO"
  }
  ```

  **Carro:**

  ```json
  {
    "numeroVaga": "B2",
    "veiculoTipo": "CARRO"
  }
  ```

  **Caminhão:**

  ```json
  {
    "numeroVaga": "C3",
    "veiculoTipo": "CAMINHAO"
  }
  ```

### **Ticket**

- **Abrir Ticket**: `POST /api/tickets`

  ```json
  {
    "estacionamentoId": 1,
    "veiculo": {
      "placa": "ABC-1234",
      "veiculoTipo": "CARRO",
      "modelo": "Corolla",
      "marca": "Toyota"
    }
  }
  ```

- **Fechar Ticket**: `PUT /api/tickets/saida`

  ```json
  {
    "horaSaida": "2024-11-21T20:00:00"
  }
  ```

### **Pagamento**

- **Pagar Ticket**: `POST /api/pagamentos/ticket`

  ```json
  {
    "ticketId": 1,
    "metodoPagamento": "CARTAO"
  }
  ```

- **Pagar Plano Mensal**: `POST /api/pagamentos/plano`

  ```json
  {
    "clienteId": 1,
    "metodoPagamento": "CARTAO",
    "tipoPlano": "PREMIUM"
  }
  ```


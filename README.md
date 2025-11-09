
# Microsserviço de Validação de Comandos de Corte e Religação

## 📋 Sobre o Projeto
Microsserviço desenvolvido para o desafio técnico da OpenMDX, responsável por validar comandos de corte e religação de energia elétrica conforme regulamentação da ANEEL.

## 🚀 Tecnologias
- **Java 17**
- **Spring Boot 3.5.7**
- **PostgreSQL 15**
- **Spring Data JPA**
- **Flyway** (migrations)
- **Swagger/OpenAPI 3** (documentação)
- **JUnit 5** (testes)
- **Maven**

## 📦 Pré-requisitos
- Java 17+
- PostgreSQL 15
- Maven 3.6+

## 🔧 Configuração do Ambiente

### 1. Banco de Dados (PostgreSQL 15)
```sql
CREATE DATABASE seu_banco;
```

### 2. Configuração da Aplicação
Arquivo `src/main/resources/application.properties`:
```properties
# PostgreSQL 15
spring.datasource.url=jdbc:postgresql://localhost:5432/seu_banco
spring.datasource.username=postgres
spring.datasource.password=sua_senha

# JPA
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

# Server
server.port=8080
```

## 🏃‍♂️ Executando a Aplicação

###  Maven
```bash
# Instalar dependências
mvn clean install

# rodar o projeto
mvn spring-boot:run
```



## 📡 Endpoints da API

### 1. Validar Comando
**POST** `/api/commands/validate`
```json
{
  "ucId": "UC001",
  "tipoUc": "residencial",
  "tipoComando": "corte",
  "timestamp": "2025-11-06T14:30:00Z",
  "regiao": "fortaleza",
  "solicitante": "sistema_comercial"
}
```

**Resposta de Sucesso:**
```json
{
  "aprovado": true,
  "motivo": null,
  "prazoExecucao": null
}
```

**Resposta de Bloqueio:**
```json
{
  "aprovado": false,
  "motivo": "Corte fora do horário permitido (8h-18h em dias úteis)",
  "prazoExecucao": null
}
```

### 2. Cadastrar Feriado
**POST** `/api/holidays`
```json
{
  "data": "2025-11-15",
  "nome": "Proclamação da República",
  "tipo": "nacional",
  "regiao": null
}
```

**Resposta:**
```json
{
  "id": 1,
  "data": "2025-11-15",
  "nome": "Proclamação da República",
  "tipo": "nacional"
}
```

### 3. Consultar Histórico
**GET** `/api/commands/history?ucId=UC001&dataInicio=2025-11-01&dataFim=2025-11-30`

**Resposta:**
```json
{
  "total": 5,
  "comandos": [
    {
      "id": 1,
      "ucId": "UC001",
      "tipoComando": "corte",
      "timestamp": "2025-11-06T14:30:00Z",
      "aprovado": false,
      "motivo": "Corte fora do horário permitido (8h-18h em dias úteis)"
    }
  ]
}
```

## 🧪 Testes

### Executando todos os testes
```bash
mvn test
```

### Testes Unitários Implementados (JUnit 5)
1. ✅ Corte em horário permitido → aprova
2. ✅ Corte em horário proibido (7h) → bloqueia
3. ✅ Corte em feriado nacional → bloqueia
4. ✅ Corte em UC essencial → bloqueia
5. ✅ Religação residencial → aprova + prazo 24h

## 📊 Documentação Interativa

Acesse a documentação Swagger em:
```
http://localhost:8080/swagger-ui.html
```

## 🗃️ Dados de Teste

### Unidades Consumidoras Pré-cadastradas
- `UC001` - Residencial - "João Silva" - Fortaleza
- `UC002` - Essencial - "Hospital Regional" - Fortaleza
- `UC003` - Comercial - "Supermercado ABC" - Fortaleza

### Feriados Nacionais Cadastrados
- 15/11/2025 - Proclamação da República
- 20/11/2025 - Consciência Negra
- 25/12/2025 - Natal

## 🔍 Regras de Negócio Implementadas

### RF001 - Horário Permitido
- **Cortes**: Permitidos apenas entre 8h-18h em dias úteis
- **Religações**: Permitidas em qualquer horário

### RF002 - Feriados e Vésperas
- Bloqueio em feriados nacionais e regionais
- Bloqueio em vésperas de feriados após 12h

### RF003 - UCs Essenciais
- Hospitais, delegacias, bombeiros não podem sofrer corte
- Religações permitidas normalmente

### RF004 - Prazos de Religação
- **Residencial**: 24 horas
- **Comercial/Industrial**: 8 horas
- **Emergência**: 30 minutos

### RF005 - Auditoria
- Todos os comandos são registrados com timestamp e motivo
- Logs imutáveis para rastreabilidade

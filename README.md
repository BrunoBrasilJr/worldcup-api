# ⚽ World Cup API

API REST de futebol construída com **Java + Spring Boot** que **ingere dados reais** da [API-Football](https://www.api-football.com/), armazena tudo em banco próprio e serve os dados de forma **independente** — as rotas públicas leem **somente do banco**, nunca da API externa em tempo real.

> Projeto de portfólio focado em arquitetura em camadas, ingestão automática de dados, processamento próprio e boas práticas de API REST.

---

## 🚀 Funcionalidades

- **Ingestão de dados reais**: consome jogos ao vivo da API-Football e materializa no banco local.
- **Fonte da verdade própria**: depois de ingeridos, todos os dados são servidos a partir do banco. A API externa é usada **apenas** na camada de ingestão.
- **Ingestão idempotente**: cada jogo e time tem um `externalId` (ID da API). Rodar a ingestão várias vezes **atualiza** registros existentes em vez de duplicar.
- **Descoberta automática de times**: times são criados automaticamente conforme aparecem nos jogos ingeridos.
- **Automação com Scheduler**: um `@Scheduled` dispara a ingestão periodicamente (intervalo configurável, calibrado para respeitar o rate limit do plano gratuito).
- **Estatísticas e classificação**: rankings e tabela calculados dinamicamente a partir dos dados do banco.

---

## 🛠️ Tecnologias

- **Java 21**
- **Spring Boot 4**
- Spring Web (MVC)
- Spring Data JPA / Hibernate
- Spring Scheduler (`@Scheduled`)
- H2 Database (em memória, para desenvolvimento)
- RestClient (cliente HTTP para a API-Football)
- Lombok
- Maven

---

## 🧱 Arquitetura

```
com.portfolio.worldcup
├── controller    → endpoints REST públicos (servem do banco)
├── service       → regras de negócio e cálculo de estatísticas
├── repository    → acesso a dados (Spring Data JPA)
├── entity        → entidades JPA
├── dto           → objetos de resposta da API
├── mapper        → conversão Entidade → DTO
├── exception     → tratamento global de erros
├── ingestion     → camada de ingestão (cliente da API-Football, DTOs e service)
│   └── dto       → DTOs que espelham a resposta da API-Football
├── scheduler     → agendador da ingestão automática (@Scheduled)
├── config        → configurações (RestClient, scheduler)
└── bootstrap     → utilitários de inicialização
```

**Princípios aplicados:**

- Separação clara entre **ingestão** (escreve no banco a partir da API) e **API pública** (lê do banco).
- A API externa nunca é chamada pelas rotas públicas — garante independência e controle de rate limit.
- Ingestão idempotente via `externalId`.
- Respostas sempre via DTOs (entidades nunca são expostas cruas).

---

## 🔵 Ingestão de dados (API-Football)

A integração usa a API-Football **apenas como fonte**. O fluxo:

1. O `ApiFootballClient` chama o endpoint `/fixtures?live=all` (jogos ao vivo no mundo).
2. O `IngestionService` converte a resposta, resolve os times (cria se necessário) e cria/atualiza cada jogo no banco.
3. O `IngestionScheduler` repete esse processo automaticamente em intervalo configurável.

**Autenticação:** a chave da API é lida de uma variável de ambiente (`API_FOOTBALL_KEY`) — nunca fica hardcoded no código.

**Controle de quota:** o plano gratuito da API-Football permite 100 requisições/dia. O scheduler é calibrado para caber nesse limite, e pode ser desligado via `ingestion.enabled=false`.

---

## 📡 Endpoints públicos

| Método | Rota                   | Descrição                     |
| ------ | ---------------------- | ----------------------------- |
| GET    | `/matches/today`       | Jogos de hoje                 |
| GET    | `/matches/live`        | Jogos ao vivo                 |
| GET    | `/matches/{id}`        | Detalhes de um jogo           |
| GET    | `/matches/{id}/events` | Eventos (timeline) de um jogo |
| GET    | `/teams`               | Lista de times                |
| GET    | `/players`             | Lista de jogadores            |
| GET    | `/stats/top-scorers`   | Ranking de artilheiros        |
| GET    | `/stats/top-assists`   | Ranking de assistências       |
| GET    | `/standings`           | Tabela de classificação       |

### Endpoint administrativo (ingestão manual)

| Método | Rota                 | Descrição                                      |
| ------ | -------------------- | ---------------------------------------------- |
| GET    | `/admin/ingest-live` | Dispara a ingestão manualmente (consome 1 req) |

### Exemplo de resposta — `GET /matches/live`

```json
[
  {
    "id": 1,
    "homeTeamName": "Tucson",
    "awayTeamName": "City SC",
    "homeScore": 0,
    "awayScore": 0,
    "matchDateTime": "2026-06-27T02:00:00",
    "stadium": null,
    "city": null,
    "status": "LIVE",
    "currentMinute": 42
  }
]
```

---

## ▶️ Como executar (Windows)

### Pré-requisitos

- Java 21+
- Maven (ou o wrapper `mvnw` incluído)
- Uma chave gratuita da [API-Football](https://dashboard.api-football.com/register)

### Passos

```bash
# 1. Clonar
git clone https://github.com/BrunoBrasilJr/worldcup-api.git
cd worldcup-api

# 2. Definir a chave da API como variável de ambiente (Windows)
setx API_FOOTBALL_KEY "SUA_CHAVE_AQUI"
# (feche e reabra o terminal/IDE para a variável ter efeito)

# 3. Rodar
mvnw.cmd spring-boot:run
```

A aplicação sobe em **http://localhost:8080**.

Para disparar a ingestão manualmente: acesse `http://localhost:8080/admin/ingest-live`.
A ingestão automática roda sozinha em intervalo configurável (veja `ingestion.enabled` no `application.properties`).

---

## 🗄️ Banco de dados (H2)

Console web: `http://localhost:8080/h2-console`

| Campo    | Valor                    |
| -------- | ------------------------ |
| JDBC URL | `jdbc:h2:mem:worldcupdb` |
| User     | `sa`                     |
| Password | _(em branco)_            |

---

## 🌱 Possíveis evoluções

- Ingestão de **eventos detalhados** (gols, cartões) via `/fixtures/events`.
- Ingestão de **estatísticas de jogadores** para enriquecer os rankings.
- **WebSocket** para enviar atualizações ao cliente em tempo real.
- Migração para **PostgreSQL** em produção.
- **Swagger/OpenAPI** para documentação interativa.
- Testes automatizados (JUnit + Mockito).
- Cache (ex: Caffeine/Redis) para otimizar leituras.

---

## 👤 Autor

**Bruno Brasil** — projeto de estudo e portfólio.

> Sinta-se à vontade para abrir issues ou sugerir melhorias.

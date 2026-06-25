# ⚽ World Cup API

API REST de Copa do Mundo construída com **Java + Spring Boot**, com suporte a jogos ao vivo **simulados em tempo real**, eventos de partida (gols, cartões, substituições, defesas) e estatísticas calculadas automaticamente (artilheiros, assistências, defesas e classificação).

> Projeto de portfólio focado em demonstrar arquitetura em camadas, JPA/Hibernate, processamento em tempo real com `@Scheduled` e boas práticas de API REST.

---

## 🚀 Funcionalidades

- **Jogos**: listagem de jogos do dia, jogos ao vivo, histórico e detalhes completos.
- **Eventos da partida**: gols, assistências, cartões (amarelo/vermelho), substituições, pênaltis, VAR e defesas — exibidos como uma timeline ordenada por minuto.
- **Estatísticas automáticas**: artilheiros, assistências, ranking de goleiros (defesas) e tabela de classificação — tudo **calculado dinamicamente** a partir dos eventos, sem tabelas redundantes.
- **Tempo real (simulado)**: um agendador (`@Scheduled`) faz os jogos ao vivo avançarem sozinhos — incrementa o minuto, troca o status automaticamente (1º tempo → intervalo → 2º tempo → fim) e gera gols aleatórios que alimentam as estatísticas em tempo real.

---

## 🛠️ Tecnologias

- **Java 21**
- **Spring Boot 4**
- Spring Web (MVC)
- Spring Data JPA / Hibernate
- H2 Database (em memória, para desenvolvimento)
- Lombok
- Maven

---

## 🧱 Arquitetura

O projeto segue uma separação clássica em camadas:

```
com.portfolio.worldcup
├── controller    → endpoints REST (porta de entrada HTTP)
├── service       → regras de negócio e cálculo de estatísticas
├── repository    → acesso a dados (Spring Data JPA)
├── entity        → entidades JPA (mapeamento das tabelas)
├── dto           → objetos de resposta da API (Data Transfer Objects)
├── mapper        → conversão Entidade → DTO
├── exception     → tratamento global de erros (@RestControllerAdvice)
├── scheduler     → simulação de jogos em tempo real (@Scheduled)
├── config        → configurações (habilitação do agendador)
└── bootstrap     → carga inicial de dados (DataSeeder)
```

**Princípios aplicados:**

- A API nunca expõe entidades cruas — sempre devolve DTOs, evitando vazamento de detalhes do banco e loops de serialização.
- Controllers são "magros": apenas recebem a requisição e delegam ao service.
- Estatísticas não são persistidas; são derivadas dos eventos a cada requisição.
- Erros retornam um corpo JSON padronizado com o status HTTP adequado.

---

## 📡 Endpoints

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
| GET    | `/stats/top-saves`     | Ranking de goleiros (defesas) |
| GET    | `/standings`           | Tabela de classificação       |

### Exemplo de resposta — `GET /matches/live`

```json
[
  {
    "id": 2,
    "homeTeamName": "Franca",
    "awayTeamName": "Espanha",
    "homeScore": 1,
    "awayScore": 0,
    "matchDateTime": "2026-06-24T23:39:34.5",
    "stadium": "Lusail",
    "city": "Doha",
    "status": "LIVE",
    "currentMinute": 30
  }
]
```

### Exemplo de resposta — `GET /stats/top-saves`

```json
[
  { "playerId": 4, "playerName": "Alisson", "teamName": "Brasil", "total": 3 },
  {
    "playerId": 8,
    "playerName": "Martinez",
    "teamName": "Argentina",
    "total": 2
  }
]
```

### Exemplo de resposta de erro — `GET /matches/999`

```json
{
  "timestamp": "2026-06-24T23:48:26.61",
  "status": 404,
  "error": "Not Found",
  "message": "Jogo com id 999 nao encontrado(a).",
  "path": "/matches/999"
}
```

---

## ▶️ Como executar

### Pré-requisitos

- Java 21+
- Maven (ou use o wrapper `mvnw` incluído no projeto)

### Passos

```bash
# 1. Clonar o repositório
git clone https://github.com/SEU_USUARIO/worldcup-api.git
cd worldcup-api

# 2. Rodar a aplicação (Windows)
mvnw.cmd spring-boot:run

# 2. Rodar a aplicação (Linux/Mac)
./mvnw spring-boot:run
```

A aplicação sobe em **http://localhost:8080**.

Ao iniciar, o banco é populado automaticamente (times, jogadores, jogos e eventos) e a simulação de tempo real começa a rodar — observe o terminal para ver os jogos ao vivo avançando.

---

## 🗄️ Banco de dados (H2)

Durante o desenvolvimento, o projeto usa o H2 em memória. O console web fica disponível em:

```
http://localhost:8080/h2-console
```

| Campo    | Valor                    |
| -------- | ------------------------ |
| JDBC URL | `jdbc:h2:mem:worldcupdb` |
| User     | `sa`                     |
| Password | _(em branco)_            |

> ⚠️ Por ser em memória, os dados são reiniciados a cada execução.

---

## 🔄 Como funciona a simulação em tempo real

Um componente `@Scheduled` é executado a cada 2 segundos e, para cada jogo com status `LIVE`/`HALFTIME`:

1. Incrementa o minuto atual (`currentMinute`).
2. Aplica as transições de status:
   - minuto 45 → `HALFTIME` (intervalo)
   - retoma o 2º tempo → `LIVE`
   - minuto 90 → `FINISHED`
3. Com probabilidade configurável, gera um gol: sorteia o time e um jogador, atualiza o placar e registra um evento `GOAL`.

Como as estatísticas são derivadas dos eventos, os rankings de artilheiros e a classificação refletem essas mudanças automaticamente.

---

## 🌱 Possíveis evoluções

- Substituir a simulação por **ingestão de dados reais** a partir de uma API de futebol externa.
- Adicionar **WebSocket** para enviar atualizações ao cliente sem necessidade de polling.
- Persistência em **PostgreSQL** para ambiente de produção.
- Autenticação/autorização (Spring Security + JWT).
- Documentação interativa com **Swagger/OpenAPI**.
- Testes automatizados (JUnit + Mockito) e cobertura.
- Paginação e filtros nos endpoints de listagem.

---

## 👤 Autor

Desenvolvido como projeto de estudo e portfólio.

> Sinta-se à vontade para abrir issues ou sugerir melhorias.

# CourtFlow

[🇺🇸 English](README.md)

Um sistema de reservas de quadras esportivas construído para explorar controle de concorrência, expiração orientada a eventos e Clean Architecture num backend real, em nível de produção — construído como projeto de portfólio para vagas de estágio/júnior em backend Java.

O CourtFlow permite que usuários reservem quadras esportivas (tênis, futsal, etc.) garantindo que duas pessoas nunca consigam reservar com sucesso a mesma quadra em horários sobrepostos — mesmo sob requisições concorrentes — e expira automaticamente reservas não confirmadas após uma janela de TTL via fila de mensagens, sem nenhum job de limpeza manual.

## Por que esse projeto

A maioria dos projetos de portfólio CRUD não toca nas partes da engenharia de backend que realmente quebram em produção: condições de corrida, leituras desatualizadas e limpeza de estado abandonado. O CourtFlow foi construído especificamente para praticar esses problemas com ferramentas reais (lock otimista, dead-letter queue do RabbitMQ) em vez de simulá-los.

## Funcionalidades

- **Gestão de quadras** — CRUD de quadras esportivas, escrita restrita a admin.
- **Fluxo de reservas** — criar (com detecção de conflito), confirmar, cancelar, buscar por id, listar por quadra.
- **Segurança de concorrência** — lock otimista (`@Version`) impede reserva duplicada sob requisições simultâneas; validado com um teste de concorrência dedicado usando `ExecutorService` + `CountDownLatch`.
- **Expiração automática** — reservas `PENDING` nunca confirmadas transicionam automaticamente para `EXPIRED` via TTL + dead-letter queue do RabbitMQ, sem cron job ou polling envolvido.
- **Autenticação JWT e autorização por papel** — registro, login e regras explícitas `hasRole`/`hasAnyRole` por endpoint (nunca um `.authenticated()` genérico).
- **Tratamento de erro centralizado e consistente** — toda resposta de erro segue um único formato `ApiErrorResponse`.
- **Observabilidade** — Spring Boot Actuator com endpoint público `/health` e endpoint `/metrics` restrito a admin; logging estruturado (ECS/JSON) opcional via profile dedicado do Spring.

## Stack tecnológica

| Camada | Tecnologia |
|---|---|
| Linguagem / Runtime | Java 21 |
| Framework | Spring Boot 4.1.0 |
| Arquitetura | Clean Architecture (`domain` / `application` / `infrastructure`) |
| Persistência | Spring Data JPA, lock otimista (`@Version`) |
| Mensageria | RabbitMQ (TTL + dead-letter queue para expiração de reservas) |
| Segurança | Spring Security, JWT (`jjwt` 0.13.0) |
| Testes | JUnit 5, testes de concorrência com `ExecutorService`/`CountDownLatch` |
| Observabilidade | Spring Boot Actuator, logging estruturado formato ECS (profile opcional) |
| Build | Maven |

## Arquitetura

O CourtFlow segue Clean Architecture, com a camada de domínio tendo **zero** dependência de framework (nem Spring, nem Hibernate):

```
domain/
  model/       → entidades e value objects puros (Court, Booking, TimeSlot, User)
  exception/   → exceções de negócio

application/
  <entidade>/  → um use case por operação (CreateBookingUseCase, ConfirmBookingUseCase, ...)

infrastructure/
  persistence/ → entidades JPA e repositórios Spring Data
  web/         → DTOs, mappers, controllers, tratamento de exceção centralizado
  security/    → serviço JWT, filtro de autenticação, entry point, access denied handler
  config/      → configuração de Security e RabbitMQ
  messaging/   → listener de expiração de reservas (consome a dead-letter queue)
```

`Booking` é modelado como uma máquina de estados explícita (factories `create()` / `reconstruct()`, transições `confirm()` / `cancel()` / `expire()`), cada método validando se a transição é legal.

## Como rodar o projeto

### Pré-requisitos

- Java 21
- Uma instância do RabbitMQ em execução
- Uma instância do PostgreSQL em execução (ajuste as configurações de conexão em `application.properties`)

### Setup

```bash
git clone https://github.com/Rangeldev73/courtflow.git
cd courtflow
# configure suas credenciais de datasource e conexão com o RabbitMQ
./mvnw spring-boot:run
```

### Opcional: logging estruturado (JSON/ECS)

Por padrão, o CourtFlow registra logs em texto simples, legível — mantido como padrão especificamente para que a experiência de "clonar e rodar" localmente continue legível para qualquer pessoa avaliando o projeto. Para alternar a saída do console para JSON formatado em ECS (o formato que uma ferramenta de agregação de logs consumiria), ative o profile `structured-logs`:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=structured-logs
```

ou, executando o jar já compilado:

```bash
java -jar courtflow.jar --spring.profiles.active=structured-logs
```

## ⚠️ Passo manual necessário: bootstrap do primeiro ADMIN

Não existe endpoint público para criar o primeiro usuário `ADMIN` — isso é intencional. Como as operações de escrita de `Court` são restritas a `ADMIN`, permitir auto-registro de admin significaria que qualquer pessoa poderia se conceder privilégios elevados, o que anularia o propósito da restrição de papel.

O primeiro `ADMIN` precisa ser promovido manualmente via SQL direto, após registrar um usuário normal:

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'seu-email@exemplo.com';
```

Qualquer gestão de admins subsequente pode ser feita pela aplicação assim que existir pelo menos uma conta `ADMIN`.

## Testes

```bash
./mvnw test
```

A segurança de concorrência é validada por um teste dedicado que dispara requisições simultâneas de reserva para o mesmo horário e verifica que apenas uma tem sucesso.

> **Nota:** os testes de integração inicialmente tinham como alvo uma configuração baseada em Testcontainers, mas isso está atualmente bloqueado por um problema do Docker Desktop (Windows) não relacionado ao código do projeto — o cliente Docker do Testcontainers recebe respostas vazias/stub do Docker Desktop 4.78, tanto via named pipe quanto via daemon TCP exposto. Os testes atualmente rodam via variáveis de ambiente configuradas manualmente na run configuration da IDE. Registrado como item em aberto, não como bloqueio para o restante do projeto.

## Limitações conhecidas / pendências

- As entradas de log ainda não incluem campos contextuais estruturados (ex: `courtId`, `bookingId`) via MDC — os campos pesquisáveis hoje se limitam ao que o ECS fornece por padrão (nível, logger, timestamp, mensagem). Registrado como issue no GitHub.
- Os testes de integração baseados em Testcontainers estão atualmente bloqueados por um problema local do Docker Desktop (ver seção de Testes acima).

# ClinicFlow

Bootstrap inicial do backend modular em Gradle para o ClinicFlow.

## Requisitos

- Java 21
- Docker

## Comandos

```bash
./gradlew test
./gradlew bootRun
docker compose up -d
```

## Estrutura

- `app`: aplicacao Spring Boot executavel
- `shared:web`: contrato HTTP e tratamento base de erros
- `modules/*`: fronteiras de dominio preparadas para evolucao incremental

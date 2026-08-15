# Gestao

This project was created using the [Ktor Project Generator](https://start.ktor.io).

Here are some useful links to get you started:

* [Ktor Documentation](https://ktor.io/docs/home.html)
* [Ktor GitHub page](https://github.com/ktorio/ktor)
* [Ktor Slack chat](https://app.slack.com/client/T09229ZC6/C0A974TJ9). [Request an invite](https://surveys.jetbrains.com/s3/kotlin-slack-sign-up).

## Features

Here's a list of features included in this project:

| Name                                                     | Description                                                                        |
|----------------------------------------------------------|------------------------------------------------------------------------------------|
| [Static Content](https://start.ktor.io/p/io.ktor/server-static-content) | Serves static files from defined locations                                         |
| [Content Negotiation](https://start.ktor.io/p/io.ktor/server-content-negotiation) | Provides automatic content conversion according to Content-Type and Accept headers |
| [kotlinx.serialization](https://start.ktor.io/p/io.ktor/server-kotlinx-serialization) | Handles JSON serialization using kotlinx.serialization library                     |
| [Exposed](https://start.ktor.io/p/org.jetbrains/server-exposed) | Adds Exposed database to your application                                          |
| [PostgreSQL](https://start.ktor.io/p/org.jetbrains/server-postgres) | Adds Postgres database support                                                     |
| [HTMX](https://htmx.org/)  | Handles page requests and responses                                                |

## Building & Running

To build or run the project, use one of the following tasks:

| Task | Description |
|------|-------------|

If the server starts successfully, you'll see the following output:

```
2024-12-04 14:32:45.584 [main] INFO  Application - Application started in 0.303 seconds.
2024-12-04 14:32:45.682 [main] INFO  Application - Responding at http://0.0.0.0:8080
```

## Container do PostgreSQL

1. O container do PostgreSQL deve estar rodando na rede **_portgresql-network_**
2. O container do PostgreSQL deve se chamar **_postgresql-db_**

Se necessário, executar o comando abaixo para **criar** a rede virtual e **conectar** o container do PostgreSQL na rede correta:

~~~
docker network create postgresql-network
docker network connect postgresql-network postgresql-db
~~~

Caso esses nomes sejam alterados, os respectivos nomes também deverão ser alterados nos comandos de deploy no Docker.

## Deploy da app Gestão BSI no Docker

1. Gerar a imagem docker (a imagem será gerada com a mesma versão do projeto)

~~~
gradlew buildImage
~~~

2. Ir para a pasta do projeto

3. Carregar a imagem

~~~
docker load -i build/jib-image.tar
~~~
4. Subir o container

~~~
docker run -d -p 8080:8080 --name gestao-bsi-ktor --network  postgresql-network  -e DB_URL=jdbc:postgresql://postgresql-db:5432/BSI -e APP_PORT=8080 -d gestao-bsi-ktor-docker-image:1.0.0
~~~

## Funcionalidades

* Diário: lista de alunos inscritos em uma disciplina (DONE)
* Download de turmas
* Geração das inscrições (DONE)
* Importação das inscrições (DONE)
* Inscrições: alunos e suas inscrições com status
* Alunos inscritos em turmas erradas

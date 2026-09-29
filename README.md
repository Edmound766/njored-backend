# njored-backend

The API server for **Njored**, a WhatsApp lead-management tool for real estate agencies. It receives WhatsApp Cloud API webhooks from Meta, turns new senders into leads, assigns each lead to an agent round-robin, sends an automatic acknowledgement, and stores the conversation history. It supports multiple businesses (multi-tenant).

Built with Kotlin, [Ktor](https://ktor.io) 3, [Exposed](https://github.com/JetBrains/Exposed), PostgreSQL and Flyway.

## How it works

1. Meta sends `POST /webhooks/whatsapp` when a customer messages a business's WhatsApp number.
2. The server responds `200` right away, because Meta retries slow or non-200 responses. It then drops duplicate deliveries using the message ID (`processed_webhooks`).
3. The receiving `phone_number_id` is used to look up the WhatsApp account, which identifies the business.
4. If the sender is new, a lead is created, assigned to the active agent who has gone longest without an assignment, and sent a greeting through the Graph API.
5. The inbound message is saved to the lead's conversation.

## API

| Method | Path                 | Description                                                         |
|--------|----------------------|---------------------------------------------------------------------|
| `GET`  | `/webhooks/whatsapp` | Meta webhook verification (`hub.mode`, `hub.verify_token`, `hub.challenge`). The verify token must match a row in `whatsapp_accounts`. |
| `POST` | `/webhooks/whatsapp` | Receives inbound WhatsApp messages.                                 |
| `GET`  | `/`                  | Health/hello check.                                                 |

`routes/LeadRoutes.kt` defines `GET /leads/{id}`, but it is not mounted in `Routing.kt` yet.

## Project layout

```
src/main/kotlin/
├── main.kt, Http.kt, Serialization.kt, StatusPages.kt, Routing.kt   # Ktor modules & wiring
├── database/        # DatabaseFactory (Hikari + Flyway), Exposed tables, enums
├── domain/          # Domain models (Business, Agent, Lead, Conversation, …)
├── dto/             # Request / WhatsApp webhook payload DTOs
├── repositories/    # Data access
├── services/        # Lead ingestion, assignment, conversations, lead status, WhatsApp client
├── routes/          # HTTP routes
└── webhooks/        # WhatsApp webhook handler
src/main/resources/
├── application.yaml # Ktor + database config
└── db/migration/    # Flyway migrations (V1–V4)
```

## Configuration

The database connection comes from environment variables, which `application.yaml` reads:

| Variable      | Default     |
|---------------|-------------|
| `DB_HOST`     | `localhost` |
| `DB_PORT`     | `5432`      |
| `DB_NAME`     | `njored`    |
| `DB_USER`     | `postgres`  |
| `DB_PASSWORD` | *(empty)*   |

Flyway migrations run automatically at startup.

WhatsApp credentials are stored per business in the `whatsapp_accounts` table (phone number ID, access token, verify token), not in environment variables.

## Running locally

Requirements: JDK 21 and a PostgreSQL instance.

```bash
./gradlew run      # start the server on http://localhost:8080
./gradlew test     # run tests
./gradlew build    # build
./gradlew buildFatJar   # build a runnable fat jar into build/libs/
```

To expose your local server to Meta for webhook testing, use a tunnel (for example `cloudflared` or `ngrok`) and set the callback URL to `https://<tunnel>/webhooks/whatsapp`.

## Docker

The `Dockerfile` is a multi-stage build (Gradle 9 / JDK 21, then a Temurin 21 JRE runtime):

```bash
docker build -t edmound776/njored-backend:latest .
```

`docker-compose.yaml` runs the full stack:

- `postgres` (Postgres 16, persistent volume `njored_pg_data`, not exposed to the host)
- `ktor-app` (the `edmound776/njored-backend:latest` image on port `8080`, which starts after Postgres is healthy)
- `cloudflare-tunnel` (exposes the app publicly through a Cloudflare Tunnel)

Create a `.env` file next to the compose file:

```env
DB_PASSWORD=change-me
CLOUDFLARE_TUNNEL_TOKEN=your-tunnel-token
```

Then run:

```bash
docker compose up -d
```

## Related

- Frontend: [njored-frontend](https://github.com/Edmound766/njored-frontend)

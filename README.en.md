[Русский](README.md) | **English**

**SPORT TRAININGS** is a Telegram bot for tracking working weights in sports exercises and following the progress of each exercise over time.

Technologies used in the project:

**Backend:**
- ***Java 21***
- ***Spring Boot 3.5.7***


**Databases:**
- ***PostgreSQL*** (exercises and users' workout records)
- ***Redis*** (per-user bot state: building a chart, adding a new exercise record, adding a new exercise)
- ***Liquibase*** (PostgreSQL schema migrations in SQL format)

**Libraries:**
- ***TelegramBots*** (Telegram API integration)
- ***JFreeChart*** (chart rendering)

**Monitoring:**
- ***Loki*** (application log storage)
- ***Grafana*** (log browsing and dashboards)



**Features**

The bot provides the following functionality:

1. **New exercise**. The user can extend the exercise catalogue.

2. **New workout record**. The user can record the weights used in the exercises performed during the current workout.

3. **Exercise progress**. The user can follow how the weight changes from workout to workout by requesting a chart for the selected exercise. The bot sends the chart as a ***PNG*** image.



**PostgreSQL database schema**

<img width="411" height="348" alt="BOTdb - public" src="https://github.com/user-attachments/assets/dd1f0e6c-10ca-4793-8eeb-16743ef35702" />

The schema is created automatically when the application starts, using Liquibase migrations from `src/main/resources/db_migrations` (the entry point is `db.changelog-master.yaml`). To add a new migration, put an SQL file into that folder and include it in the master file.



**Running with Docker Compose**

Docker with the Compose plugin is required. The whole stack (bot, PostgreSQL, Redis, Loki, Grafana) starts with a single command.

1. Create a `.env` file from the example:

   ```bash
   cp .env.example .env
   ```

2. Fill in the variables in `.env`:

   | Variable | Description |
   |---|---|
   | `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` | PostgreSQL database name, user and password |
   | `POSTGRES_HOST_PORT` | PostgreSQL port on the host (default `5435`, reachable from `localhost` only) |
   | `APP_PORT` | Application port on the host (default `8080`) |
   | `BOT_TOKEN` | Bot token from [@BotFather](https://t.me/BotFather) |
   | `BOT_NAME` | Bot username |
   | `BOT_URL` | Public HTTPS webhook URL: `https://<your-domain>/callback/webhook` |
   | `GRAFANA_PORT` | Grafana port on the host (default `3000`) |
   | `GRAFANA_ADMIN_USER`, `GRAFANA_ADMIN_PASSWORD` | Grafana administrator login and password |

   The bot works through a webhook, so Telegram must be able to reach `BOT_URL`. For local development you can use a tunnel such as `ngrok http 8080` and put the resulting address into `BOT_URL`. If ngrok is not available (for example in Russia), use [Tuna](https://tuna.am): `tuna http 8080`.

3. Build the image and start the containers:

   ```bash
   docker compose up -d --build
   ```

   Startup order: PostgreSQL, Redis and Loki -> bot; Loki -> Grafana. Liquibase migrations are applied when the bot starts.

4. Add yourself to the users table. The bot only answers users who are listed there (`id` is your Telegram id):

   ```bash
   docker compose exec postgres sh -c 'psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "INSERT INTO users (id, name, is_blocked, is_admin) VALUES (123456789, '"'"'me'"'"', false, true)"'
   ```

5. Open a chat with the bot and send `/start`.

**Services and ports**

| Service | Address | Purpose |
|---|---|---|
| Bot | `http://localhost:8080` | Receives the webhook `POST /callback/webhook` |
| PostgreSQL | `localhost:5435` | Database (localhost only) |
| Redis | inside the Compose network | User states |
| Loki | inside the Compose network | Log storage (7-day retention) |
| Grafana | `http://localhost:3000` | Log browsing; the Loki data source is provisioned automatically |

**Logs and dashboards**

The bot ships its logs to Loki (the Spring profile `loki`, enabled in Compose). In Grafana open *Explore*, pick the Loki data source and filter by the label `app="sport_trainings"`. The anonymized update records (type and id, no user data) are meant for dashboards, for example the number of updates by type:

```
sum by (type) (count_over_time({app="sport_trainings"} |= "update_received" | logfmt [5m]))
```

Message text is logged at `debug` level only.

**Useful commands**

```bash
docker compose logs -f app      # bot logs
docker compose ps               # container status
docker compose down             # stop, data is kept
docker compose down -v          # stop and delete all data (DB, Redis, Loki, Grafana)
```

**Running without Docker**

You need Java 21, a running PostgreSQL (by default `localhost:5435`, database `BOTdb`) and Redis (`localhost:6379`). Set the environment variables `POSTGRES_PASSWORD`, `BOT_TOKEN`, `BOT_NAME`, `BOT_URL` and run `./gradlew bootRun`. Logs go to the console only unless the `loki` profile is enabled.

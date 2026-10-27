**Русский** | [English](README.en.md)

Проект **SPORT TRAININGS** - Telegram-бот для трекинга весов на спортивных упражнениях и отслеживания динамики упражнения.

Технологии, использованные в проекте:

**Backend:**
- ***Java 21***
- ***Spring Boot 3.5.7***


**Databases:**
- ***PostgreSQL*** (информация об упражнениях и тренировках пользователей)
- ***Redis*** (состояние бота для каждого пользователя: построение графика, новая запись об упражнении, новое упражнение)
- ***Liquibase*** (миграции схемы PostgreSQL в формате SQL)

**Библиотеки:**
- ***TelegramBots*** (связь с Telegram API)
- ***JFreeChart*** (построение графиков) 

**Мониторинг:**
- ***Loki*** (хранение логов приложения)
- ***Grafana*** (просмотр логов и дашборды)



**Функциональные возможности**

Бот обладает следующей функциональностью:

1. **Новое упражнение**. Бот позволяет пользователю пополнить базу упражнений.

2. **Новая запись о тренировке**. Пользователь может внести в информацию о весах, проработанных на тех упражнениях, которые на текущей тренировке выполнял.

3. **Динамика упражнений**. Пользователь имеет возможность отследить динамику весов по каждому упражнению от тренировки к тренировке, запрашивая график по выбранному упражнению. Бот выводит график в виде ***PNG***-изображения.



**Схема базы данных в PostgreSQL**

<img width="411" height="348" alt="BOTdb - public" src="https://github.com/user-attachments/assets/dd1f0e6c-10ca-4793-8eeb-16743ef35702" />

Схема создаётся автоматически при старте приложения миграциями Liquibase из `src/main/resources/db_migrations` (главный файл - `db.changelog-master.yaml`). Чтобы добавить новую миграцию, положите SQL-файл в эту папку и подключите его в master-файле.



**Запуск через Docker Compose**

Требуется установленный Docker с плагином Compose. Весь стек (бот, PostgreSQL, Redis, Loki, Grafana) поднимается одной командой.

1. Создайте файл `.env` на основе примера:

   ```bash
   cp .env.example .env
   ```

2. Заполните переменные в `.env`:

   | Переменная | Описание |
   |---|---|
   | `POSTGRES_DB`, `POSTGRES_USER`, `POSTGRES_PASSWORD` | Имя базы, пользователь и пароль PostgreSQL |
   | `POSTGRES_HOST_PORT` | Порт PostgreSQL на хосте (по умолчанию `5435`, доступен только с `localhost`) |
   | `APP_PORT` | Порт приложения на хосте (по умолчанию `8080`) |
   | `BOT_TOKEN` | Токен бота от [@BotFather](https://t.me/BotFather) |
   | `BOT_NAME` | Username бота |
   | `BOT_URL` | Публичный HTTPS-адрес вебхука: `https://<ваш-домен>/callback/webhook` |
   | `GRAFANA_PORT` | Порт Grafana на хосте (по умолчанию `3000`) |
   | `GRAFANA_ADMIN_USER`, `GRAFANA_ADMIN_PASSWORD` | Логин и пароль администратора Grafana |

   Бот работает через webhook, поэтому Telegram должен достучаться до `BOT_URL`. Для локальной разработки можно использовать туннель, например `ngrok http 8080`, и указать полученный адрес в `BOT_URL`. Если ngrok недоступен (например, в России), подойдёт [Tuna](https://tuna.am): `tuna http 8080`.

3. Соберите образ и запустите контейнеры:

   ```bash
   docker compose up -d --build
   ```

   Порядок запуска: PostgreSQL, Redis и Loki -> бот; Loki -> Grafana. Миграции Liquibase применяются при старте бота.

4. Добавьте себя в таблицу пользователей. Бот отвечает только тем, кто в ней есть (`id` - ваш Telegram id):

   ```bash
   docker compose exec postgres sh -c 'psql -U "$POSTGRES_USER" -d "$POSTGRES_DB" -c "INSERT INTO users (id, name, is_blocked, is_admin) VALUES (123456789, '"'"'me'"'"', false, true)"'
   ```

5. Откройте чат с ботом и отправьте `/start`.

**Сервисы и порты**

| Сервис | Адрес | Назначение |
|---|---|---|
| Бот | `http://localhost:8080` | Принимает вебхук `POST /callback/webhook` |
| PostgreSQL | `localhost:5435` | База данных (только с localhost) |
| Redis | внутри сети Compose | Состояния пользователей |
| Loki | внутри сети Compose | Хранилище логов (хранение 7 дней) |
| Grafana | `http://localhost:3000` | Просмотр логов, источник данных Loki настроен автоматически |

**Логи и дашборды**

Бот отправляет логи в Loki (профиль Spring `loki`, включён в Compose). В Grafana откройте *Explore*, выберите источник Loki и используйте метку `app="sport_trainings"`. Обезличенные записи об апдейтах (тип и номер, без данных пользователя) подходят для дашбордов, например количество апдейтов по типам:

```
sum by (type) (count_over_time({app="sport_trainings"} |= "update_received" | logfmt [5m]))
```

Текст сообщений пишется только на уровне `debug`.

**Полезные команды**

```bash
docker compose logs -f app      # логи бота
docker compose ps               # состояние контейнеров
docker compose down             # остановить, данные сохраняются
docker compose down -v          # остановить и удалить все данные (БД, Redis, Loki, Grafana)
```

**Запуск без Docker**

Нужны Java 21, запущенные PostgreSQL (по умолчанию `localhost:5435`, база `BOTdb`) и Redis (`localhost:6379`). Задайте переменные окружения `POSTGRES_PASSWORD`, `BOT_TOKEN`, `BOT_NAME`, `BOT_URL` и выполните `./gradlew bootRun`. Логи при этом идут только в консоль, пока не включён профиль `loki`.

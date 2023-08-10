# shop
Микросервисный шаблон с реализацией Spring Boot Security (OAuth2, JWT, RBAC, SSL/TLS), Event Sourcing, MQ, Saga

Стек: Java, Spring Boot / Cloud Stream, PostgreSQL, Kafka, Docker, Playwright, JobRunr.

## Сервисы

* IDP - провайдер учетных данных
* Gateway - API шлюз
* Account - сервис ресурсов (счета пользователей)


## Функционал API
* Пользователь может зарегистрироваться.
* Админ может просматривать информацию о счетах пользователей.

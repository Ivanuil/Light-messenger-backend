## KAFKA-TEST

### About

KAFKA-TEST это простой месенджер использующий Kafka и WebSocket для доставки сообщений.

### How to run

На данный момент сервис не имеет веб интерфейса, доступ возможен с помощью запросов через Postman.

Последовательность действий для запуска:

1. Запустить Zookeper, Kafka и Postgres.

    В [файле](util/docker-compose.yml) имеется готовая конфигурация для запуска в Docker.
    В случае запуска иным образом возможно потребуется изменить URL и порты в [файле](src/main/resources/application.properties)

2. Запустить сервис

3. Подключиться к сервису через Postman
    
    В [файле](util/KafkaTest.postman_collection.json) имеется готовая для использования колекция запросов.
    Подключение для получения сообщений происходит с помощью функции WebSocket Postman по Url ws://localhost:8080/websocket

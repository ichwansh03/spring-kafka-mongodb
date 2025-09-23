- [Using Spring for Apache Kafka](https://docs.spring.io/spring-kafka/reference/kafka.html)

```curl
curl --location 'http://localhost:8080/api/logs' \
--header 'Content-Type: application/json' \
--data '{
    "level": "INFO",
    "message": "User logged in 4",
    "timestamp": "2025-09-24T06:14:30Z",
    "metadata": {
        "userId": "1236",
        "ip": "192.168.1.13"
    }
}'
```
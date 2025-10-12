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

* access mongodb from terminal with command `docker exec -it <container_id> mongosh`
* initiate a replica set with command 
```mongosh
rs.initiate({
  _id: "rs0",
  members: [{ _id: 0, host: "mongodb:27017" }]
}) 
```
* check replica set status with command `rs.status()`
* create connector to debezium with payload
```curl
curl --location 'http://localhost:8083/connectors' \
--header 'Content-Type: application/json' \
--data '{
  "name": "mongo-connector",
  "config": {
    "connector.class": "io.debezium.connector.mongodb.MongoDbConnector",
    "mongodb.connection.string": "mongodb://mongodb:27017/?replicaSet=rs0",
    "database.include.list": "logsdb",
    "collection.include.list": "logsdb.logs",
    "topic.prefix": "mongo",
    "publish.full.document": "true"
  }
}' 
```
* change database and collection name in the payload as per your need
* check connector status with command `curl --location 'http://localhost:8083/connectors/mongo-connector/status'`
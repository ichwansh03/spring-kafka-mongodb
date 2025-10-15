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

### Setup MongoDB with Debezium Connector

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

### Setup Jenkins
* run compose file jenkins bundled with jdk17
* install maven inside jenkins bash with command `docker exec -it <container_id> bash` and then inside bash run
```bash
apt-get update
apt-get install maven -y
```
* run compose file, access jenkins at `http://localhost:8080`
* generate token from jenkins and use it in place of password in below command with command `docker exec -it jenkins_container_id cat /var/jenkins_home/secrets/initialAdminPassword`
* install suggested plugins and create first admin user
* create new job with maven project and use below command in place of your git repo link (example in `Jenkinsfile`)
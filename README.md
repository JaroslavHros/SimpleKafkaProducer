## SimpleKafka Producer 
Created as part of learning process.

## Folder Structure
The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

## Run Kafka Producer
1. compose cluster with commnad:
```bash
sudo docker compose up -d 
```
2. connect to the bash of one kafka container from cluster:
```bash
sudo docker exec -it "container-name" /bin/bash
```
3. create topic for client
```bash
kafka-topics --create --bootstrap-server localhost:9092 --topic replicatedTopic --partitions 3 --replication-factor 3
```
> **_NOTE:_** use at least three partitions and replication factor of three, to bring the high availability up.
4. check if the topic was create successfully via kafka-topics :
```bash
kafka-topics --list --bootstrap-server localhost:9092
```
or via additional tool called **kafkacat**:
```bash
kafkacat -b localhost:9092 -L
```
> **_NOTE:_** -L is used to list metadata about topics, metadata are returned with list of topics and their respective partitions with leaders and so on.
5. finally when topic is successfully create you can run producer from IDE or via CLI 

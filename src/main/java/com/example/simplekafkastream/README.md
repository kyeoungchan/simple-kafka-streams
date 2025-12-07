# 💻 실행 가이드
1. 실행하기 전 로컬에서 다음의 커맨드를 먼저 입력한다.
```shell
$ bin/kafka-topics.sh --create \
--bootstrap-server my-kafka:9092 \
--partitions 3 \
--topic stream_log

WARNING: Due to limitations in metric names, topics with a period ('.') or underscore ('_') could collide. To avoid issues it is best to use either, but not both.
Created topic stream_log.
```
2. `SimpleKafkaStreamApplication`을 실행한다.
3. 다음의 커맨드를 통해 `stream_log`에 데이터를 프로듀스한다.
```shell
$ bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 \
--topic stream_log
>hello
>kafka
>streams
```
4. 다음의 커맨드를 통해 `stream_log_copy` 토픽에서 데이터를 확인한다.
```shell
$ bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 \
--topic stream_log_copy --from-beginning
hello
kafka
streams
```


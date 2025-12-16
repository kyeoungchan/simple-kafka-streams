# 💻 실행 가이드
먼저 [조인 파트](https://github.com/kyeoungchan/simple-kafka-streams/tree/main/src/main/java/com/example/simplekafkastream/join)에서 실행 가이드대로 토픽들을 생성시킨 상황을 전제로 한다.  
`address` 토픽과는 달리 `address_v2` 토픽은 파티션이 2개이기 때문에 KStream으로 사용하는 `order`과 코파티셔닝이 되지 않는다.
```shell
$ bin/kafka-topics.sh --create \
--bootstrap-server my-kafka:9092 \
--partitions 2 \
--topic address_v2
```

<br>

`KStreamJoinGlobalKTable` 애플리케이션을 실행한다.

<br>

```shell
$ bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 \
--topic address_v2 \
--property "parse.key=true" \
--property "key.separator=:"
> kyeongchan:Gyeongju
> yeju:Seoul
```
```shell
$ bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 \
--topic order \
--property "parse.key=true" \
--property "key.separator=:"
> kyeongchan:iPhone2
> yeju:Galaxy2
```
```shell
$ bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 \
--topic order_join \
--property "print.key=true" \
--property "key.separator=:" \
--from-beginning
# 결과에 iPhone와 Galaxy 관련 내용은 KStreamJoinKTable 실습에서 남은 데이터들까지 같이 조인됐기 때문에 발생된 거다.
# 여기서는 iPone2와 Galaxy2 관련 내용만 보면 된다.
kyeongchan:iPhone send to Gyeongju
kyeongchan:Mac send to Seoul
kyeongchan:iPhone2 send to Gyeongju
yeju: Galaxy send to Seoul
yeju:Galaxy2 send to Seoul
```
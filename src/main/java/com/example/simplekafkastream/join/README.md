# 💻 실행 가이드
KTable로 사용할 토픽과 KStream으로 사용할 토픽을 만들 때 코파티셔닝을 시키기 위해서는 둘 다 파티션 개수를 동이랗게 만들어야 한다.
```shell
# address라는 이름의 Ktable 토픽 생성
$ bin/kafka-topics.sh --create \
--bootstrap-server my-kafka:9092 \
--partitions 3 \
--topic address

# order라는 이름의 KStream 토픽 생성
$ bin/kafka-topics.sh --create \
--bootstrap-server my-kafka:9092 \
--partitions 3 \
--topic order

# 조인된 데이터를 저장할 토픽 이름: order_join
$ bin/kafka-topics.sh --create \
--bootstrap-server my-kafka:9092 \
--partitions 3 \
--topic order_join
```
> KStream, KTable, GlobalKTable 모두 동일한 토픽이다.  
> 스트림즈 애플리케이션 내부에서 사용할 때 메시지 키와 메시지 값을 사용하는 형태를 구분할 뿐이다.

<br>

KStreamJoinKTable 애플리케이션을 실행시킨다.

<br>

KTable로 사용되는 address 토픽에 이름을 메시지 키로 하고 주소를 메시지 값으로 하는 데이터를 넣는다.
```shell
$ bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 \
--topic address \
--property "parse.key=true" \
--property "key.separator=:"
> kyeongchan:Gyeongju
> yeju:Seoul
```
<br>

KStream으로 사용되는 order 토픽에 이름을 메시지 키로 하고 주문 물품을 메시지 값으로 하는 데이터를 넣는다.
```shell
$ bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 \
--topic order \
--property "parse.key=true" \
--property "key.separator=:"
> kyeongchan:iPhone
> yeju:Galaxy
```

<br>

이제 order_join 토픽을 확인하여 order 토픽과 address 토픽이 조인된 데이터가 들어오는지 확인한다.
```shell
$ bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 \
--topic order_join \
--property "print.key=true" \
--property "key.separator=:" \
--from-beginning
kyeongchan:iPhone send to Gyeongju
yeju:Galaxy send to Seoul
^CProcessed a total of 2 messages
```

<br>

KTable은 address는 데이터가 업데이트가 되고,  
KStream인 order는 데이터가 추가되는 것을 확인할 수 있다.
```shell
$ bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 \
--topic address \
--property "parse.key=true" \
--property "key.separator=:"
> kyeongchan:Seoul
```
```shell
$ bin/kafka-console-producer.sh --bootstrap-server my-kafka:9092 \
--topic order \
--property "parse.key=true" \
--property "key.separator=:"
> kyeongchan:Mac
```

<br>

order_join 토픽을 확인해보자.
```shell
$ bin/kafka-console-consumer.sh --bootstrap-server my-kafka:9092 \
--topic order_join \
--property "print.key=true" \
--property "key.separator=:" \
--from-beginning
kyeongchan:iPhone send to Gyeongju
kyeongchan:Mac send to Seoul
yeju: Galaxy send to Seoul
^CProcessed a total of 3 messages
```


package com.example.simplekafkastream;

import java.util.Properties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SimpleKafkaStreamApplication {

    private static final String APPLICATION_NAME = "streams-application";
    private static final String BOOTSTRAP_SERVERS = "my-kafka:9092";
    private static final String STREAM_LOG = "stream_log";
    private static final String STREAM_LOG_COPY = "stream_log_copy";

    public static void main(String[] args) {

        Properties props = new Properties();

        // 스트림즈 애플리케이션은 애플리케이션 아이디 값을 기준으로 병렬처리하기 때문에 기존에 작성되지 않은 애플리케이션 아이디를 사용한다.
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, APPLICATION_NAME);

        // 스트림즈 애플리케이션과 연동할 카프카 클러스터 정보
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);

        // 스트림 처리를 위해 메시지 키와 메시지 값의 역직렬화, 직렬화 방식을 지정한다.
        // 스트림즈 애플리케이션에서는 데이터를 처리할 때 메시지 키 또는 메시지 값을 역직렬화하여 사용하고 최종적으로 데이터를 토픽에 넣을 때는 직렬화해서 데이터를 저장한다.
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        // 스트림 토폴로지를 정의하기 위한 StreamsBuilder
        StreamsBuilder builder = new StreamsBuilder();

        // stream_log 토픽으로부터 KStream 객체를 만들기 위해 StreamBuilder의 stream() 메서드 사용
        // KTable 만들 때는 table(), GlobalKTable 만들 때는 globalTable() 지원
        // stream() 메서드까지 3개의 메서드는 최초의 토픽 데이터를 가져오는 소스 프로세서다.
        KStream<String, String> streamLog = builder.stream(STREAM_LOG);

        // stream_log 토픽을 담은 KStream 객체를 다른 토픽으로 전송하기 위해 to() 메서드 사용
        // KStream 인스턴스의 데이터들을 특정 토픽으로 저장하기 위한 용도
        // to() 메서드는 싱크 프로세서다.
        streamLog.to(STREAM_LOG_COPY);

        // KafkaStreams 인스턴스를 실행하려면 start() 메서드를 사용하면 된다.
        // stream_log 토픽의 데이터를 stream_log_copy 토픽으로 전달한다.
        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }

}

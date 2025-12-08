package com.example.simplekafkastream.filter;

import java.util.Properties;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;

/**
 * 토픽으로 들어온 문자열 데이터 중 문자열의 길이가 5보다 큰 경우만 필터링 하는 스트림즈 애플리케이션
 * 스트림 프로세서가 추가되었다.
 */
public class StreamsFilter {

    private static final String APPLICATION_NAME = "streams-filter-application";
    private static final String BOOTSTRAP_SERVERS = "my-kafka:9092";
    private static final String STREAM_LOG = "stream_log";
    private static final String STREAM_LOG_FILTER = "stream_log_filter";

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
        KStream<String, String> streamLog = builder.stream(STREAM_LOG);

        // 메시지 값의 길이가 5보다 큰 경우만 필터링하도록 작성
        KStream<String, String> filteredStream = streamLog.filter(
                (key, value) -> value.length() > 5
        );

        // 필터링된 KStream을 stream_log_filter 토픽에 저장하도록 소스 프로세서 작성
        filteredStream.to(STREAM_LOG_FILTER);

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();
    }
}

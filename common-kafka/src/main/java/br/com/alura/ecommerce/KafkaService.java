package br.com.alura.ecommerce;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {

    private final KafkaConsumer<String, T> consumer;
    private final String groupId;
    private ConsumerFuncion parse;
    private Class<T> typeClass;

    public KafkaService(String groupId, String topic, ConsumerFuncion parse, Class<T> typeClass, Map<String, String> config) {
        this(parse, groupId, typeClass, config);
        consumer.subscribe(Collections.singletonList(topic));
    }

    private KafkaService(ConsumerFuncion parse, String groupId, Class<T> typeClass, Map<String, String> config) {
        this.parse = parse;
        this.groupId = groupId;
        this.typeClass = typeClass;
        this.consumer = new KafkaConsumer<>(getProperties(typeClass, groupId, config));
    }


    public KafkaService(String groupId, Pattern compile, ConsumerFuncion parse, Class<T> typeClass, Map<String, String> config) {
        this(parse, groupId, typeClass, config);
        this.consumer.subscribe(compile);
    }

    public void run() {

        while (true) {
            final ConsumerRecords<String, T> poll = consumer.poll(Duration.ofMillis(100));
            if (! poll.isEmpty()) {
                System.out.println("Encontrei! " + poll.count());
            }

            for (ConsumerRecord<String, T> stringStringConsumerRecord : poll) {
                try {
                    parse.consume(stringStringConsumerRecord);
                } catch (ExecutionException e) {
                    // just logging
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    // just logging
                    e.printStackTrace();
                }
            }
        }

    }

    private Properties getProperties(Class<T> type, String groupId, Map<String, String> propertiesMapOverride) {
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());

        properties.putAll(propertiesMapOverride);
        return properties;
    }

    @Override
    public void close() {
        this.consumer.close();
    }
}

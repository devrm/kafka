package br.com.alura.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaDispatcher<T> implements Closeable {

    private KafkaProducer<String, T> producer;

    public KafkaDispatcher() {
        this.producer = new KafkaProducer<>(getProperties());
    }

    private static Properties getProperties() {

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());

        return properties;
    }


    public void send(String topic, String key, T mensagem) throws ExecutionException, InterruptedException {
        final ProducerRecord<String, T> record = new ProducerRecord<>(topic, key, mensagem);

        final Callback callback = (recordMetadata, e) -> {
            if (e != null) {
                e.printStackTrace();
            }
            System.out.println(recordMetadata.topic() + ":::partition " + recordMetadata.partition() +
                    ":::offset " + recordMetadata.offset() + "::Data " + recordMetadata.timestamp());
        };

        this.producer.send(record, callback).get();
    }

    @Override
    public void close() {
        this.producer.close();
    }
}


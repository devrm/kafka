package br.com.alura.ecommerce;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class LogService {

     public static void main(String ...args) {
         Map<String, String> config = new HashMap<>();
         config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

         LogService logService = new LogService();
         try (KafkaService service = new KafkaService(LogService.class.getSimpleName(),
                 Pattern.compile("ECOMMERCE.*"),logService::parse, String.class, config)) {
             service.run();
         }
     }

     public void parse(ConsumerRecord<String, String> record) {
         System.out.println("--------------------------------------------------");
         System.out.println("Logging...");
         System.out.println(record.topic());
         System.out.println(record.key());
         System.out.println(record.value());
         System.out.println(record.partition());
         System.out.println(record.offset());
     }


}

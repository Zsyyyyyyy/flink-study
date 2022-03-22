package org.examples.app;

import akka.remote.serialization.StringSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Properties;

public class TestKafkaProducer {
        public KafkaProducer<String,String> kafkaProducer() {
            Properties properties = new Properties();
            properties.put("bootstrap.servers","127.0.0.1:9092");
            properties.put("key.serializer", StringSerializer.class.getName());
            properties.put("value.serializer", StringSerializer.class.getName());
            KafkaProducer<String,String> producer = new KafkaProducer<String, String>(properties);
            return producer;
        }


}

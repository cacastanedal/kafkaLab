package com.github.cacastanedal.kafka;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeys {
    public static void main(String[] args) {

        Logger logger = LoggerFactory.getLogger(ProducerDemoKeys.class);
        Properties properties = new Properties();

        // Kafka client address
        String bootstrapServers = "127.0.0.1:9092";

        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);

        // Kafka receives bites, it needs to serialize the java data type
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        logger.info("Before Kafka Producer");
        // Key and value data type needs to be specified
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);
        logger.info("After Kafka Producer");


        for(int i = 0; i<10; i++){

            String topic = "first_topic";
            String value = "hello world! " + i;
            String key = "Key_" + i+100;
            ProducerRecord<String, String> record =
                    new ProducerRecord<>(topic, key, value);

            logger.info("Key: " + key);

            // asynchronous
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                    if (e == null){
                        logger.info(
                                "Received new metadata.\n" +
                                        "Topic: " + recordMetadata.topic() + "\n" +
                                        "Partition: " + recordMetadata.partition() + "\n" +
                                        "Offset: " + recordMetadata.offset() + "\n" +
                                        "Timestamp: " + recordMetadata.timestamp()
                        );
                    } else {
                        logger.error("Error while producing", e.getMessage());
                    }
                }
            });
        }


        // producer.flush();
        logger.info("Closing producer");
        producer.close();

    }
}

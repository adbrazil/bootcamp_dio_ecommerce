package br.hatanaka.ecommerce.checkout.streaming;



import br.hatanaka.ecommerce.checkout.config.IKafkaConstants;
import br.hatanaka.ecommerce.checkout.entity.CheckoutEntity;
import br.hatanaka.ecommerce.checkout.event.CheckoutCreatedEvent;
import com.fasterxml.jackson.databind.ser.std.StringSerializer;
import com.google.gson.Gson;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


import org.apache.avro.data.Json;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.function.context.PollableBean;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

//import org.springframework.integration.support.MessageBuilder;


import java.util.*;




@RequiredArgsConstructor
//@Slf4j
public class CheckoutCreatedSource {

    String OUTPUT="checkout-created-output";


    Random random = new Random();


public void producer(CheckoutCreatedEvent checkout){

        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
        props.put("schema.registry.url", "http://localhost:8081");
       KafkaProducer  produto = new  KafkaProducer(props);



       ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(IKafkaConstants.TOPIC_NAME,
            checkout.toString());
       produto.send(record);

}





}
package br.hatanaka.ecommerce.payment.listener;


import br.hatanaka.ecommerce.checkout.event.CheckoutCreatedEvent;
import br.hatanaka.ecommerce.payment.config.IKafkaConstants;
import br.hatanaka.ecommerce.payment.entity.PaymentEntity;
import br.hatanaka.ecommerce.payment.event.PaymentCreatedEvent;
import br.hatanaka.ecommerce.payment.service.PaymentService;
import com.google.gson.Gson;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.util.Properties;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class CheckoutProcessor {



    private final PaymentService paymentService;



    CheckoutCreatedEvent checkout;


    @Bean
    public Function<String,String> fizzBuzzProcessor()  {
        return value -> {
            Gson gson = new Gson();

            CheckoutCreatedEvent checkoutCreatedEvent = gson.fromJson(value, CheckoutCreatedEvent.class);

            this.checkout= checkoutCreatedEvent;

            PaymentEntity paymentEntity = null;

            try{
                 paymentEntity = paymentService.create(checkoutCreatedEvent).get();
            }
            catch (Exception e){

            }

           // final PaymentEntity paymentEntity = paymentService.create(checkoutCreatedEvent).get()(()-> new Exception("Erro"));
            final PaymentCreatedEvent paymentCreatedEvent = PaymentCreatedEvent.newBuilder()
                    .setCheckoutCode(paymentEntity.getCheckoutCode())
                    .setPaymentCode(paymentEntity.getCode())
                    .build();


            Properties props = new Properties();
            props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, IKafkaConstants.KAFKA_BROKERS);
            props.put(ProducerConfig.CLIENT_ID_CONFIG, IKafkaConstants.CLIENT_ID);
            props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
            props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());
            props.put("schema.registry.url", "http://localhost:8081");
            KafkaProducer produto = new  KafkaProducer(props);



            ProducerRecord<Long, String> record = new ProducerRecord<Long, String>(IKafkaConstants.TOPIC_NAME,
                    checkout.toString());
            produto.send(record);



            return "" ;
        };
    }



    public Supplier<String> fizzBuzzProducer() {
        return () -> {

            return "";
        };

    }

    @Bean
    public Consumer<String> abacaxi() {
        return  payload-> payload.toString() ;
    }
}

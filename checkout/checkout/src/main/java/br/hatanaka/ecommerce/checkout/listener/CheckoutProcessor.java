package br.hatanaka.ecommerce.checkout.listener;


import br.hatanaka.ecommerce.checkout.entity.CheckoutEntity;
import br.hatanaka.ecommerce.checkout.event.CheckoutCreatedEvent;
import br.hatanaka.ecommerce.checkout.service.CheckoutService;

import br.hatanaka.ecommerce.payment.event.PaymentCreatedEvent;

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


    CheckoutCreatedEvent checkout;
    private final CheckoutService checkoutService;

    @Bean
    public Function<String,String> processor()  {
        return value -> {

            Gson gson = new Gson();

           PaymentCreatedEvent paymentCreatedEvent = gson.fromJson(value, PaymentCreatedEvent.class);


            checkoutService.updateStatus(paymentCreatedEvent.getCheckoutCode().toString(), CheckoutEntity.Status.APPROVED);

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

package br.hatanaka.ecommerce.checkout.service;

import br.hatanaka.ecommerce.checkout.entity.CheckoutEntity;
import br.hatanaka.ecommerce.checkout.event.CheckoutCreatedEvent;
import br.hatanaka.ecommerce.checkout.repository.CheckoutRepository;
import br.hatanaka.ecommerce.checkout.resource.CheckoutRequest;
import br.hatanaka.ecommerce.checkout.streaming.CheckoutCreatedSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CheckoutServiceImp implements  CheckoutService{

    private final CheckoutRepository checkoutRepository;

    private  CheckoutCreatedSource createdSource;



    @Override
    public Optional<CheckoutEntity> create(CheckoutRequest checkoutRequest) {
        final CheckoutEntity checkoutEntity= CheckoutEntity
                .builder()
                .code(UUID.randomUUID().toString())
                .status(CheckoutEntity.Status.CREATED)
                .build();

        final CheckoutEntity entity = checkoutRepository.save(checkoutEntity);
        final CheckoutCreatedEvent  checkoutCreatedEvent = CheckoutCreatedEvent
                .newBuilder()
                .setCheckoutCode(entity.getCode())
                .setStatus(entity.getStatus().name())
                .build();



        createdSource= new CheckoutCreatedSource();
        createdSource.producer(checkoutCreatedEvent);

        return Optional.of(entity);
    }

    @Override
    public Optional<CheckoutEntity> updateStatus(String checkoutCode, CheckoutEntity.Status status) {
        final CheckoutEntity checkoutEntity = checkoutRepository.findByCode(checkoutCode).orElse(CheckoutEntity.builder().build());
        checkoutEntity.setStatus(CheckoutEntity.Status.APPROVED);
        return Optional.of(checkoutRepository.save(checkoutEntity));
    }
}

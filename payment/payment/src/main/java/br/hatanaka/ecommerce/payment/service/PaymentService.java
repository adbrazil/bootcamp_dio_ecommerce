package br.hatanaka.ecommerce.payment.service;

import br.hatanaka.ecommerce.checkout.event.CheckoutCreatedEvent;
import br.hatanaka.ecommerce.payment.entity.PaymentEntity;

import java.util.Optional;

public interface PaymentService {

    Optional<PaymentEntity> create(CheckoutCreatedEvent checkoutCreatedEvent);
}

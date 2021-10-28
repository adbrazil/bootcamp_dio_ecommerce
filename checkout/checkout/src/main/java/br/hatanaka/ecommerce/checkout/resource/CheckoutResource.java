package br.hatanaka.ecommerce.checkout.resource;


import br.hatanaka.ecommerce.checkout.entity.CheckoutEntity;
import br.hatanaka.ecommerce.checkout.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;

@RestController
@RequestMapping("/v1/checkout")
@RequiredArgsConstructor
public class CheckoutResource {

    private final CheckoutService checkoutService;

    @PostMapping("/")
    public ResponseEntity<CheckoutResponse> create(@RequestBody CheckoutRequest checkoutRequest) throws Exception {
        //Corrigi esse ponto,talvez mudar a versão do JDK, parece que esta não possui o orElsThrow sem parâmetros
//        System.out.println(checkoutRequest);

        final CheckoutEntity checkoutEntity = checkoutService.create(checkoutRequest).orElseThrow(() -> new Exception("Erro"));
        final CheckoutResponse checkoutResponse =CheckoutResponse
                .builder()
                .code(checkoutEntity.getCode())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(checkoutResponse);
    }

}

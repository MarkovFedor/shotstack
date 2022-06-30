package com.team.stripe.service;

import com.team.stripe.repository.StripeRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StripeService {
    private final static String apiKey = "sk_test_51L7hlZDdHr4T9bFh2QNFgZEVj6HXCVYEZ3ApI1FgnvuaGry14xOFkHTHWnU1FOeMmOJpSmN2HidgBMINbabQurKG00GVrRxig8";
    @Autowired
    private StripeRepository stripeRepository;

    public String createPaymentIntent() throws StripeException {
        Stripe.apiKey = apiKey;
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()

                        .setAmount(1000L)
                        .setCurrency("eur")
                        .setCaptureMethod(PaymentIntentCreateParams.CaptureMethod.MANUAL)
                        .addAllPaymentMethodType(List.of("card"))
                        .setReceiptEmail("fedor.markov.1@mail.ru")
                        .setShipping(PaymentIntentCreateParams.Shipping.builder()
                                .setAddress(PaymentIntentCreateParams.Shipping.Address.builder()
                                        .setCity("Saints-Petersburg")
                                        .setCountry("Russia")
                                        .setLine1("Somewhere")
                                        .setLine2("32")
                                        .setState("Lenoblast'")
                                        .build())
                                .setCarrier("DHL")
                                .setName("Something")
                                .build())
                        .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent.getId();
    }

    public void confirmPaymentIntent(String intent_id) throws StripeException {
        Stripe.apiKey = apiKey;
        PaymentIntent paymentIntent = PaymentIntent.retrieve(intent_id);

        Map<String, Object> params = new HashMap<>();
        params.put("payment_method", "pm_card_visa");

        paymentIntent.confirm(params);
    }

    public void capturePaymentIntent(String intent_id) throws StripeException {
        Stripe.apiKey = apiKey;
        PaymentIntent intent = PaymentIntent.retrieve(intent_id);

        PaymentIntent updatedPaymentIntent = intent.capture();
    }
}
